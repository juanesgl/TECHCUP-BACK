package edu.dosw.proyect.core.services.impl;

import edu.dosw.proyect.controllers.dtos.RegisterMatchResultRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.RegisterMatchResultResponseDTO;
import edu.dosw.proyect.controllers.dtos.response.StandingsTableResponseDTO;
import edu.dosw.proyect.controllers.dtos.response.TeamStandingDTO;
import edu.dosw.proyect.controllers.mappers.StandingsTableMapper;
import edu.dosw.proyect.core.exceptions.BusinessRuleException;
import edu.dosw.proyect.core.exceptions.ResourceNotFoundException;
import edu.dosw.proyect.core.models.Equipo;
import edu.dosw.proyect.core.models.Partido;
import edu.dosw.proyect.core.models.Tournament;
import edu.dosw.proyect.core.models.enums.MatchStatus;
import edu.dosw.proyect.core.repositories.PartidoRepository;
import edu.dosw.proyect.core.services.StandingsTableService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Implementation of the Automatic Standings Table service.
 *
 * Business rules enforced:
 *  - Only FINISHED or IN_PROGRESS matches count towards the standings.
 *  - Results cannot be registered for CANCELLED matches.
 *  - Results cannot be registered for already FINISHED matches.
 *  - Sorting: Points > Goal Difference > Goals Scored > Team Name (alphabetical).
 *  - If no matches have been played, an empty table with all zeros is returned.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StandingsTableServiceImpl implements StandingsTableService {

    private final PartidoRepository   matchRepository;
    private final StandingsTableMapper standingsMapper;

    // ─────────────────────────────────────────────────────────────────
    // REGISTER RESULT
    // ─────────────────────────────────────────────────────────────────

    @Override
    public RegisterMatchResultResponseDTO registerResult(Long matchId,
                                                         RegisterMatchResultRequestDTO request) {
        log.info("Registering result for match ID: {}", matchId);

        Partido match = matchRepository.findById(matchId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Match not found with ID: " + matchId));

        validateMatchIsRegistrable(match);

        match.setGolesLocal(request.getHomeGoals());
        match.setGolesVisitante(request.getAwayGoals());
        match.setEstado(MatchStatus.FINALIZADO);
        matchRepository.save(match);

        log.info("Result registered: match {} → {}:{} (FINISHED)",
                matchId, request.getHomeGoals(), request.getAwayGoals());

        return standingsMapper.toRegisterMatchResultResponseDTO(match);
    }

    // ─────────────────────────────────────────────────────────────────
    // GET STANDINGS TABLE
    // ─────────────────────────────────────────────────────────────────

    @Override
    public StandingsTableResponseDTO getStandings(String tournamentId) {
        log.info("Calculating standings table for tournament: {}", tournamentId);

        List<Partido> matches = matchRepository.findByTorneo_TournId(tournamentId);

        // Stats accumulator per team: teamId → [MP, W, D, L, GF, GA]
        Map<Long, long[]> accumulator = new LinkedHashMap<>();
        Map<Long, String> teamNames   = new HashMap<>();
        int totalMatchesPlayed        = 0;

        for (Partido match : matches) {
            if (!standingsMapper.isMatchCountable(match)) {
                continue;
            }

            Equipo home = match.getEquipoLocal();
            Equipo away = match.getEquipoVisitante();

            if (home == null || home.getId() == null
                    || away == null || away.getId() == null) {
                log.warn("Match {} skipped: team(s) with null ID.", match.getId());
                continue;
            }

            totalMatchesPlayed++;

            storeTeamName(teamNames, home);
            storeTeamName(teamNames, away);

            long[] sHome = statsFor(accumulator, home.getId());
            long[] sAway = statsFor(accumulator, away.getId());

            // MP
            sHome[0]++; sAway[0]++;
            // GF / GA
            sHome[4] += match.getGolesLocal();      sHome[5] += match.getGolesVisitante();
            sAway[4] += match.getGolesVisitante();  sAway[5] += match.getGolesLocal();

            if (match.getGolesLocal() > match.getGolesVisitante()) {
                sHome[1]++; sAway[3]++;   // W home / L away
            } else if (match.getGolesLocal() < match.getGolesVisitante()) {
                sAway[1]++; sHome[3]++;   // W away / L home
            } else {
                sHome[2]++; sAway[2]++;   // D both
            }
        }

        List<TeamStandingDTO> standings = buildSortedStandings(accumulator, teamNames);
        String tournamentName = resolveTournamentName(matches);

        log.info("Standings calculated: {} teams, {} matches played.",
                standings.size(), totalMatchesPlayed);

        return standingsMapper.toStandingsTableResponseDTO(
                tournamentId, tournamentName, totalMatchesPlayed, standings);
    }

    // ─────────────────────────────────────────────────────────────────
    // PRIVATE HELPERS
    // ─────────────────────────────────────────────────────────────────

    /** Validates that a match can receive a new result. */
    private void validateMatchIsRegistrable(Partido match) {
        if (match.getEstado() == MatchStatus.CANCELADO) {
            throw new BusinessRuleException(
                    "Cannot register a result for a CANCELLED match.");
        }
        if (match.getEstado() == MatchStatus.FINALIZADO) {
            throw new BusinessRuleException(
                    "This match already has a registered result (status: FINISHED).");
        }
    }

    /** Stores the team name in the auxiliary map (without overwriting). */
    private void storeTeamName(Map<Long, String> names, Equipo team) {
        names.putIfAbsent(team.getId(),
                team.getNombre() != null ? team.getNombre() : "Unnamed");
    }

    /** Gets or creates the stats array for a team: [MP, W, D, L, GF, GA]. */
    private long[] statsFor(Map<Long, long[]> accumulator, Long teamId) {
        return accumulator.computeIfAbsent(teamId, k -> new long[6]);
    }

    /**
     * Converts the accumulator map into a sorted list of TeamStandingDTO.
     * Sort order: Points DESC → GD DESC → GF DESC → Name ASC
     */
    private List<TeamStandingDTO> buildSortedStandings(Map<Long, long[]> accumulator,
                                                       Map<Long, String> names) {
        List<TeamStandingDTO> list = new ArrayList<>();

        for (Map.Entry<Long, long[]> entry : accumulator.entrySet()) {
            Long   id   = entry.getKey();
            long[] s    = entry.getValue();
            String name = names.getOrDefault(id, "Unknown");

            list.add(standingsMapper.toTeamStandingDTO(
                    0, id, name,
                    (int) s[0], (int) s[1], (int) s[2],
                    (int) s[3], (int) s[4], (int) s[5]));
        }

        list.sort(Comparator
                .comparingInt(TeamStandingDTO::getPoints).reversed()
                .thenComparingInt(TeamStandingDTO::getGoalDifference).reversed()
                .thenComparingInt(TeamStandingDTO::getGoalsFor).reversed()
                .thenComparing(TeamStandingDTO::getTeamName));

        // Assign 1-based position numbers
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setPosition(i + 1);
        }
        return list;
    }

    /** Attempts to resolve the tournament name from the available matches. */
    private String resolveTournamentName(List<Partido> matches) {
        return matches.stream()
                .map(Partido::getTorneo)
                .filter(Objects::nonNull)
                .map(Tournament::getName)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse("Tournament");
    }
}