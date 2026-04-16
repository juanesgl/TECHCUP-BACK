package edu.dosw.proyect.core.services.impl;

import edu.dosw.proyect.controllers.dtos.request.MatchEventRequestDTO;
import edu.dosw.proyect.controllers.dtos.request.RegisterMatchRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.RegisterMatchResponseDTO;
import edu.dosw.proyect.core.exceptions.BusinessRuleException;
import edu.dosw.proyect.core.exceptions.ResourceNotFoundException;
import edu.dosw.proyect.core.models.enums.MatchStatus;
import edu.dosw.proyect.core.models.enums.TipoEvento;
import edu.dosw.proyect.core.services.MatchRegistrationService;
import edu.dosw.proyect.persistence.entity.*;
import edu.dosw.proyect.persistence.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MatchRegistrationServiceImpl implements MatchRegistrationService {

    private final PartidoRepository           matchRepository;
    private final JugadorRepository           playerRepository;
    private final EquipoRepository            teamRepository;
    private final EventoPartidoRepository     eventRepository;
    private final EstadisticaEquipoRepository statsRepository;

    @Override
    @Transactional
    public RegisterMatchResponseDTO registerMatch(Long matchId, RegisterMatchRequestDTO request) {
        log.info("Registrando partido completo ID: {}", matchId);

        PartidoEntity match = matchRepository.findById(matchId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Partido con ID : " + matchId+" no encontrado"));

        if (match.getEstado() == MatchStatus.CANCELADO) {
            throw new BusinessRuleException(
                    "No se puede registrar resultado de un partido CANCELADO");
        }
        if (match.getEstado() == MatchStatus.FINALIZADO) {
            throw new BusinessRuleException(
                    "El partido ya tiene resultado registrado");
        }

        match.setGolesLocal(request.getHomeGoals());
        match.setGolesVisitante(request.getAwayGoals());
        match.setEstado(MatchStatus.FINALIZADO);
        matchRepository.save(match);
        log.info("Marcador guardado: {} - {}", request.getHomeGoals(), request.getAwayGoals());

        List<String> scorers      = new ArrayList<>();
        List<String> yellowCards  = new ArrayList<>();
        List<String> redCards     = new ArrayList<>();
        int totalYellow = 0;
        int totalRed    = 0;

        if (request.getEvents() != null) {
            for (MatchEventRequestDTO eventDTO : request.getEvents()) {

                JugadorEntity player = playerRepository.findById(eventDTO.getPlayerId())
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "Jugador con ID: " + eventDTO.getPlayerId()+ " no encontrado "));

                EquipoEntity team = teamRepository.findById(eventDTO.getTeamId())
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "Equipo con ID : " + eventDTO.getTeamId()+ " no encontrado "));

                validateTeamParticipates(match, team);

                EventoPartidoEntity event = EventoPartidoEntity.builder()
                        .partido(match)
                        .jugador(player)
                        .tipoEvento(eventDTO.getEventType())
                        .minuto(eventDTO.getMinute())
                        .descripcion(eventDTO.getDescription())
                        .build();
                eventRepository.save(event);

                String playerName = player.getUsuario() != null
                        ? player.getUsuario().getName()
                        : "Jugador #" + player.getId();
                String entry = playerName + " (" + team.getNombre()
                        + ", min. " + eventDTO.getMinute() + ")";

                if (eventDTO.getEventType() == TipoEvento.GOL) {
                    scorers.add(entry);
                } else if (eventDTO.getEventType() == TipoEvento.TARJETA_AMARILLA) {
                    yellowCards.add(entry);
                    totalYellow++;
                } else if (eventDTO.getEventType() == TipoEvento.TARJETA_ROJA) {
                    redCards.add(entry);
                    totalRed++;
                }
            }
        }

        if (match.getTorneo() != null) {
            updateTeamStats(match.getEquipoLocal(), match.getTorneo(),
                    request.getHomeGoals(), request.getAwayGoals());
            updateTeamStats(match.getEquipoVisitante(), match.getTorneo(),
                    request.getAwayGoals(), request.getHomeGoals());
        }

        String outcome;
        if (request.getHomeGoals() > request.getAwayGoals())      outcome = "LOCAL";
        else if (request.getHomeGoals() < request.getAwayGoals()) outcome = "VISITANTE";
        else                                                        outcome = "EMPATE";

        String homeName = match.getEquipoLocal() != null
                ? match.getEquipoLocal().getNombre() : "Equipo Local";
        String awayName = match.getEquipoVisitante() != null
                ? match.getEquipoVisitante().getNombre() : "Equipo Visitante";

        log.info("Partido ID {} registrado. Resultado: {} {}-{} {}",
                matchId, homeName, request.getHomeGoals(),
                request.getAwayGoals(), awayName);

        return RegisterMatchResponseDTO.builder()
                .matchId(matchId)
                .homeTeam(homeName)
                .awayTeam(awayName)
                .homeGoals(request.getHomeGoals())
                .awayGoals(request.getAwayGoals())
                .outcome(outcome)
                .totalGoals(scorers.size())
                .totalYellowCards(totalYellow)
                .totalRedCards(totalRed)
                .scorers(scorers)
                .yellowCardPlayers(yellowCards)
                .redCardPlayers(redCards)
                .message("Partido registrado exitosamente")
                .build();
    }


    private void validateTeamParticipates(PartidoEntity match, EquipoEntity team) {
        boolean participates =
                (match.getEquipoLocal() != null &&
                        team.getId().equals(match.getEquipoLocal().getId())) ||
                        (match.getEquipoVisitante() != null &&
                                team.getId().equals(match.getEquipoVisitante().getId()));

        if (!participates) {
            throw new BusinessRuleException(
                    "El equipo ID " + team.getId() + " no participa en este partido");
        }
    }

    private void updateTeamStats(EquipoEntity team, TournamentEntity tournament,
                                 int goalsFor, int goalsAgainst) {
        if (team == null) return;

        EstadisticasEquipoEntity stats = statsRepository
                .findByEquipoIdAndTorneoId(team.getId(), tournament.getId())
                .orElseGet(() -> {
                    EstadisticasEquipoEntity s = new EstadisticasEquipoEntity();
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
}