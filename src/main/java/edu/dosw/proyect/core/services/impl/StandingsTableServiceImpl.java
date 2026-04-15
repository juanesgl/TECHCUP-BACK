package edu.dosw.proyect.core.services.impl;

import edu.dosw.proyect.controllers.dtos.request.RegisterMatchResultRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.RegisterMatchResultResponseDTO;
import edu.dosw.proyect.controllers.dtos.response.StandingsTableResponseDTO;
import edu.dosw.proyect.controllers.dtos.response.TeamStandingDTO;
import edu.dosw.proyect.controllers.mappers.StandingsTableMapper;
import edu.dosw.proyect.core.exceptions.BusinessRuleException;
import edu.dosw.proyect.core.exceptions.ResourceNotFoundException;
import edu.dosw.proyect.core.models.Partido;
import edu.dosw.proyect.core.models.enums.MatchStatus;
import edu.dosw.proyect.persistence.entity.TeamStatisticsEntity;
import edu.dosw.proyect.persistence.entity.MatchEntity;
import edu.dosw.proyect.persistence.entity.TeamEntity;
import edu.dosw.proyect.persistence.entity.TournamentEntity;
import edu.dosw.proyect.persistence.mapper.MatchPersistenceMapper;
import edu.dosw.proyect.persistence.repository.TeamStatisticsRepository;
import edu.dosw.proyect.persistence.repository.MatchRepository;
import edu.dosw.proyect.persistence.repository.TournamentRepository;
import edu.dosw.proyect.core.services.StandingsTableService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StandingsTableServiceImpl implements StandingsTableService {

    private final MatchRepository matchRepository;
    private final TeamStatisticsRepository statsRepository;
    private final TournamentRepository tournamentRepository;
    private final StandingsTableMapper standingsMapper;
    private final MatchPersistenceMapper partidoMapper;

    @Override
    public RegisterMatchResultResponseDTO registerResult(Long matchId,
                                                         RegisterMatchResultRequestDTO request) {
        log.info("Registrando resultado partido ID: {}", matchId);

        MatchEntity entity = matchRepository.findById(matchId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Match not found with ID: " + matchId));

        Partido match = partidoMapper.toDomain(entity);
        validateMatchIsRegistrable(match);

        entity.setGolesLocal(request.getHomeGoals());
        entity.setGolesVisitante(request.getAwayGoals());
        entity.setEstado(MatchStatus.FINALIZADO);
        matchRepository.save(entity);

        updateTeamStats(entity);

        Partido updated = partidoMapper.toDomain(entity);
        return standingsMapper.toRegisterMatchResultResponseDTO(updated);
    }

    private void updateTeamStats(MatchEntity match) {
        if (match.getTorneo() == null) return;

        updateSingleTeamStats(match.getEquipoLocal(), match.getTorneo(),
                match.getGolesLocal(), match.getGolesVisitante());
        updateSingleTeamStats(match.getEquipoVisitante(), match.getTorneo(),
                match.getGolesVisitante(), match.getGolesLocal());
    }

    private void updateSingleTeamStats(
            TeamEntity team,
            TournamentEntity tournament, int goalsFor, int goalsAgainst) {
        if (team == null) return;

        TeamStatisticsEntity stats = statsRepository
                .findByEquipoIdAndTorneoId(team.getId(), tournament.getId())
                .orElseGet(() -> {
                    TeamStatisticsEntity s = new TeamStatisticsEntity();
                    s.setEquipo(team);
                    s.setTorneo(tournament);
                    return s;
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
        TournamentEntity tournament = tournamentRepository.findByTournId(tournamentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Tournament not found: " + tournamentId));

        List<TeamStatisticsEntity> allStats =
                statsRepository.findByTorneoIdOrderByPuntosDesc(tournament.getId());

        List<TeamStandingDTO> standings = new ArrayList<>();
        int position = 1;
        int totalMatchesPlayed = 0;

        for (TeamStatisticsEntity stat : allStats) {
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

        return standingsMapper.toStandingsTableResponseDTO(
                tournamentId, tournament.getName(), totalMatchesPlayed, standings);
    }

    private void validateMatchIsRegistrable(Partido match) {
        if (match.getEstado() == MatchStatus.CANCELADO) {
            throw new BusinessRuleException("No se puede registrar resultado de partido CANCELADO.");
        }
        if (match.getEstado() == MatchStatus.FINALIZADO) {
            throw new BusinessRuleException("El partido ya tiene resultado registrado.");
        }
    }
}