package edu.dosw.proyect.core.services.impl;

import edu.dosw.proyect.controllers.dtos.request.RespuestaInvitacionRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.InvitacionResponseDTO;
import edu.dosw.proyect.core.exceptions.BusinessRuleException;
import edu.dosw.proyect.core.exceptions.ResourceNotFoundException;
import edu.dosw.proyect.controllers.mappers.InvitacionMapper;
import edu.dosw.proyect.core.models.enums.RespuestaInvitacion;
import edu.dosw.proyect.persistence.entity.EquipoJugadorEntity;
import edu.dosw.proyect.persistence.entity.InvitacionEntity;
import edu.dosw.proyect.persistence.entity.JugadorEntity;
import edu.dosw.proyect.persistence.mapper.InvitacionPersistenceMapper;
import edu.dosw.proyect.persistence.mapper.JugadorPersistenceMapper;
import edu.dosw.proyect.persistence.repository.EquipoJugadorRepository;
import edu.dosw.proyect.persistence.repository.InvitacionRepository;
import edu.dosw.proyect.persistence.repository.JugadorRepository;
import edu.dosw.proyect.core.services.InvitacionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class InvitacionServiceImpl implements InvitacionService {

    private static final int MAX_JUGADORES_POR_EQUIPO = 12;

    private final InvitacionRepository    invitacionRepository;
    private final JugadorRepository       jugadorRepository;
    private final EquipoJugadorRepository equipoJugadorRepository;
    private final InvitacionMapper        invitacionMapper;
    private final JugadorPersistenceMapper jugadorPersistenceMapper;
    private final InvitacionPersistenceMapper invitacionPersistenceMapper;

    @Override
    @Transactional
    public InvitacionResponseDTO responderInvitacion(Long jugadorId, Long invitacionId,
                                                     RespuestaInvitacionRequestDTO request) {
        log.info("Procesando respuesta: Jugador {} responde {} a invitación {}",
                jugadorId, request.getRespuesta(), invitacionId);

        if (request.getRespuesta() == null) {
            throw new BusinessRuleException("La respuesta a la invitación es obligatoria");
        }

        JugadorEntity jugadorEntity = jugadorRepository.findById(jugadorId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Jugador no encontrado con ID: " + jugadorId));

        InvitacionEntity invitacionEntity = invitacionRepository.findById(invitacionId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Invitación no encontrada con ID: " + invitacionId));

        if (!invitacionEntity.getJugador().getId().equals(jugadorId)) {
            throw new BusinessRuleException(
                    "Esta invitación no pertenece al jugador con ID: " + jugadorId);
        }

        if (!"PENDIENTE".equals(invitacionEntity.getEstado())) {
            throw new BusinessRuleException(
                    "La invitación ya fue " + invitacionEntity.getEstado().toLowerCase() +
                            " y no puede modificarse");
        }

        String nombreJugador = jugadorEntity.getUsuario() != null
                ? jugadorEntity.getUsuario().getName() : "Jugador";
        String mensajeCapitan;

        if (request.getRespuesta() == RespuestaInvitacion.ACEPTAR) {

            if (jugadorEntity.isTieneEquipo()) {
                invitacionEntity.setEstado("RECHAZADA");
                invitacionEntity.setFechaRespuesta(LocalDateTime.now());
                invitacionRepository.save(invitacionEntity);
                throw new BusinessRuleException(
                        "El jugador " + nombreJugador +
                                " ya pertenece a un equipo. La invitación fue rechazada automáticamente.");
            }

            List<EquipoJugadorEntity> integrantesActuales =
                    equipoJugadorRepository.findByEquipoId(
                            invitacionEntity.getEquipo().getId());

            if (integrantesActuales.size() >= MAX_JUGADORES_POR_EQUIPO) {
                throw new BusinessRuleException(
                        "El equipo '" + invitacionEntity.getEquipo().getNombre() +
                                "' ya tiene el máximo de " + MAX_JUGADORES_POR_EQUIPO +
                                " jugadores permitidos");
            }

            EquipoJugadorEntity vinculo = new EquipoJugadorEntity();
            vinculo.setEquipo(invitacionEntity.getEquipo());
            vinculo.setJugador(jugadorEntity);
            vinculo.setFechaUnion(LocalDateTime.now());
            vinculo.setActivo(true);
            equipoJugadorRepository.save(vinculo);

            jugadorEntity.setTieneEquipo(true);
            jugadorEntity.setDisponible(false);
            jugadorRepository.save(jugadorEntity);

            List<InvitacionEntity> otrasInvitaciones =
                    invitacionRepository.findByJugadorId(jugadorId);
            for (InvitacionEntity otra : otrasInvitaciones) {
                if (!otra.getId().equals(invitacionId) &&
                        "PENDIENTE".equals(otra.getEstado())) {
                    otra.setEstado("RECHAZADA");
                    otra.setFechaRespuesta(LocalDateTime.now());
                    invitacionRepository.save(otra);
                    log.info("Invitación {} rechazada automáticamente para jugador {}",
                            otra.getId(), jugadorId);
                }
            }

            invitacionEntity.setEstado("ACEPTADA");
            mensajeCapitan = "El jugador " + nombreJugador +
                    " ha aceptado tu invitación y se unió al equipo '" +
                    invitacionEntity.getEquipo().getNombre() + "'.";

            log.info("Jugador {} aceptó invitación {} y se unió al equipo {}",
                    jugadorId, invitacionId, invitacionEntity.getEquipo().getNombre());

        } else {
            invitacionEntity.setEstado("RECHAZADA");
            mensajeCapitan = "El jugador " + nombreJugador +
                    " ha rechazado tu invitación al equipo '" +
                    invitacionEntity.getEquipo().getNombre() + "'.";

            log.info("Jugador {} rechazó invitación {}", jugadorId, invitacionId);
        }

        invitacionEntity.setFechaRespuesta(LocalDateTime.now());
        invitacionRepository.save(invitacionEntity);

        return invitacionMapper.toResponseDTO(
                invitacionPersistenceMapper.toDomain(invitacionEntity),
                mensajeCapitan);
    }
}