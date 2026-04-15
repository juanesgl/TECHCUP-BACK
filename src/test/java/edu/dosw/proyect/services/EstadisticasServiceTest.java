package edu.dosw.proyect.services;

import edu.dosw.proyect.controllers.dtos.response.TournamentStatisticsDTO;
import edu.dosw.proyect.core.models.Player;
import edu.dosw.proyect.core.models.Team;
import edu.dosw.proyect.core.models.enums.MatchStatus;
import edu.dosw.proyect.core.models.enums.EventType;
import edu.dosw.proyect.core.services.EstadisticasService;
import edu.dosw.proyect.persistence.entity.*;
import edu.dosw.proyect.persistence.mapper.PlayerPersistenceMapper;
import edu.dosw.proyect.persistence.mapper.MatchPersistenceMapper;
import edu.dosw.proyect.persistence.repository.MatchEventRepository;
import edu.dosw.proyect.persistence.repository.MatchRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EstadisticasServiceTest {

    @Mock private MatchRepository matchRepository;
    @Mock private MatchEventRepository matchEventRepository;
    @Mock private MatchPersistenceMapper partidoMapper;
    @Mock private PlayerPersistenceMapper jugadorMapper;

    @InjectMocks
    private EstadisticasService estadisticasService;

    private TeamEntity buildEquipoEntity(Long id, String nombre) {
        TeamEntity e = new TeamEntity();
        e.setId(id);
        e.setNombre(nombre);
        return e;
    }

    private MatchEntity buildPartidoEntity(MatchStatus estado) {
        MatchEntity p = new MatchEntity();
        p.setId(1L);
        p.setEstado(estado);
        p.setGolesLocal(2);
        p.setGolesVisitante(1);
        p.setEquipoLocal(buildEquipoEntity(1L, "Alpha"));
        p.setEquipoVisitante(buildEquipoEntity(2L, "Beta"));
        return p;
    }

    @Test
    void obtenerEstadisticasTorneo_SinPartidos_RetornaVacio() {
        when(matchRepository.findByTorneo_TournId("TOURN-1"))
                .thenReturn(List.of());
        when(matchEventRepository.findByPartido_Torneo_TournId("TOURN-1"))
                .thenReturn(List.of());

        TournamentStatisticsDTO result =
                estadisticasService.obtenerEstadisticasTorneo("TOURN-1");

        assertNotNull(result);
        assertEquals(0, result.getTotalPartidosJugados());
        assertTrue(result.getTablaPosiciones().isEmpty());
    }

    @Test
    void obtenerEstadisticasTorneo_PartidoFinalizado_ContabilizaCorrectamente() {
        MatchEntity entity = buildPartidoEntity(MatchStatus.FINALIZADO);

        Team local =
                new Team();
        local.setId(1L);
        local.setNombre("Alpha");

        Team visitante =
                new Team();
        visitante.setId(2L);
        visitante.setNombre("Beta");

        edu.dosw.proyect.core.models.Partido domain =
                new edu.dosw.proyect.core.models.Partido();
        domain.setId(1L);
        domain.setEstado(MatchStatus.FINALIZADO);
        domain.setGolesLocal(2);
        domain.setGolesVisitante(1);
        domain.setTeamLocal(local);
        domain.setTeamVisitante(visitante);

        when(matchRepository.findByTorneo_TournId("TOURN-1"))
                .thenReturn(List.of(entity));
        when(matchEventRepository.findByPartido_Torneo_TournId("TOURN-1"))
                .thenReturn(List.of());
        when(partidoMapper.toDomain(entity)).thenReturn(domain);

        TournamentStatisticsDTO result =
                estadisticasService.obtenerEstadisticasTorneo("TOURN-1");

        assertEquals(1, result.getTotalPartidosJugados());
        assertEquals(3, result.getTotalGolesAnotados());
    }

    @Test
    void obtenerEstadisticasTorneo_PartidoProgramado_NoSeCuenta() {
        MatchEntity entity = buildPartidoEntity(MatchStatus.PROGRAMADO);

        edu.dosw.proyect.core.models.Partido domain =
                new edu.dosw.proyect.core.models.Partido();
        domain.setEstado(MatchStatus.PROGRAMADO);

        when(matchRepository.findByTorneo_TournId("TOURN-1"))
                .thenReturn(List.of(entity));
        when(matchEventRepository.findByPartido_Torneo_TournId("TOURN-1"))
                .thenReturn(List.of());
        when(partidoMapper.toDomain(entity)).thenReturn(domain);

        TournamentStatisticsDTO result =
                estadisticasService.obtenerEstadisticasTorneo("TOURN-1");

        assertEquals(0, result.getTotalPartidosJugados());
    }

    @Test
    void obtenerEstadisticasTorneo_ConEventoGol_ApareceEnGoleadores() {
        MatchEntity matchEntity = buildPartidoEntity(MatchStatus.FINALIZADO);

        Team local =
                new Team();
        local.setId(1L);
        local.setNombre("Alpha");
        Team visitante =
                new Team();
        visitante.setId(2L);
        visitante.setNombre("Beta");

        edu.dosw.proyect.core.models.Partido domain =
                new edu.dosw.proyect.core.models.Partido();
        domain.setEstado(MatchStatus.FINALIZADO);
        domain.setGolesLocal(1);
        domain.setGolesVisitante(0);
        domain.setTeamLocal(local);
        domain.setTeamVisitante(visitante);

        UserEntity userEntity = UserEntity.builder()
                .id(10L).name("Juan").email("j@mail.com")
                .password("p").role("PLAYER").build();
        PlayerEntity playerEntity = new PlayerEntity();
        playerEntity.setId(10L);
        playerEntity.setUsuario(userEntity);

        edu.dosw.proyect.core.models.User usuario =
                edu.dosw.proyect.core.models.User.builder()
                        .id(10L).name("Juan").email("j@mail.com")
                        .password("p").role("PLAYER").build();
        Player jugadorDomain =
                new Player();
        jugadorDomain.setId(10L);
        jugadorDomain.setUsuario(usuario);

        MatchEventEntity evento = new MatchEventEntity();
        evento.setJugador(playerEntity);
        evento.setEventType(EventType.GOL);

        when(matchRepository.findByTorneo_TournId("TOURN-1"))
                .thenReturn(List.of(matchEntity));
        when(matchEventRepository.findByPartido_Torneo_TournId("TOURN-1"))
                .thenReturn(List.of(evento));
        when(partidoMapper.toDomain(matchEntity)).thenReturn(domain);
        when(jugadorMapper.toDomain(playerEntity)).thenReturn(jugadorDomain);

        TournamentStatisticsDTO result =
                estadisticasService.obtenerEstadisticasTorneo("TOURN-1");

        assertEquals(1, result.getTablaGoleadores().size());
        assertEquals(1, result.getTablaGoleadores().get(0).getGoles());
    }

    @Test
    void obtenerEstadisticasTorneo_PartidoFinalizado_VisitanteGana() {
        MatchEntity entity = buildPartidoEntity(MatchStatus.FINALIZADO);
        entity.setGolesLocal(0);
        entity.setGolesVisitante(2);

        Team local =
                new Team();
        local.setId(1L);
        local.setNombre("Alpha");
        Team visitante =
                new Team();
        visitante.setId(2L);
        visitante.setNombre("Beta");

        edu.dosw.proyect.core.models.Partido domain =
                new edu.dosw.proyect.core.models.Partido();
        domain.setEstado(MatchStatus.FINALIZADO);
        domain.setGolesLocal(0);
        domain.setGolesVisitante(2);
        domain.setTeamLocal(local);
        domain.setTeamVisitante(visitante);

        when(matchRepository.findByTorneo_TournId("TOURN-1"))
                .thenReturn(List.of(entity));
        when(matchEventRepository.findByPartido_Torneo_TournId("TOURN-1"))
                .thenReturn(List.of());
        when(partidoMapper.toDomain(entity)).thenReturn(domain);

        TournamentStatisticsDTO result =
                estadisticasService.obtenerEstadisticasTorneo("TOURN-1");

        assertEquals(1, result.getTotalPartidosJugados());
        var betaStats = result.getTablaPosiciones().get(0);
        assertEquals("Beta", betaStats.getNombreEquipo());
        assertEquals(3, betaStats.getPuntos());
    }

    @Test
    void obtenerEstadisticasTorneo_PartidoFinalizado_Empate() {
        MatchEntity entity = buildPartidoEntity(MatchStatus.FINALIZADO);
        entity.setGolesLocal(1);
        entity.setGolesVisitante(1);

        Team local =
                new Team();
        local.setId(1L);
        local.setNombre("Alpha");
        Team visitante =
                new Team();
        visitante.setId(2L);
        visitante.setNombre("Beta");

        edu.dosw.proyect.core.models.Partido domain =
                new edu.dosw.proyect.core.models.Partido();
        domain.setEstado(MatchStatus.FINALIZADO);
        domain.setGolesLocal(1);
        domain.setGolesVisitante(1);
        domain.setTeamLocal(local);
        domain.setTeamVisitante(visitante);

        when(matchRepository.findByTorneo_TournId("TOURN-1"))
                .thenReturn(List.of(entity));
        when(matchEventRepository.findByPartido_Torneo_TournId("TOURN-1"))
                .thenReturn(List.of());
        when(partidoMapper.toDomain(entity)).thenReturn(domain);

        TournamentStatisticsDTO result =
                estadisticasService.obtenerEstadisticasTorneo("TOURN-1");

        assertEquals(1, result.getTotalPartidosJugados());
        result.getTablaPosiciones().forEach(e ->
                assertEquals(1, e.getPuntos()));
    }
}