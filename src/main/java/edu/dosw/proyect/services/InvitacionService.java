package edu.dosw.proyect.services;

import edu.dosw.proyect.dtos.request.RespuestaInvitacionRequestDTO;
import edu.dosw.proyect.dtos.response.InvitacionResponseDTO;
import edu.dosw.proyect.exceptions.BusinessRuleException;
import edu.dosw.proyect.exceptions.ResourceNotFoundException;
import edu.dosw.proyect.mappers.InvitacionMapper;
import edu.dosw.proyect.models.Invitacion;
import edu.dosw.proyect.models.User;
import edu.dosw.proyect.models.enums.EstadoInvitacion;
import edu.dosw.proyect.models.enums.RespuestaInvitacion;
import edu.dosw.proyect.repositories.InvitacionRepository;
import edu.dosw.proyect.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class InvitacionService {

    private final InvitacionRepository invitacionRepository;
    private final UserRepository userRepository;
    private final InvitacionMapper invitacionMapper;

    public InvitacionResponseDTO responderInvitacion(Long jugadorId, Long invitacionId, RespuestaInvitacionRequestDTO request) {
        log.info("Jugador {} responde {} a la invitación {}", jugadorId, request.getRespuesta(), invitacionId);

        if (request.getRespuesta() == null) {
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
            // Validacion TH-01
            if (jugador.getSportProfile() != null && jugador.getSportProfile().getEquipoActual() != null) {
                invitacion.setEstado(EstadoInvitacion.RECHAZADA);
                invitacionRepository.save(invitacion);
                log.warn("Jugador {} intentó aceptar invitación pero ya tiene equipo. Rechazo automático (TH-01).", jugadorId);
                throw new BusinessRuleException("Ya perteneces a un equipo de futbol, no puedes aceptar la invitación");
            }
            
            // Flujo normal de aceptación
            invitacion.setEstado(EstadoInvitacion.ACEPTADA);
            if (jugador.getSportProfile() != null) {
                jugador.getSportProfile().setEquipoActual(invitacion.getEquipoInvita());
            } else {
                log.warn("El jugador {} no tiene Perfil Deportivo creado. Regla omitida por falta de perfil.", jugadorId);
            }
            userRepository.save(jugador);
        } else {
            invitacion.setEstado(EstadoInvitacion.RECHAZADA);
        }

        invitacionRepository.save(invitacion);

        String mensajeCapitan = request.getRespuesta() == RespuestaInvitacion.ACEPTAR 
            ? "El jugador " + jugador.getName() + " ha aceptado tu invitación." 
            : "El jugador " + jugador.getName() + " ha rechazado tu invitación.";

        log.info("Invitación {} procesada con estado final: {}", invitacionId, invitacion.getEstado());
        
        return invitacionMapper.toResponseDTO(invitacion, mensajeCapitan);
    }
}
