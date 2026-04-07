package edu.dosw.proyect.core.services.impl;

import edu.dosw.proyect.controllers.dtos.request.RespuestaInvitacionRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.InvitacionResponseDTO;
import edu.dosw.proyect.core.exceptions.BusinessRuleException;
import edu.dosw.proyect.core.exceptions.ResourceNotFoundException;
import edu.dosw.proyect.controllers.mappers.InvitacionMapper;
import edu.dosw.proyect.core.models.Invitacion;
import edu.dosw.proyect.core.models.Jugador;
import edu.dosw.proyect.core.models.enums.RespuestaInvitacion;
import edu.dosw.proyect.persistence.entity.InvitacionEntity;
import edu.dosw.proyect.persistence.entity.JugadorEntity;
import edu.dosw.proyect.persistence.mapper.InvitacionPersistenceMapper;
import edu.dosw.proyect.persistence.mapper.JugadorPersistenceMapper;
import edu.dosw.proyect.persistence.repository.InvitacionRepository;
import edu.dosw.proyect.persistence.repository.JugadorRepository;
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
    private final InvitacionMapper invitacionMapper;
    private final JugadorPersistenceMapper jugadorPersistenceMapper;
    private final InvitacionPersistenceMapper invitacionPersistenceMapper;

    @Override
    public InvitacionResponseDTO responderInvitacion(Long jugadorId, Long invitacionId,
                                                     RespuestaInvitacionRequestDTO request) {
        log.info("Procesando respuesta: Jugador {} responde {} a la invitacion {}",
                jugadorId, request.getRespuesta(), invitacionId);

        if (request.getRespuesta() == null) {
            throw new BusinessRuleException("La respuesta a la invitacion es obligatoria");
        }

        JugadorEntity jugadorEntity = jugadorRepository.findById(jugadorId)
                .orElseThrow(() -> new ResourceNotFoundException("Jugador no encontrado"));

        InvitacionEntity invitacionEntity = invitacionRepository.findById(invitacionId)
                .orElseThrow(() -> new ResourceNotFoundException("Invitacion no encontrada"));

        if (!invitacionEntity.getJugador().getId().equals(jugadorId)) {
            throw new BusinessRuleException("La invitacion no pertenece a este jugador");
        }

        if (!"PENDIENTE".equals(invitacionEntity.getEstado())) {
            throw new BusinessRuleException("La invitacion ya fue " + invitacionEntity.getEstado());
        }

        if (request.getRespuesta() == RespuestaInvitacion.ACEPTAR) {
            if (jugadorEntity.isTieneEquipo()) {
                invitacionEntity.setEstado("RECHAZADA");
                invitacionRepository.save(invitacionEntity);
                throw new BusinessRuleException(
                        "Ya perteneces a un equipo, no puedes aceptar la invitacion");
            }
            invitacionEntity.setEstado("ACEPTADA");
            jugadorEntity.setTieneEquipo(true);
            jugadorRepository.save(jugadorEntity);
        } else {
            invitacionEntity.setEstado("RECHAZADA");
        }

        invitacionRepository.save(invitacionEntity);

        String nombreJugador = jugadorEntity.getUsuario() != null
                ? jugadorEntity.getUsuario().getName() : "Jugador";
        String mensajeCapitan = request.getRespuesta() == RespuestaInvitacion.ACEPTAR
                ? "El jugador " + nombreJugador + " ha aceptado tu invitacion."
                : "El jugador " + nombreJugador + " ha rechazado tu invitacion.";

        Invitacion invitacion = invitacionPersistenceMapper.toDomain(invitacionEntity);
        log.info("Invitacion {} procesada. Estado: {}", invitacionId, invitacionEntity.getEstado());
        return invitacionMapper.toResponseDTO(invitacion, mensajeCapitan);
    }
}