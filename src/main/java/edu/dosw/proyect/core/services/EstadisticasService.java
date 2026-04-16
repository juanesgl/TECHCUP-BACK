package edu.dosw.proyect.core.services;

import edu.dosw.proyect.controllers.dtos.response.*;
import edu.dosw.proyect.core.models.Team;
import edu.dosw.proyect.core.models.Player;
import edu.dosw.proyect.core.models.Partido;
import edu.dosw.proyect.core.models.User;
import edu.dosw.proyect.core.models.enums.MatchStatus;
import edu.dosw.proyect.core.models.enums.EventType;
import edu.dosw.proyect.persistence.entity.MatchEntity;
import edu.dosw.proyect.persistence.entity.MatchEventEntity;
import edu.dosw.proyect.persistence.mapper.PlayerPersistenceMapper;
import edu.dosw.proyect.persistence.mapper.MatchPersistenceMapper;
import edu.dosw.proyect.persistence.repository.MatchEventRepository;
import edu.dosw.proyect.persistence.repository.MatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class EstadisticasService {

    private final MatchRepository matchRepository;
    private final MatchEventRepository matchEventRepository;
    private final MatchPersistenceMapper partidoMapper;
    private final PlayerPersistenceMapper jugadorMapper;

    public TournamentStatisticsDTO obtenerEstadisticasTorneo(String tournId) {
        List<MatchEntity> entities = matchRepository.findByTorneo_TournId(tournId);
        List<Partido> partidos = entities.stream()
                .map(partidoMapper::toDomain).toList();

        List<MatchEventEntity> eventoEntities =
                matchEventRepository.findByPartido_Torneo_TournId(tournId);

        Map<Long, TeamStatisticsDTO> mapEquipos = new HashMap<>();
        int totalGolesAnotados = 0;
        int partidosJugados = 0;

        for (Partido p : partidos) {
            if (p.getEstado() == MatchStatus.FINALIZADO || p.getEstado() == MatchStatus.EN_JUEGO) {
                partidosJugados++;
                totalGolesAnotados += (p.getGolesLocal() + p.getGolesVisitante());

                TeamStatisticsDTO local = getOrCreateEquipoStats(mapEquipos, p.getTeamLocal());
                TeamStatisticsDTO visitante = getOrCreateEquipoStats(mapEquipos, p.getTeamVisitante());

                local.setPartidosJugados(local.getPartidosJugados() + 1);
                visitante.setPartidosJugados(visitante.getPartidosJugados() + 1);
                local.setGolesFavor(local.getGolesFavor() + p.getGolesLocal());
                local.setGolesContra(local.getGolesContra() + p.getGolesVisitante());
                visitante.setGolesFavor(visitante.getGolesFavor() + p.getGolesVisitante());
                visitante.setGolesContra(visitante.getGolesContra() + p.getGolesLocal());

                if (p.getGolesLocal() > p.getGolesVisitante()) {
                    local.setVictorias(local.getVictorias() + 1);
                    local.setPuntos(local.getPuntos() + 3);
                    visitante.setDerrotas(visitante.getDerrotas() + 1);
                } else if (p.getGolesLocal() < p.getGolesVisitante()) {
                    visitante.setVictorias(visitante.getVictorias() + 1);
                    visitante.setPuntos(visitante.getPuntos() + 3);
                    local.setDerrotas(local.getDerrotas() + 1);
                } else {
                    local.setEmpates(local.getEmpates() + 1);
                    local.setPuntos(local.getPuntos() + 1);
                    visitante.setEmpates(visitante.getEmpates() + 1);
                    visitante.setPuntos(visitante.getPuntos() + 1);
                }

                local.setDiferenciaGol(local.getGolesFavor() - local.getGolesContra());
                visitante.setDiferenciaGol(visitante.getGolesFavor() - visitante.getGolesContra());
            }
        }

        List<TeamStatisticsDTO> tablaPosiciones = new ArrayList<>(mapEquipos.values());
        tablaPosiciones.sort((e1, e2) -> e1.getPuntos() != e2.getPuntos()
                ? Integer.compare(e2.getPuntos(), e1.getPuntos())
                : Integer.compare(e2.getDiferenciaGol(), e1.getDiferenciaGol()));

        Map<Long, PlayerStatisticsDTO> mapJugadores = new HashMap<>();

        for (MatchEventEntity ep : eventoEntities) {
            if (ep.getJugador() == null) continue;
            Player jugador = jugadorMapper.toDomain(ep.getJugador());
            User usuario = jugador.getUsuario();
            if (usuario == null || usuario.getId() == null) continue;

            PlayerStatisticsDTO stats = mapJugadores.computeIfAbsent(usuario.getId(),
                    id -> PlayerStatisticsDTO.builder()
                            .jugadorId(id)
                            .nombreJugador(usuario.getName())
                            .nombreEquipo("Sin equipo")
                            .goles(0).tarjetasAmarillas(0).tarjetasRojas(0)
                            .build());

            if (ep.getEventType() == EventType.GOL) {
                stats.setGoles(stats.getGoles() + 1);
            } else if (ep.getEventType() == EventType.TARJETA_AMARILLA) {
                stats.setTarjetasAmarillas(stats.getTarjetasAmarillas() + 1);
            } else if (ep.getEventType() == EventType.TARJETA_ROJA) {
                stats.setTarjetasRojas(stats.getTarjetasRojas() + 1);
            }
        }

        List<PlayerStatisticsDTO> goleadores = new ArrayList<>(mapJugadores.values());
        goleadores.removeIf(j -> j.getGoles() == 0);
        goleadores.sort((j1, j2) -> Integer.compare(j2.getGoles(), j1.getGoles()));

        List<PlayerStatisticsDTO> sancionados = new ArrayList<>(mapJugadores.values());
        sancionados.removeIf(j -> j.getTarjetasAmarillas() == 0 && j.getTarjetasRojas() == 0);
        sancionados.sort((j1, j2) -> j1.getTarjetasRojas() != j2.getTarjetasRojas()
                ? Integer.compare(j2.getTarjetasRojas(), j1.getTarjetasRojas())
                : Integer.compare(j2.getTarjetasAmarillas(), j1.getTarjetasAmarillas()));

        return TournamentStatisticsDTO.builder()
                .torneoId(tournId)
                .totalPartidosJugados(partidosJugados)
                .totalGolesAnotados(totalGolesAnotados)
                .tablaPosiciones(tablaPosiciones)
                .tablaGoleadores(goleadores)
                .tablaTarjetas(sancionados)
                .build();
    }

    private TeamStatisticsDTO getOrCreateEquipoStats(
            Map<Long, TeamStatisticsDTO> map, Team team) {
        if (team == null || team.getId() == null) {
            return TeamStatisticsDTO.builder().nombreEquipo("Desconocido").build();
        }
        return map.computeIfAbsent(team.getId(), id -> TeamStatisticsDTO.builder()
                .equipoId(id).nombreEquipo(team.getNombre())
                .partidosJugados(0).victorias(0).empates(0).derrotas(0)
                .golesFavor(0).golesContra(0).diferenciaGol(0).puntos(0)
                .build());
    }
}