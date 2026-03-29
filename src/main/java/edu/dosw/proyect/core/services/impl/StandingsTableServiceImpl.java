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
import edu.dosw.proyect.core.models.EstadisticaEquipo;
import edu.dosw.proyect.core.models.Tournament;
import edu.dosw.proyect.core.models.enums.MatchStatus;
import edu.dosw.proyect.core.repositories.EstadisticaEquipoRepository;
import edu.dosw.proyect.core.repositories.PartidoRepository;
import edu.dosw.proyect.core.repositories.TournamentRepository;
import edu.dosw.proyect.core.services.StandingsTableService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Implementation of the Automatic Standings Table service.
 *
 * Business rules enforced:
 * - Only FINISHED or IN_PROGRESS matches count towards the standings.
 * - Results cannot be registered for CANCELLED matches.
 * - Results cannot be registered for already FINISHED matches.
 * - Sorting: Points > Goal Difference > Goals Scored > Team Name
 * (alphabetical).
 * - If no matches have been played, an empty table with all zeros is returned.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StandingsTableServiceImpl implements StandingsTableService {

    private final PartidoRepository matchRepository;
    private final EstadisticaEquipoRepository statsRepository;
    private final TournamentRepository tournamentRepository;
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

        // PERSIST STATISTICS (Req-06)
        updateTeamStats(match);

        log.info("Result registered and stats updated: match {} → {}:{} (FINISHED)",
                matchId, request.getHomeGoals(), request.getAwayGoals());

        return standingsMapper.toRegisterMatchResultResponseDTO(match);
    }

    private void updateTeamStats(Partido match) {
        if (match.getTorneo() == null)
            return;

        updateSingleTeamStats(match.getEquipoLocal(), match.getTorneo(), match.getGolesLocal(), match.getGolesVisitante());
        updateSingleTeamStats(match.getEquipoVisitante(), match.getTorneo(), match.getGolesVisitante(),
                match.getGolesLocal());
    }

    private void updateSingleTeamStats(Equipo team, Tournament tournament, int goalsFor, int goalsAgainst) {
        EstadisticaEquipo stats = statsRepository.findByEquipoIdAndTorneoId(team.getId(), tournament.getId())
                .orElseGet(() -> {
                    EstadisticaEquipo newStats = new EstadisticaEquipo();
                    newStats.setEquipo(team);
                    newStats.setTorneo(tournament);
                    return newStats;
                });

        stats.setPartidosJugados(stats.getPartidosJugados() + 1);
        stats.setGolesFavor(stats.getGolesFavor() + goalsFor);
        stats.setGolesContra(stats.getGolesContra() + goalsAgainst);
        stats.setDiferenciaGol(stats.getGolesFavor() - stats.getGolesContra());

        if (goalsFor > goalsAgainst) {
            stats.setPartidosGanados(stats.getPartidosGanados() + 1);
            stats.setPuntos(stats.getPuntos() + 3);
        } else if (goalsFor < goalsAgainst) {
            stats.setPartidosPerdidos(stats.getPartidosPerdidos() + 1);
        } else {
            stats.setPartidosEmpatados(stats.getPartidosEmpatados() + 1);
            stats.setPuntos(stats.getPuntos() + 1);
        }

        statsRepository.save(stats);
    }

    // ─────────────────────────────────────────────────────────────────
    // GET STANDINGS TABLE
    // ─────────────────────────────────────────────────────────────────

    @Override
    public StandingsTableResponseDTO getStandings(String tournamentId) {
        log.info("Retrieving standings table for tournament: {}", tournamentId);

        Tournament tournament = tournamentRepository.findByTournId(tournamentId)
                .orElseThrow(() -> new ResourceNotFoundException("Tournament not found: " + tournamentId));

        List<EstadisticaEquipo> allStats = statsRepository.findByTorneoIdOrderByPuntosDesc(tournament.getId());

        List<TeamStandingDTO> standings = new ArrayList<>();
        int position = 1;
        int totalMatchesPlayed = 0;

        for (EstadisticaEquipo stat : allStats) {
            standings.add(TeamStandingDTO.builder()
                    .position(position++)
                    .teamId(stat.getEquipo().getId())
                    .teamName(stat.getEquipo().getNombre())
                    .matchesPlayed(stat.getPartidosJugados())
                    .wins(stat.getPartidosGanados())
                    .draws(stat.getPartidosEmpatados())
                    .losses(stat.getPartidosPerdidos())
                    .goalsFor(stat.getGolesFavor())
                    .goalsAgainst(stat.getGolesContra())
                    .goalDifference(stat.getDiferenciaGol())
                    .points(stat.getPuntos())
                    .build());
            totalMatchesPlayed += stat.getPartidosJugados();
        }

        // Divide by 2 because each match is counted for both teams
        totalMatchesPlayed = totalMatchesPlayed / 2;

        log.info("Standings retrieved: {} teams from persistent storage.", standings.size());

        return standingsMapper.toStandingsTableResponseDTO(
                tournamentId, tournament.getName(), totalMatchesPlayed, standings);
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
            Long id = entry.getKey();
            long[] s = entry.getValue();
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