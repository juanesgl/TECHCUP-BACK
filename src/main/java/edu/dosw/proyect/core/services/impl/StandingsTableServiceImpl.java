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

@Slf4j
@Service
@RequiredArgsConstructor
public class StandingsTableServiceImpl implements StandingsTableService {

    private final PartidoRepository matchRepository;
    private final EstadisticaEquipoRepository statsRepository;
    private final TournamentRepository tournamentRepository;
    private final StandingsTableMapper standingsMapper;

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

        updateTeamStats(match);

        log.info("Result registered and stats updated: match {} â†’ {}:{} (FINISHED)",
                matchId, request.getHomeGoals(), request.getAwayGoals());

        return standingsMapper.toRegisterMatchResultResponseDTO(match);
    }

    private void updateTeamStats(Partido match) {
        if (match.getTorneo() == null)
            return;

        updateSingleTeamStats(match.getEquipoLocal(), match.getTorneo(),
                match.getGolesLocal(), match.getGolesVisitante());
        updateSingleTeamStats(match.getEquipoVisitante(), match.getTorneo(),
                match.getGolesVisitante(), match.getGolesLocal());
    }

    private void updateSingleTeamStats(Equipo team, Tournament tournament,
            int goalsFor, int goalsAgainst) {
        EstadisticaEquipo stats = statsRepository
                .findByEquipoIdAndTorneoId(team.getId(), tournament.getId())
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

    @Override
    public StandingsTableResponseDTO getStandings(String tournamentId) {
        String normalizedId = tournamentId == null ? null : tournamentId.trim().toUpperCase();
        log.info("Retrieving standings table for tournament: {} (normalizado: {})", tournamentId, normalizedId);

        Tournament tournament = tournamentRepository.findByTournId(normalizedId)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Tournament not found: " + normalizedId));

        List<EstadisticaEquipo> allStats =
                statsRepository.findByTorneoIdOrderByPuntosDesc(tournament.getId());

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

        
        totalMatchesPlayed = totalMatchesPlayed / 2;

        log.info("Standings retrieved: {} teams from persistent storage.", standings.size());

        return standingsMapper.toStandingsTableResponseDTO(
                tournamentId, tournament.getName(), totalMatchesPlayed, standings);
    }

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
}
