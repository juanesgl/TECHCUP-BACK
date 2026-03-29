package edu.dosw.proyect.core.services.impl;

import edu.dosw.proyect.controllers.dtos.request.RespuestaInvitacionRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.InvitacionResponseDTO;
import edu.dosw.proyect.core.exceptions.BusinessRuleException;
import edu.dosw.proyect.core.exceptions.ResourceNotFoundException;
import edu.dosw.proyect.controllers.mappers.InvitacionMapper;
import edu.dosw.proyect.core.models.Invitacion;
import edu.dosw.proyect.core.models.Jugador;
import edu.dosw.proyect.core.models.User;
import edu.dosw.proyect.core.models.enums.RespuestaInvitacion;
import edu.dosw.proyect.core.repositories.InvitacionRepository;
import edu.dosw.proyect.core.repositories.JugadorRepository;
import edu.dosw.proyect.core.repositories.UserRepository;
import edu.dosw.proyect.core.services.InvitacionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class InvitacionServiceImpl implements InvitacionService {

    private final InvitacionRepository invitacionRepository;
    private final JugadorRepository jugadorRepository;
    private final UserRepository userRepository;
    private final InvitacionMapper invitacionMapper;

    @Override
    public InvitacionResponseDTO responderInvitacion(Long jugadorId, Long invitacionId,
            RespuestaInvitacionRequestDTO request) {
        log.info("Iniciando procesamiento de respuesta: Jugador {} responde {} a la invitación {}", jugadorId,
                request.getRespuesta(), invitacionId);

        if (request.getRespuesta() == null) {
            log.warn("La petición recibida no contiene una respuesta válida.");
            throw new BusinessRuleException("La respuesta a la invitación es obligatoria");
        }

        Jugador jugador = jugadorRepository.findById(jugadorId)
                .orElseThrow(() -> new ResourceNotFoundException("Jugador no encontrado"));

        Invitacion invitacion = invitacionRepository.findById(invitacionId)
                .orElseThrow(() -> new ResourceNotFoundException("Invitación no encontrada"));

        if (!invitacion.getJugadorInvitado().getId().equals(jugadorId)) {
            throw new BusinessRuleException("La invitación no pertenece a este jugador");
        }

        if (!"PENDIENTE".equals(invitacion.getEstado())) {
            throw new BusinessRuleException("La invitación ya fue " + invitacion.getEstado());
        }

        if (request.getRespuesta() == RespuestaInvitacion.ACEPTAR) {
            log.info("El jugador {} ha elegido ACEPTAR la invitación {}. Validando reglas de negocio...", jugadorId,
                    invitacionId);
            
            User usuario = jugador.getUsuario();
            if (usuario != null && usuario.getSportProfile() != null && usuario.getSportProfile().getEquipoActual() != null) {
                invitacion.setEstado("RECHAZADA");
                invitacionRepository.save(invitacion);
                log.warn(
                        "Violación TH-01: Jugador {} intentó aceptar la invitación {} pero ya pertenece a un equipo. Invitación rechazada automáticamente.",
                        jugadorId, invitacionId);
                throw new BusinessRuleException("Ya perteneces a un equipo de futbol, no puedes aceptar la invitación");
            }

            invitacion.setEstado("ACEPTADA");
            if (usuario != null && usuario.getSportProfile() != null) {
                usuario.getSportProfile().setEquipoActual(invitacion.getEquipoInvita());
                userRepository.save(usuario);
            } else {
                log.warn("Jugador {} aceptó pero no tiene perfil deportivo o usuario asociado. Se omitió la asignación técnica de equipo.",
                        jugadorId);
            }
        } else {
            log.info("El jugador {} decidió RECHAZAR la invitación {}.", jugadorId, invitacionId);
            invitacion.setEstado("RECHAZADA");
        }

        invitacionRepository.save(invitacion);

        String nombreJugador = jugador.getUsuario() != null ? jugador.getUsuario().getName() : jugador.getNombre();
        String mensajeCapitan = request.getRespuesta() == RespuestaInvitacion.ACEPTAR
                ? "El jugador " + nombreJugador + " ha aceptado tu invitación."
                : "El jugador " + nombreJugador + " ha rechazado tu invitación.";

        log.info("Invitación {} procesada exitosamente. Estado final: {}", invitacionId, invitacion.getEstado());

        return invitacionMapper.toResponseDTO(invitacion, mensajeCapitan);
    }
}
