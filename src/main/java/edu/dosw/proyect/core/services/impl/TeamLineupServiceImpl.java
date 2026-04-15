package edu.dosw.proyect.core.services.impl;

import edu.dosw.proyect.controllers.dtos.request.SaveLineupRequestDTO;
import edu.dosw.proyect.controllers.dtos.request.StarterEntryRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.TeamLineupResponseDTO;
import edu.dosw.proyect.controllers.mappers.TeamLineupMapper;
import edu.dosw.proyect.core.exceptions.BusinessRuleException;
import edu.dosw.proyect.core.exceptions.ResourceNotFoundException;
import edu.dosw.proyect.core.models.Team;
import edu.dosw.proyect.core.models.Partido;
import edu.dosw.proyect.core.models.TeamLineup;
import edu.dosw.proyect.core.models.User;
import edu.dosw.proyect.core.models.enums.LineupStatus;
import edu.dosw.proyect.core.models.enums.MatchStatus;
import edu.dosw.proyect.persistence.entity.TeamEntity;
import edu.dosw.proyect.persistence.entity.MatchEntity;
import edu.dosw.proyect.persistence.entity.TeamLineupEntity;
import edu.dosw.proyect.persistence.entity.UserEntity;
import edu.dosw.proyect.persistence.mapper.TeamPersistenceMapper;
import edu.dosw.proyect.persistence.mapper.MatchPersistenceMapper;
import edu.dosw.proyect.persistence.mapper.TeamLineupPersistenceMapper;
import edu.dosw.proyect.persistence.mapper.UserPersistenceMapper;
import edu.dosw.proyect.persistence.repository.TeamRepository;
import edu.dosw.proyect.persistence.repository.MatchRepository;
import edu.dosw.proyect.persistence.repository.TeamLineupJpaRepository;
import edu.dosw.proyect.persistence.repository.UserRepository;
import edu.dosw.proyect.core.services.TeamLineupService;
import edu.dosw.proyect.core.services.authorization.AuthorizationValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamLineupServiceImpl implements TeamLineupService {

    private static final int REQUIRED_STARTERS = 7;

    private final TeamLineupJpaRepository      lineupRepository;
    private final TeamRepository teamRepository;
    private final MatchRepository matchRepository;
    private final UserRepository               userRepository;
    private final TeamLineupMapper             lineupMapper;
    private final TeamLineupPersistenceMapper  lineupPersistenceMapper;
    private final AuthorizationValidator       authorizationValidator;
    private final MatchPersistenceMapper partidoMapper;
    private final TeamPersistenceMapper equipoMapper;
    private final UserPersistenceMapper        userMapper;

    @Override
    @Transactional
    public TeamLineupResponseDTO saveLineup(Long captainId, SaveLineupRequestDTO request) {
        UserEntity captainEntity = userRepository.findById(captainId)
                .orElseThrow(() -> new ResourceNotFoundException("Capitán no encontrado"));
        User captain = userMapper.toDomain(captainEntity);
        authorizationValidator.validatePermission(captain, "MANAGE_LINEUP");

        Team team = resolveTeam(request.getTeamId());
        resolveScheduledMatch(request.getMatchId(), request.getTeamId());

        validateCaptainOwnership(captainId, team);
        validateStarterCount(request.getStarters());
        validateFormationSelected(request);
        validateFieldPositionsAssigned(request.getStarters());

        lineupRepository.findByTeamIdAndMatchId(request.getTeamId(), request.getMatchId())
                .ifPresent(e -> { throw new BusinessRuleException(
                        "Ya existe alineación para este partido."); });

        Map<Long, User> playerMap = buildPlayerMap(request.getStarters());
        TeamLineup lineup = lineupMapper.toNewTeamLineup(request, captainId, playerMap);
        lineup.setTeamName(team.getNombre());

        TeamLineupEntity saved = lineupRepository.save(
                lineupPersistenceMapper.toEntity(lineup));
        TeamLineup savedDomain = lineupPersistenceMapper.toDomain(saved);

        return lineupMapper.toResponseDTO(savedDomain, "Alineación guardada exitosamente.");
    }

    @Override
    @Transactional
    public TeamLineupResponseDTO updateLineup(Long captainId, Long lineupId,
                                              SaveLineupRequestDTO request) {
        UserEntity captainEntity = userRepository.findById(captainId)
                .orElseThrow(() -> new ResourceNotFoundException("Capitán no encontrado"));
        User captain = userMapper.toDomain(captainEntity);
        authorizationValidator.validatePermission(captain, "MANAGE_LINEUP");

        TeamLineupEntity existingEntity = lineupRepository.findById(lineupId)
                .orElseThrow(() -> new ResourceNotFoundException("Alineación no encontrada"));
        TeamLineup existing = lineupPersistenceMapper.toDomain(existingEntity);

        validateCaptainOwnership(captainId, resolveTeam(existing.getTeamId()));
        validateLineupNotLocked(existing);
        validateStarterCount(request.getStarters());
        validateFormationSelected(request);
        validateFieldPositionsAssigned(request.getStarters());

        Map<Long, User> playerMap = buildPlayerMap(request.getStarters());
        lineupMapper.applyUpdate(existing, request, playerMap);

        TeamLineupEntity updated = lineupRepository.save(
                lineupPersistenceMapper.toEntity(existing));
        return lineupMapper.toResponseDTO(
                lineupPersistenceMapper.toDomain(updated),
                "Alineación actualizada exitosamente.");
    }

    @Override
    public TeamLineupResponseDTO getLineup(Long captainId, Long teamId, Long matchId) {
        UserEntity captainEntity = userRepository.findById(captainId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        User captain = userMapper.toDomain(captainEntity);
        authorizationValidator.validatePermission(captain, "VIEW_OPPONENT_LINEUP");

        validateCaptainOwnership(captainId, resolveTeam(teamId));

        TeamLineupEntity entity = lineupRepository.findByTeamIdAndMatchId(teamId, matchId)
                .orElseThrow(() -> new ResourceNotFoundException("Alineación no encontrada."));

        return lineupMapper.toResponseDTO(
                lineupPersistenceMapper.toDomain(entity),
                "Alineación retornada exitosamente.");
    }

    @Override
    public List<TeamLineupResponseDTO> getTeamLineups(Long captainId, Long teamId) {
        userRepository.findById(captainId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        authorizationValidator.validateOwnership(captainId, captainId);
        validateCaptainOwnership(captainId, resolveTeam(teamId));

        return lineupRepository.findByTeamId(teamId).stream()
                .map(e -> lineupMapper.toResponseDTO(
                        lineupPersistenceMapper.toDomain(e),
                        "Alineación retornada exitosamente."))
                .collect(Collectors.toList());
    }

    // ------------------------------------------------------------------ helpers

    private Team resolveTeam(Long teamId) {
        TeamEntity entity = teamRepository.findById(teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Equipo no encontrado"));
        return equipoMapper.toDomain(entity);
    }

    private void resolveScheduledMatch(Long matchId, Long teamId) {
        MatchEntity entity = matchRepository.findById(matchId)
                .orElseThrow(() -> new ResourceNotFoundException("Partido no encontrado"));
        Partido match = partidoMapper.toDomain(entity);

        boolean teamInvolved =
                (match.getTeamLocal() != null &&
                        teamId.equals(match.getTeamLocal().getId())) ||
                        (match.getTeamVisitante() != null &&
                                teamId.equals(match.getTeamVisitante().getId()));

        if (!teamInvolved)
            throw new BusinessRuleException("El equipo no participa en este partido.");
        if (match.getEstado() != MatchStatus.PROGRAMADO)
            throw new BusinessRuleException(
                    "Solo se pueden gestionar alineaciones en partidos programados.");
    }

    private void validateCaptainOwnership(Long captainId, Team team) {
        if (team.getCapitan() == null || !captainId.equals(team.getCapitan().getId()))
            throw new BusinessRuleException("Solo el capitán puede gestionar la alineación.");
    }

    private void validateStarterCount(List<StarterEntryRequestDTO> starters) {
        if (starters == null || starters.size() != REQUIRED_STARTERS)
            throw new BusinessRuleException("Debe seleccionar exactamente 7 titulares.");
    }

    private void validateFormationSelected(SaveLineupRequestDTO request) {
        if (request.getFormation() == null)
            throw new BusinessRuleException("Debe seleccionar una formación táctica.");
    }

    private void validateFieldPositionsAssigned(List<StarterEntryRequestDTO> starters) {
        if (starters.stream().anyMatch(s -> s.getFieldPosition() == null))
            throw new BusinessRuleException("Cada titular debe tener una posición asignada.");
    }

    private void validateLineupNotLocked(TeamLineup lineup) {
        if (lineup.getStatus() == LineupStatus.LOCKED)
            throw new BusinessRuleException(
                    "La alineación no puede modificarse — el partido ya comenzó.");
    }

    private Map<Long, User> buildPlayerMap(List<StarterEntryRequestDTO> starters) {
        Map<Long, User> map = new HashMap<>();
        for (StarterEntryRequestDTO entry : starters) {
            userRepository.findById(entry.getPlayerId())
                    .ifPresent(u -> map.put(entry.getPlayerId(), userMapper.toDomain(u)));
        }
        return map;
    }
}