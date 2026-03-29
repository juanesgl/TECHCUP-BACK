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
import edu.dosw.proyect.core.models.enums.UserRole;
import edu.dosw.proyect.core.repositories.EquipoRepository;
import edu.dosw.proyect.core.repositories.PartidoRepository;
import edu.dosw.proyect.core.repositories.TeamLineupRepository;
import edu.dosw.proyect.core.repositories.UserRepository;
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
    private final EquipoRepository     equipoRepository;
    private final PartidoRepository    matchRepository;
    private final UserRepository       userRepository;
    private final TeamLineupMapper     lineupMapper;
    private final AuthorizationValidator authorizationValidator;

    @Override
    public TeamLineupResponseDTO saveLineup(Long captainId, SaveLineupRequestDTO request) {
        log.info("Captain {} saving lineup for team {} / match {}",
                captainId, request.getTeamId(), request.getMatchId());

        User captain = userRepository.findById(captainId)
                .orElseThrow(() -> new ResourceNotFoundException("CapitÃ¡n no encontrado"));
        authorizationValidator.validatePermission(captain, "MANAGE_LINEUP");

        Equipo team  = resolveTeam(request.getTeamId());
        Partido match = resolveScheduledMatch(request.getMatchId(), request.getTeamId());

        validateCaptainOwnership(captainId, team);
        validateStarterCount(request.getStarters());
        validateFormationSelected(request);
        validateFieldPositionsAssigned(request.getStarters());

        lineupRepository.findByTeamIdAndMatchId(request.getTeamId(), request.getMatchId())
                .ifPresent(existing -> {
                    throw new BusinessRuleException(
                            "A lineup already exists for this match. Use the update endpoint.");
                });

        Map<Long, User> playerMap = buildPlayerMap(request.getStarters());

        TeamLineup lineup = lineupMapper.toNewTeamLineup(request, captainId, playerMap);
        lineup.setTeamName(team.getNombre());
        lineupRepository.save(lineup);

        log.info("Lineup saved successfully â€” ID: {}", lineup.getId());

        return lineupMapper.toResponseDTO(lineup,
                "Lineup saved successfully. Your team is ready for the match!");
    }

    @Override
    public TeamLineupResponseDTO updateLineup(Long captainId, Long lineupId,
                                              SaveLineupRequestDTO request) {
        log.info("Captain {} updating lineup ID: {}", captainId, lineupId);

        User captain = userRepository.findById(captainId)
                .orElseThrow(() -> new ResourceNotFoundException("CapitÃ¡n no encontrado"));
        
        authorizationValidator.validatePermission(captain, "MANAGE_LINEUP");

        TeamLineup existing = lineupRepository.findById(lineupId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Lineup not found with ID: " + lineupId));

        validateCaptainOwnership(captainId, resolveTeam(existing.getTeamId()));
        validateLineupNotLocked(existing);
        validateStarterCount(request.getStarters());
        validateFormationSelected(request);
        validateFieldPositionsAssigned(request.getStarters());

        resolveScheduledMatch(existing.getMatchId(), existing.getTeamId());

        Map<Long, User> playerMap = buildPlayerMap(request.getStarters());
        lineupMapper.applyUpdate(existing, request, playerMap);

        lineupRepository.save(existing);

        log.info("Lineup ID {} updated successfully.", lineupId);

        return lineupMapper.toResponseDTO(existing,
                "Lineup updated successfully. Changes saved correctly!");
    }

    @Override
    public TeamLineupResponseDTO getLineup(Long captainId, Long teamId, Long matchId) {
        log.info("Captain {} retrieving lineup for team {} / match {}", captainId, teamId, matchId);

        User captain = userRepository.findById(captainId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        
        authorizationValidator.validatePermission(captain, "VIEW_OPPONENT_LINEUP");

        validateCaptainOwnership(captainId, resolveTeam(teamId));

        TeamLineup lineup = lineupRepository
                .findByTeamIdAndMatchId(teamId, matchId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No lineup found for this team and match. "
                                + "There are no scheduled matches at the moment."));

        return lineupMapper.toResponseDTO(lineup, "Lineup retrieved successfully.");
    }

    @Override
    public List<TeamLineupResponseDTO> getTeamLineups(Long captainId, Long teamId) {
        log.info("Captain {} retrieving all lineups for team {}", captainId, teamId);

        User captain = userRepository.findById(captainId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        
        authorizationValidator.validateOwnership(captainId, captainId);

        validateCaptainOwnership(captainId, resolveTeam(teamId));

        List<TeamLineup> lineups = lineupRepository.findByTeamId(teamId);

        if (lineups.isEmpty()) {
            log.info("No lineups found for team {}. No scheduled matches at the moment.", teamId);
        }

        return lineups.stream()
                .map(l -> lineupMapper.toResponseDTO(l,
                        lineups.isEmpty()
                                ? "There are no scheduled matches at the moment."
                                : "Lineup retrieved successfully."))
                .collect(Collectors.toList());
    }

    private Equipo resolveTeam(Long teamId) {
        return equipoRepository.findById(teamId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Team not found with ID: " + teamId));
    }

    private Partido resolveScheduledMatch(Long matchId, Long teamId) {
        Partido match = matchRepository.findById(matchId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Match not found with ID: " + matchId));

        boolean teamInvolved =
                (match.getEquipoLocal()     != null && teamId.equals(match.getEquipoLocal().getId())) ||
                        (match.getEquipoVisitante() != null && teamId.equals(match.getEquipoVisitante().getId()));

        if (!teamInvolved) {
            throw new BusinessRuleException(
                    "The team is not participating in the specified match.");
        }

        if (match.getEstado() != MatchStatus.PROGRAMADO) {
            throw new BusinessRuleException(
                    "Lineup management is only allowed for scheduled matches. "
                            + "There are no scheduled matches at the moment.");
        }

        return match;
    }

    private void validateCaptainOwnership(Long captainId, Equipo team) {
        if (team.getCapitan() == null || !captainId.equals(team.getCapitan().getId())) {
            throw new BusinessRuleException(
                    "Only the team captain can manage the lineup.");
        }
    }

    private void validateStarterCount(List<StarterEntryRequestDTO> starters) {
        if (starters == null || starters.size() != REQUIRED_STARTERS) {
            throw new BusinessRuleException(
                    "You must select exactly 7 starter players.");
        }
    }

    private void validateFormationSelected(SaveLineupRequestDTO request) {
        if (request.getFormation() == null) {
            throw new BusinessRuleException(
                    "A tactical formation must be selected.");
        }
    }

    private void validateFieldPositionsAssigned(List<StarterEntryRequestDTO> starters) {
        boolean anyMissingPosition = starters.stream()
                .anyMatch(s -> s.getFieldPosition() == null);
        if (anyMissingPosition) {
            throw new BusinessRuleException(
                    "Every starter player must have a field position assigned.");
        }
    }

    private void validateLineupNotLocked(TeamLineup lineup) {
        if (lineup.getStatus() == LineupStatus.LOCKED) {
            throw new BusinessRuleException(
                    "The lineup can no longer be modified because the match has already started.");
        }
    }

    private Map<Long, User> buildPlayerMap(List<StarterEntryRequestDTO> starters) {
        Map<Long, User> map = new HashMap<>();
        for (StarterEntryRequestDTO entry : starters) {
            userRepository.findById(entry.getPlayerId())
                    .ifPresent(u -> map.put(entry.getPlayerId(), u));
        }
        return map;
    }
}
