package edu.dosw.proyect.core.services;

import edu.dosw.proyect.controllers.dtos.response.*;
import edu.dosw.proyect.core.models.Equipo;
import edu.dosw.proyect.core.models.Jugador;
import edu.dosw.proyect.core.models.Partido;
import edu.dosw.proyect.core.models.User;
import edu.dosw.proyect.core.models.enums.MatchStatus;
import edu.dosw.proyect.core.models.enums.TipoEvento;
import edu.dosw.proyect.persistence.entity.EventoPartidoEntity;
import edu.dosw.proyect.persistence.entity.PartidoEntity;
import edu.dosw.proyect.persistence.mapper.JugadorPersistenceMapper;
import edu.dosw.proyect.persistence.mapper.PartidoPersistenceMapper;
import edu.dosw.proyect.persistence.repository.EventoPartidoRepository;
import edu.dosw.proyect.persistence.repository.PartidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class EstadisticasService {

    private final PartidoRepository partidoRepository;
    private final EventoPartidoRepository eventoPartidoRepository;
    private final PartidoPersistenceMapper partidoMapper;
    private final JugadorPersistenceMapper jugadorMapper;

    public EstadisticasTorneoDTO obtenerEstadisticasTorneo(String tournId) {
        List<PartidoEntity> entities = partidoRepository.findByTorneo_TournId(tournId);
        List<Partido> partidos = entities.stream()
                .map(partidoMapper::toDomain).toList();

        List<EventoPartidoEntity> eventoEntities =
                eventoPartidoRepository.findByPartido_Torneo_TournId(tournId);

        Map<Long, EstadisticasEquipoDTO> mapEquipos = new HashMap<>();
        int totalGolesAnotados = 0;
        int partidosJugados = 0;

        for (Partido p : partidos) {
            if (p.getEstado() == MatchStatus.FINALIZADO || p.getEstado() == MatchStatus.EN_JUEGO) {
                partidosJugados++;
                totalGolesAnotados += (p.getGolesLocal() + p.getGolesVisitante());

                EstadisticasEquipoDTO local = getOrCreateEquipoStats(mapEquipos, p.getEquipoLocal());
                EstadisticasEquipoDTO visitante = getOrCreateEquipoStats(mapEquipos, p.getEquipoVisitante());

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

        List<EstadisticasEquipoDTO> tablaPosiciones = new ArrayList<>(mapEquipos.values());
        tablaPosiciones.sort((e1, e2) -> e1.getPuntos() != e2.getPuntos()
                ? Integer.compare(e2.getPuntos(), e1.getPuntos())
                : Integer.compare(e2.getDiferenciaGol(), e1.getDiferenciaGol()));

        Map<Long, EstadisticasJugadorDTO> mapJugadores = new HashMap<>();

        for (EventoPartidoEntity ep : eventoEntities) {
            if (ep.getJugador() == null) continue;
            Jugador jugador = jugadorMapper.toDomain(ep.getJugador());
            User usuario = jugador.getUsuario();
            if (usuario == null || usuario.getId() == null) continue;

            EstadisticasJugadorDTO stats = mapJugadores.computeIfAbsent(usuario.getId(),
                    id -> EstadisticasJugadorDTO.builder()
                            .jugadorId(id)
                            .nombreJugador(usuario.getName())
                            .nombreEquipo("Sin equipo")
                            .goles(0).tarjetasAmarillas(0).tarjetasRojas(0)
                            .build());

            if (ep.getTipoEvento() == TipoEvento.GOL) {
                stats.setGoles(stats.getGoles() + 1);
            } else if (ep.getTipoEvento() == TipoEvento.TARJETA_AMARILLA) {
                stats.setTarjetasAmarillas(stats.getTarjetasAmarillas() + 1);
            } else if (ep.getTipoEvento() == TipoEvento.TARJETA_ROJA) {
                stats.setTarjetasRojas(stats.getTarjetasRojas() + 1);
            }
        }

        List<EstadisticasJugadorDTO> goleadores = new ArrayList<>(mapJugadores.values());
        goleadores.removeIf(j -> j.getGoles() == 0);
        goleadores.sort((j1, j2) -> Integer.compare(j2.getGoles(), j1.getGoles()));

        List<EstadisticasJugadorDTO> sancionados = new ArrayList<>(mapJugadores.values());
        sancionados.removeIf(j -> j.getTarjetasAmarillas() == 0 && j.getTarjetasRojas() == 0);
        sancionados.sort((j1, j2) -> j1.getTarjetasRojas() != j2.getTarjetasRojas()
                ? Integer.compare(j2.getTarjetasRojas(), j1.getTarjetasRojas())
                : Integer.compare(j2.getTarjetasAmarillas(), j1.getTarjetasAmarillas()));

        return EstadisticasTorneoDTO.builder()
                .torneoId(tournId)
                .totalPartidosJugados(partidosJugados)
                .totalGolesAnotados(totalGolesAnotados)
                .tablaPosiciones(tablaPosiciones)
                .tablaGoleadores(goleadores)
                .tablaTarjetas(sancionados)
                .build();
    }

    private EstadisticasEquipoDTO getOrCreateEquipoStats(
            Map<Long, EstadisticasEquipoDTO> map, Equipo equipo) {
        if (equipo == null || equipo.getId() == null) {
            return EstadisticasEquipoDTO.builder().nombreEquipo("Desconocido").build();
        }
        return map.computeIfAbsent(equipo.getId(), id -> EstadisticasEquipoDTO.builder()
                .equipoId(id).nombreEquipo(equipo.getNombre())
                .partidosJugados(0).victorias(0).empates(0).derrotas(0)
                .golesFavor(0).golesContra(0).diferenciaGol(0).puntos(0)
                .build());
    }
}