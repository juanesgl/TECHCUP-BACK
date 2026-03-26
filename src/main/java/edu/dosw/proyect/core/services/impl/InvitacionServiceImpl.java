package edu.dosw.proyect.core.services.impl;

import edu.dosw.proyect.controllers.dtos.request.RespuestaInvitacionRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.InvitacionResponseDTO;
import edu.dosw.proyect.core.exceptions.BusinessRuleException;
import edu.dosw.proyect.core.exceptions.ResourceNotFoundException;
import edu.dosw.proyect.controllers.mappers.InvitacionMapper;
import edu.dosw.proyect.core.models.Invitacion;
import edu.dosw.proyect.core.models.User;
import edu.dosw.proyect.core.models.enums.EstadoInvitacion;
import edu.dosw.proyect.core.models.enums.RespuestaInvitacion;
import edu.dosw.proyect.core.repositories.InvitacionRepository;
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
    private final UserRepository userRepository;
    private final InvitacionMapper invitacionMapper;

    @Override
    public InvitacionResponseDTO responderInvitacion(Long jugadorId, Long invitacionId, RespuestaInvitacionRequestDTO request) {
        log.info("Iniciando procesamiento de respuesta: Jugador {} responde {} a la invitación {}", jugadorId, request.getRespuesta(), invitacionId);

        if (request.getRespuesta() == null) {
            log.warn("La petición recibida no contiene una respuesta válida.");
            throw new BusinessRuleException("La respuesta a la invitación es obligatoria");
        }

        User jugador = userRepository.findById(jugadorId)
                .orElseThrow(() -> new ResourceNotFoundException("Jugador no encontrado"));

        Invitacion invitacion = invitacionRepository.findById(invitacionId)
                .orElseThrow(() -> new ResourceNotFoundException("Invitación no encontrada"));

        if (!invitacion.getJugadorInvitado().getId().equals(jugadorId)) {
            throw new BusinessRuleException("La invitación no pertenece a este jugador");
        }

        if (invitacion.getEstado() != EstadoInvitacion.PENDIENTE) {
            throw new BusinessRuleException("La invitación ya fue " + invitacion.getEstado());
        }

        if (request.getRespuesta() == RespuestaInvitacion.ACEPTAR) {
            log.info("El jugador {} ha elegido ACEPTAR la invitación {}. Validando reglas de negocio...", jugadorId, invitacionId);
            if (jugador.getSportProfile() != null && jugador.getSportProfile().getEquipoActual() != null) {
                invitacion.setEstado(EstadoInvitacion.RECHAZADA);
                invitacionRepository.save(invitacion);
                log.warn("Violación TH-01: Jugador {} intentó aceptar la invitación {} pero ya pertenece a un equipo. Invitación rechazada automáticamente.", jugadorId, invitacionId);
                throw new BusinessRuleException("Ya perteneces a un equipo de futbol, no puedes aceptar la invitación");
            }
            
            invitacion.setEstado(EstadoInvitacion.ACEPTADA);
            if (jugador.getSportProfile() != null) {
                jugador.getSportProfile().setEquipoActual(invitacion.getEquipoInvita());
            } else {
                log.warn("Jugador {} aceptó pero no tiene perfil deportivo. Se omitió la asignación técnica de equipo.", jugadorId);
            }
            userRepository.save(jugador);
        } else {
            log.info("El jugador {} decidió RECHAZAR la invitación {}.", jugadorId, invitacionId);
            invitacion.setEstado(EstadoInvitacion.RECHAZADA);
        }

        invitacionRepository.save(invitacion);

        String mensajeCapitan = request.getRespuesta() == RespuestaInvitacion.ACEPTAR 
            ? "El jugador " + jugador.getName() + " ha aceptado tu invitación." 
            : "El jugador " + jugador.getName() + " ha rechazado tu invitación.";

        log.info("Invitación {} procesada exitosamente. Estado final: {}", invitacionId, invitacion.getEstado());
        
        return invitacionMapper.toResponseDTO(invitacion, mensajeCapitan);
    }
}
