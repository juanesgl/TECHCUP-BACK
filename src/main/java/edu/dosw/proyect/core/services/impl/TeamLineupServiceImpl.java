package edu.dosw.proyect.core.services.impl;

import edu.dosw.proyect.controllers.dtos.request.SaveLineupRequestDTO;
import edu.dosw.proyect.controllers.dtos.request.StarterEntryRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.TeamLineupResponseDTO;
import edu.dosw.proyect.controllers.mappers.TeamLineupMapper;
import edu.dosw.proyect.core.exceptions.BusinessRuleException;
import edu.dosw.proyect.core.exceptions.ResourceNotFoundException;
import edu.dosw.proyect.core.models.Equipo;
import edu.dosw.proyect.core.models.Partido;
import edu.dosw.proyect.core.models.TeamLineup;
import edu.dosw.proyect.core.models.User;
import edu.dosw.proyect.core.models.enums.LineupStatus;
import edu.dosw.proyect.core.models.enums.MatchStatus;
import edu.dosw.proyect.persistence.entity.EquipoEntity;
import edu.dosw.proyect.persistence.entity.PartidoEntity;
import edu.dosw.proyect.persistence.entity.UserEntity;
import edu.dosw.proyect.persistence.mapper.EquipoPersistenceMapper;
import edu.dosw.proyect.persistence.mapper.PartidoPersistenceMapper;
import edu.dosw.proyect.persistence.mapper.UserPersistenceMapper;
import edu.dosw.proyect.persistence.repository.EquipoRepository;
import edu.dosw.proyect.persistence.repository.PartidoRepository;
import edu.dosw.proyect.persistence.repository.TeamLineupRepository;
import edu.dosw.proyect.persistence.repository.UserRepository;
import edu.dosw.proyect.core.services.TeamLineupService;
import edu.dosw.proyect.core.services.authorization.AuthorizationValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamLineupServiceImpl implements TeamLineupService {

    private static final int REQUIRED_STARTERS = 7;

    private final TeamLineupRepository lineupRepository;
    private final EquipoRepository equipoRepository;
    private final PartidoRepository matchRepository;
    private final UserRepository userRepository;
    private final TeamLineupMapper lineupMapper;
    private final AuthorizationValidator authorizationValidator;
    private final PartidoPersistenceMapper partidoMapper;
    private final EquipoPersistenceMapper equipoMapper;
    private final UserPersistenceMapper userMapper;

    @Override
    public TeamLineupResponseDTO saveLineup(Long captainId, SaveLineupRequestDTO request) {
        UserEntity captainEntity = userRepository.findById(captainId)
                .orElseThrow(() -> new ResourceNotFoundException("Capitan no encontrado"));
        User captain = userMapper.toDomain(captainEntity);
        authorizationValidator.validatePermission(captain, "MANAGE_LINEUP");

        Equipo team = resolveTeam(request.getTeamId());
        Partido match = resolveScheduledMatch(request.getMatchId(), request.getTeamId());

        validateCaptainOwnership(captainId, team);
        validateStarterCount(request.getStarters());
        validateFormationSelected(request);
        validateFieldPositionsAssigned(request.getStarters());

        lineupRepository.findByTeamIdAndMatchId(request.getTeamId(), request.getMatchId())
                .ifPresent(e -> { throw new BusinessRuleException(
                        "Ya existe alineacion para este partido."); });

        Map<Long, User> playerMap = buildPlayerMap(request.getStarters());
        TeamLineup lineup = lineupMapper.toNewTeamLineup(request, captainId, playerMap);
        lineup.setTeamName(team.getNombre());
        lineupRepository.save(lineup);

        return lineupMapper.toResponseDTO(lineup, "Alineacion guardada exitosamente.");
    }

    @Override
    public TeamLineupResponseDTO updateLineup(Long captainId, Long lineupId,
                                              SaveLineupRequestDTO request) {
        UserEntity captainEntity = userRepository.findById(captainId)
                .orElseThrow(() -> new ResourceNotFoundException("Capitan no encontrado"));
        User captain = userMapper.toDomain(captainEntity);
        authorizationValidator.validatePermission(captain, "MANAGE_LINEUP");

        TeamLineup existing = lineupRepository.findById(lineupId)
                .orElseThrow(() -> new ResourceNotFoundException("Alineacion no encontrada"));

        validateCaptainOwnership(captainId, resolveTeam(existing.getTeamId()));
        validateLineupNotLocked(existing);
        validateStarterCount(request.getStarters());
        validateFormationSelected(request);
        validateFieldPositionsAssigned(request.getStarters());

        Map<Long, User> playerMap = buildPlayerMap(request.getStarters());
        lineupMapper.applyUpdate(existing, request, playerMap);
        lineupRepository.save(existing);

        return lineupMapper.toResponseDTO(existing, "Alineacion actualizada exitosamente.");
    }

    @Override
    public TeamLineupResponseDTO getLineup(Long captainId, Long teamId, Long matchId) {
        UserEntity captainEntity = userRepository.findById(captainId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        User captain = userMapper.toDomain(captainEntity);
        authorizationValidator.validatePermission(captain, "VIEW_OPPONENT_LINEUP");

        validateCaptainOwnership(captainId, resolveTeam(teamId));

        TeamLineup lineup = lineupRepository.findByTeamIdAndMatchId(teamId, matchId)
                .orElseThrow(() -> new ResourceNotFoundException("Alineacion no encontrada."));

        return lineupMapper.toResponseDTO(lineup, "Alineacion retornada exitosamente.");
    }

    @Override
    public List<TeamLineupResponseDTO> getTeamLineups(Long captainId, Long teamId) {
        UserEntity captainEntity = userRepository.findById(captainId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        authorizationValidator.validateOwnership(captainId, captainId);
        validateCaptainOwnership(captainId, resolveTeam(teamId));

        List<TeamLineup> lineups = lineupRepository.findByTeamId(teamId);
        return lineups.stream()
                .map(l -> lineupMapper.toResponseDTO(l, "Alineacion retornada exitosamente."))
                .collect(Collectors.toList());
    }

    private Equipo resolveTeam(Long teamId) {
        EquipoEntity entity = equipoRepository.findById(teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Equipo no encontrado"));
        return equipoMapper.toDomain(entity);
    }

    private Partido resolveScheduledMatch(Long matchId, Long teamId) {
        PartidoEntity entity = matchRepository.findById(matchId)
                .orElseThrow(() -> new ResourceNotFoundException("Partido no encontrado"));
        Partido match = partidoMapper.toDomain(entity);

        boolean teamInvolved =
                (match.getEquipoLocal() != null && teamId.equals(match.getEquipoLocal().getId())) ||
                        (match.getEquipoVisitante() != null && teamId.equals(match.getEquipoVisitante().getId()));

        if (!teamInvolved) throw new BusinessRuleException("El equipo no participa en este partido.");
        if (match.getEstado() != MatchStatus.PROGRAMADO)
            throw new BusinessRuleException("Solo se puede gestionar alineaciones en partidos programados.");

        return match;
    }

    private void validateCaptainOwnership(Long captainId, Equipo team) {
        if (team.getCapitan() == null || !captainId.equals(team.getCapitan().getId()))
            throw new BusinessRuleException("Solo el capitan puede gestionar la alineacion.");
    }

    private void validateStarterCount(List<StarterEntryRequestDTO> starters) {
        if (starters == null || starters.size() != REQUIRED_STARTERS)
            throw new BusinessRuleException("Debe seleccionar exactamente 7 titulares.");
    }

    private void validateFormationSelected(SaveLineupRequestDTO request) {
        if (request.getFormation() == null)
            throw new BusinessRuleException("Debe seleccionar una formacion tactica.");
    }

    private void validateFieldPositionsAssigned(List<StarterEntryRequestDTO> starters) {
        if (starters.stream().anyMatch(s -> s.getFieldPosition() == null))
            throw new BusinessRuleException("Cada titular debe tener una posicion asignada.");
    }

    private void validateLineupNotLocked(TeamLineup lineup) {
        if (lineup.getStatus() == LineupStatus.LOCKED)
            throw new BusinessRuleException("La alineacion no puede modificarse — el partido ya comenzo.");
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