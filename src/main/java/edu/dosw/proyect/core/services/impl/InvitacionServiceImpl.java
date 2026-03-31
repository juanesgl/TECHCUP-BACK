package edu.dosw.proyect.core.services.impl;

import edu.dosw.proyect.controllers.dtos.request.RespuestaInvitacionRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.InvitacionResponseDTO;
import edu.dosw.proyect.core.exceptions.BusinessRuleException;
import edu.dosw.proyect.core.exceptions.ResourceNotFoundException;
import edu.dosw.proyect.controllers.mappers.InvitacionMapper;
import edu.dosw.proyect.core.models.Invitacion;
import edu.dosw.proyect.core.models.Jugador;
import edu.dosw.proyect.core.models.enums.RespuestaInvitacion;
import edu.dosw.proyect.persistence.repository.InvitacionRepository;
import edu.dosw.proyect.persistence.repository.JugadorRepository;
import edu.dosw.proyect.persistence.repository.UserRepository;
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
        log.info("Iniciando procesamiento de respuesta: Jugador {} responde {} a la invitaciÃ³n {}", jugadorId,
                request.getRespuesta(), invitacionId);

        if (request.getRespuesta() == null) {
            log.warn("La peticiÃ³n recibida no contiene una respuesta vÃ¡lida.");
            throw new BusinessRuleException("La respuesta a la invitaciÃ³n es obligatoria");
        }

        Jugador jugador = jugadorRepository.findById(jugadorId)
                .orElseThrow(() -> new ResourceNotFoundException("Jugador no encontrado"));

        Invitacion invitacion = invitacionRepository.findById(invitacionId)
                .orElseThrow(() -> new ResourceNotFoundException("InvitaciÃ³n no encontrada"));

        if (!invitacion.getJugadorInvitado().getId().equals(jugadorId)) {
            throw new BusinessRuleException("La invitaciÃ³n no pertenece a este jugador");
        }

        if (!"PENDIENTE".equals(invitacion.getEstado())) {
            throw new BusinessRuleException("La invitaciÃ³n ya fue " + invitacion.getEstado());
        }

        if (request.getRespuesta() == RespuestaInvitacion.ACEPTAR) {
            log.info("El jugador {} ha elegido ACEPTAR la invitaciÃ³n {}. Validando reglas de negocio...", jugadorId,
                    invitacionId);

            if (jugador.isTieneEquipo()) {
                invitacion.setEstado("RECHAZADA");
                invitacionRepository.save(invitacion);
                throw new BusinessRuleException(
                        "Ya perteneces a un equipo de futbol, no puedes aceptar la invitaciÃ³n");
            }

            invitacion.setEstado("ACEPTADA");
            jugador.setTieneEquipo(true);
            jugadorRepository.save(jugador);

            if (jugador.getUsuario() != null) {
                userRepository.save(jugador.getUsuario());
            }

        } else {
            log.info("El jugador {} decidiÃ³ RECHAZAR la invitaciÃ³n {}.", jugadorId, invitacionId);
            invitacion.setEstado("RECHAZADA");
        }

        invitacionRepository.save(invitacion);

        String nombreJugador = jugador.getUsuario() != null ? jugador.getUsuario().getName() : jugador.getNombre();
        String mensajeCapitan = request.getRespuesta() == RespuestaInvitacion.ACEPTAR
                ? "El jugador " + nombreJugador + " ha aceptado tu invitaciÃ³n."
                : "El jugador " + nombreJugador + " ha rechazado tu invitaciÃ³n.";

        log.info("InvitaciÃ³n {} procesada exitosamente. Estado final: {}", invitacionId, invitacion.getEstado());

        return invitacionMapper.toResponseDTO(invitacion, mensajeCapitan);
    }
}
