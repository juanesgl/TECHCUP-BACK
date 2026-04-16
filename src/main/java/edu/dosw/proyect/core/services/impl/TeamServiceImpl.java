package edu.dosw.proyect.core.services.impl;

import edu.dosw.proyect.controllers.dtos.request.UpdateTeamRequestDTO;
import edu.dosw.proyect.controllers.dtos.request.CreateTeamRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.CreateTeamResponseDTO;
import edu.dosw.proyect.controllers.dtos.response.TeamResponseDTO;
import edu.dosw.proyect.controllers.mappers.TeamMapper;
import edu.dosw.proyect.core.exceptions.BusinessRuleException;
import edu.dosw.proyect.core.exceptions.ResourceNotFoundException;

import edu.dosw.proyect.core.services.TeamService;
import edu.dosw.proyect.persistence.entity.InvitationEntity;
import edu.dosw.proyect.persistence.entity.TeamEntity;
import edu.dosw.proyect.persistence.entity.TeamPlayerEntity;
import edu.dosw.proyect.persistence.entity.UserEntity;
import edu.dosw.proyect.persistence.mapper.TeamPersistenceMapper;
import edu.dosw.proyect.persistence.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {

    private static final int MIN_JUGADORES = 8;
    private static final int MAX_JUGADORES = 12;
    private static final List<String> CARRERAS_ADMITIDAS =
            Arrays.asList("sistemas", "ia", "ciberseguridad", "estadistica");

    private final TeamRepository teamRepository;
    private final UserRepository          userRepository;
    private final PlayerRepository playerRepository;
    private final InvitationRepository invitationRepository;
    private final TeamPlayerRepository teamPlayerRepository;
    private final TournamentRepository    torneoRepository;
    private final TeamMapper teamMapper;
    private final TeamPersistenceMapper teamPersistenceMapper;

    @Override
    @Transactional
    public CreateTeamResponseDTO crearEquipo(Long capitanId, CreateTeamRequestDTO request) {
        log.info("Iniciando creación de equipo por capitán ID: {}", capitanId);

        UserEntity capitan = userRepository.findById(capitanId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Capitán no encontrado con ID: " + capitanId));

        if (teamRepository.existsByNombre(request.getNombreEquipo())) {
            throw new BusinessRuleException(
                    "Ya existe un equipo con el nombre: " + request.getNombreEquipo());
        }

        List<UserEntity> integrantes = new ArrayList<>();
        List<String> notificaciones = new ArrayList<>();

        for (Long invitadoId : request.getJugadoresInvitadosIds()) {
            UserEntity invitado = userRepository.findById(invitadoId).orElse(null);
            if (invitado != null) {
                integrantes.add(invitado);
                notificaciones.add("Invitación enviada a " + invitado.getName());
            } else {
                notificaciones.add("Jugador con ID " + invitadoId + " no encontrado");
            }
        }

        integrantes.add(capitan);

        if (integrantes.size() < MIN_JUGADORES) {
            throw new BusinessRuleException(
                    "El equipo debe tener mínimo " + MIN_JUGADORES +
                            " integrantes incluyendo el capitán. Actualmente: " +
                            integrantes.size());
        }

        if (integrantes.size() > MAX_JUGADORES) {
            throw new BusinessRuleException(
                    "El equipo no puede tener más de " + MAX_JUGADORES +
                            " integrantes. Actualmente: " + integrantes.size());
        }

        long conteoCarreras = integrantes.stream()
                .filter(u -> u.getAcademicProgram() != null &&
                        CARRERAS_ADMITIDAS.contains(
                                u.getAcademicProgram().toLowerCase()))
                .count();

        if ((double) conteoCarreras / integrantes.size() <= 0.5) {
            throw new BusinessRuleException(
                    "Más de la mitad del equipo debe ser del programa de " +
                            "Ingeniería de Sistemas, IA, Ciberseguridad o Estadística.");
        }

        for (UserEntity integrante : integrantes) {
            if (!integrante.getId().equals(capitanId)) {
                boolean tieneEquipo = playerRepository.findById(integrante.getId())
                        .map(j -> j.isTieneEquipo()).orElse(false);
                if (tieneEquipo) {
                    throw new BusinessRuleException(
                            "El jugador " + integrante.getName() +
                                    " ya pertenece a un equipo.");
                }
            }
        }

        TeamEntity teamEntity = TeamEntity.builder()
                .nombre(request.getNombreEquipo())
                .escudoUrl(request.getEscudo())
                .colorUniformeLocal(request.getColoresUniforme())
                .capitan(playerRepository.findById(capitanId).orElse(null))
                .estadoInscripcion("PENDIENTE")
                .build();

        teamRepository.save(teamEntity);

        for (UserEntity integrante : integrantes) {
            if (!integrante.getId().equals(capitanId)) {
                InvitationEntity inv = InvitationEntity.builder()
                        .equipo(teamEntity)
                        .jugador(playerRepository.findById(integrante.getId()).orElse(null))
                        .estado("PENDIENTE")
                        .fechaEnvio(LocalDateTime.now())
                        .build();
                invitationRepository.save(inv);
            }
        }

        log.info("Equipo '{}' creado con ID: {}",
                teamEntity.getNombre(), teamEntity.getId());
        return teamMapper.toCrearEquipoResponseDTO(
                "El equipo '" + request.getNombreEquipo() +
                        "' fue creado exitosamente. Se enviaron " +
                        (integrantes.size() - 1) + " invitaciones.",
                notificaciones);
    }

    @Override
    public TeamResponseDTO consultarEquipo(Long equipoId) {
        log.info("Consultando equipo ID: {}", equipoId);
        TeamEntity entity = teamRepository.findById(equipoId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Equipo no encontrado con ID: " + equipoId));
        return teamMapper.toResponseDTO(teamPersistenceMapper.toDomain(entity));
    }

    @Override
    public List<TeamResponseDTO> consultarEquiposPorTorneo(String tournamentId) {
        log.info("Consultando equipos del torneo: {}", tournamentId);
        var torneo = torneoRepository.findByTournId(tournamentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Torneo no encontrado: " + tournamentId));
        return teamRepository.findByTorneoId(torneo.getId()).stream()
                .map(e -> teamMapper.toResponseDTO(teamPersistenceMapper.toDomain(e)))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TeamResponseDTO actualizarEquipo(Long equipoId, Long capitanId,
                                            UpdateTeamRequestDTO request) {
        log.info("Actualizando equipo ID: {} por capitán ID: {}", equipoId, capitanId);

        TeamEntity equipo = teamRepository.findById(equipoId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Equipo no encontrado con ID: " + equipoId));

        validarEsCapitan(equipo, capitanId);

        if (request.getNombreEquipo() != null && !request.getNombreEquipo().isBlank()) {
            if (!request.getNombreEquipo().equals(equipo.getNombre()) &&
                    teamRepository.existsByNombre(request.getNombreEquipo())) {
                throw new BusinessRuleException(
                        "Ya existe un equipo con el nombre: " + request.getNombreEquipo());
            }
            equipo.setNombre(request.getNombreEquipo());
        }
        if (request.getEscudo() != null && !request.getEscudo().isBlank()) {
            equipo.setEscudoUrl(request.getEscudo());
        }
        if (request.getColoresUniforme() != null && !request.getColoresUniforme().isBlank()) {
            equipo.setColorUniformeLocal(request.getColoresUniforme());
        }

        TeamEntity updated = teamRepository.save(equipo);
        log.info("Equipo ID: {} actualizado", equipoId);
        return teamMapper.toResponseDTO(teamPersistenceMapper.toDomain(updated));
    }

    @Override
    @Transactional
    public void eliminarEquipo(Long equipoId, Long capitanId) {
        log.info("Eliminando equipo ID: {} por capitán ID: {}", equipoId, capitanId);

        TeamEntity equipo = teamRepository.findById(equipoId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Equipo no encontrado con ID: " + equipoId));

        validarEsCapitan(equipo, capitanId);

        teamPlayerRepository.findByEquipoId(equipoId).forEach(ej -> {
            ej.getJugador().setTieneEquipo(false);
            ej.getJugador().setDisponible(true);
            playerRepository.save(ej.getJugador());
        });

        teamRepository.delete(equipo);
        log.info("Equipo ID: {} eliminado", equipoId);
    }

    @Override
    public List<String> consultarJugadoresEquipo(Long equipoId) {
        log.info("Consultando jugadores del equipo ID: {}", equipoId);

        teamRepository.findById(equipoId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Equipo no encontrado con ID: " + equipoId));

        return teamPlayerRepository.findByEquipoId(equipoId).stream()
                .filter(TeamPlayerEntity::isActivo)
                .map(ej -> {
                    String nombre = ej.getJugador().getUsuario() != null
                            ? ej.getJugador().getUsuario().getName()
                            : "Jugador #" + ej.getJugador().getId();
                    return nombre + " (ID: " + ej.getJugador().getId() + ")";
                })
                .collect(Collectors.toList());
    }

    private void validarEsCapitan(TeamEntity equipo, Long capitanId) {
        if (equipo.getCapitan() == null ||
                !equipo.getCapitan().getId().equals(capitanId)) {
            throw new BusinessRuleException(
                    "Solo el capitán del equipo puede realizar esta acción.");
        }
    }
}