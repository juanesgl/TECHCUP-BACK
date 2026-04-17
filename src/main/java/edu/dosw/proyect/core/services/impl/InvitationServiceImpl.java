package edu.dosw.proyect.core.services.impl;

import edu.dosw.proyect.controllers.dtos.request.AnswerInvitationRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.InvitationResponseDTO;
import edu.dosw.proyect.core.exceptions.BusinessRuleException;
import edu.dosw.proyect.core.exceptions.ResourceNotFoundException;
import edu.dosw.proyect.controllers.mappers.InvitacionMapper;
import edu.dosw.proyect.core.models.enums.InvitationResponse;
import edu.dosw.proyect.persistence.entity.InvitationEntity;
import edu.dosw.proyect.persistence.entity.PlayerEntity;
import edu.dosw.proyect.persistence.entity.TeamPlayerEntity;
import edu.dosw.proyect.persistence.mapper.InvitationPersistenceMapper;

import edu.dosw.proyect.persistence.repository.TeamPlayerRepository;
import edu.dosw.proyect.persistence.repository.InvitationRepository;
import edu.dosw.proyect.persistence.repository.PlayerRepository;
import edu.dosw.proyect.core.services.InvitationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class InvitationServiceImpl implements InvitationService {

    private static final int MAX_JUGADORES_POR_EQUIPO = 12;
    private static final String ESTADO_PENDIENTE  = "PENDIENTE";
    private static final String ESTADO_RECHAZADA  = "RECHAZADA";
    private static final String ESTADO_ACEPTADA   = "ACEPTADA";

    private final InvitationRepository invitationRepository;
    private final PlayerRepository playerRepository;
    private final TeamPlayerRepository teamPlayerRepository;
    private final InvitacionMapper        invitacionMapper;

    private final InvitationPersistenceMapper invitationPersistenceMapper;

    @Override
    @Transactional
    public InvitationResponseDTO responderInvitacion(Long jugadorId, Long invitacionId,
                                                     AnswerInvitationRequestDTO request) {
        log.info("Procesando respuesta: Jugador {} responde {} a invitación {}",
                jugadorId, request.getRespuesta(), invitacionId);

        if (request.getRespuesta() == null) {
            throw new BusinessRuleException("La respuesta a la invitación es obligatoria");
        }

        PlayerEntity playerEntity = getJugadorOrThrow(jugadorId);
        InvitationEntity invitationEntity = getInvitacionOrThrow(invitacionId);

        validarInvitacion(jugadorId, invitationEntity);

        String nombreJugador = getNombreJugador(playerEntity);
        String mensajeCapitan;

        if (request.getRespuesta() == InvitationResponse.ACEPTAR) {
            mensajeCapitan = procesarAceptacion(jugadorId, invitacionId, playerEntity, invitationEntity, nombreJugador);
        } else {
            mensajeCapitan = procesarRechazo(jugadorId, invitacionId, invitationEntity, nombreJugador);
        }

        invitationEntity.setFechaRespuesta(LocalDateTime.now());
        invitationRepository.save(invitationEntity);

        return invitacionMapper.toResponseDTO(
                invitationPersistenceMapper.toDomain(invitationEntity),
                mensajeCapitan);
    }

    private PlayerEntity getJugadorOrThrow(Long jugadorId) {
        return playerRepository.findById(jugadorId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Jugador no encontrado con ID: " + jugadorId));
    }

    private InvitationEntity getInvitacionOrThrow(Long invitacionId) {
        return invitationRepository.findById(invitacionId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Invitación no encontrada con ID: " + invitacionId));
    }

    private void validarInvitacion(Long jugadorId, InvitationEntity invitationEntity) {
        if (!invitationEntity.getJugador().getId().equals(jugadorId)) {
            throw new BusinessRuleException(
                    "Esta invitación no pertenece al jugador con ID: " + jugadorId);
        }

        if (!ESTADO_PENDIENTE.equals(invitationEntity.getEstado())) {
            throw new BusinessRuleException(
                    "La invitación ya fue " + invitationEntity.getEstado().toLowerCase() +
                            " y no puede modificarse");
        }
    }

    private String getNombreJugador(PlayerEntity playerEntity) {
        return playerEntity.getUsuario() != null
                ? playerEntity.getUsuario().getName() : "Jugador";
    }

    private String procesarAceptacion(Long jugadorId, Long invitacionId, PlayerEntity playerEntity,
                                      InvitationEntity invitationEntity, String nombreJugador) {
        validarJugadorSinEquipo(playerEntity, invitationEntity, nombreJugador);
        validarCapacidadEquipo(invitationEntity);

        vincularJugadorAEquipo(playerEntity, invitationEntity);
        rechazarOtrasInvitaciones(jugadorId, invitacionId);

        invitationEntity.setEstado(ESTADO_ACEPTADA);
        
        log.info("Jugador {} aceptó invitación {} y se unió al equipo {}",
                jugadorId, invitacionId, invitationEntity.getEquipo().getNombre());
                
        return "El jugador " + nombreJugador +
                " ha aceptado tu invitación y se unió al equipo '" +
                invitationEntity.getEquipo().getNombre() + "'.";
    }

    private void validarJugadorSinEquipo(PlayerEntity playerEntity, InvitationEntity invitationEntity, String nombreJugador) {
        if (playerEntity.isTieneEquipo()) {
            invitationEntity.setEstado(ESTADO_RECHAZADA);
            invitationEntity.setFechaRespuesta(LocalDateTime.now());
            invitationRepository.save(invitationEntity);
            throw new BusinessRuleException(
                    "El jugador " + nombreJugador +
                            " ya pertenece a un equipo. La invitación fue rechazada automáticamente.");
        }
    }

    private void validarCapacidadEquipo(InvitationEntity invitationEntity) {
        List<TeamPlayerEntity> integrantesActuales =
                teamPlayerRepository.findByEquipoId(
                        invitationEntity.getEquipo().getId());

        if (integrantesActuales.size() >= MAX_JUGADORES_POR_EQUIPO) {
            throw new BusinessRuleException(
                    "El equipo '" + invitationEntity.getEquipo().getNombre() +
                            "' ya tiene el máximo de " + MAX_JUGADORES_POR_EQUIPO +
                            " jugadores permitidos");
        }
    }

    private void vincularJugadorAEquipo(PlayerEntity playerEntity, InvitationEntity invitationEntity) {
        TeamPlayerEntity vinculo = new TeamPlayerEntity();
        vinculo.setEquipo(invitationEntity.getEquipo());
        vinculo.setJugador(playerEntity);
        vinculo.setFechaUnion(LocalDateTime.now());
        vinculo.setActivo(true);
        teamPlayerRepository.save(vinculo);

        playerEntity.setTieneEquipo(true);
        playerEntity.setDisponible(false);
        playerRepository.save(playerEntity);
    }

    private void rechazarOtrasInvitaciones(Long jugadorId, Long invitacionIdActual) {
        List<InvitationEntity> otrasInvitaciones =
                invitationRepository.findByJugadorId(jugadorId);
        for (InvitationEntity otra : otrasInvitaciones) {
            if (!otra.getId().equals(invitacionIdActual) &&
                    ESTADO_PENDIENTE.equals(otra.getEstado())) {
                otra.setEstado(ESTADO_RECHAZADA);
                otra.setFechaRespuesta(LocalDateTime.now());
                invitationRepository.save(otra);
                log.info("Invitación {} rechazada automáticamente para jugador {}",
                        otra.getId(), jugadorId);
            }
        }
    }

    private String procesarRechazo(Long jugadorId, Long invitacionId, InvitationEntity invitationEntity, String nombreJugador) {
        invitationEntity.setEstado(ESTADO_RECHAZADA);
        
        log.info("Jugador {} rechazó invitación {}", jugadorId, invitacionId);
        
        return "El jugador " + nombreJugador +
                " ha rechazado tu invitación al equipo '" +
                invitationEntity.getEquipo().getNombre() + "'.";
    }
}