package edu.dosw.proyect.services;

import edu.dosw.proyect.controllers.dtos.response.EstadisticasTorneoDTO;
import edu.dosw.proyect.core.models.enums.MatchStatus;
import edu.dosw.proyect.core.models.enums.TipoEvento;
import edu.dosw.proyect.core.services.EstadisticasService;
import edu.dosw.proyect.persistence.entity.*;
import edu.dosw.proyect.persistence.mapper.JugadorPersistenceMapper;
import edu.dosw.proyect.persistence.mapper.PartidoPersistenceMapper;
import edu.dosw.proyect.persistence.repository.EventoPartidoRepository;
import edu.dosw.proyect.persistence.repository.PartidoRepository;
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

    @Mock private PartidoRepository partidoRepository;
    @Mock private EventoPartidoRepository eventoPartidoRepository;
    @Mock private PartidoPersistenceMapper partidoMapper;
    @Mock private JugadorPersistenceMapper jugadorMapper;

    @InjectMocks
    private EstadisticasService estadisticasService;

    private EquipoEntity buildEquipoEntity(Long id, String nombre) {
        EquipoEntity e = new EquipoEntity();
        e.setId(id);
        e.setNombre(nombre);
        return e;
    }

    private PartidoEntity buildPartidoEntity(MatchStatus estado) {
        PartidoEntity p = new PartidoEntity();
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
        when(partidoRepository.findByTorneo_TournId("TOURN-1"))
                .thenReturn(List.of());
        when(eventoPartidoRepository.findByPartido_Torneo_TournId("TOURN-1"))
                .thenReturn(List.of());

        EstadisticasTorneoDTO result =
                estadisticasService.obtenerEstadisticasTorneo("TOURN-1");

        assertNotNull(result);
        assertEquals(0, result.getTotalPartidosJugados());
        assertTrue(result.getTablaPosiciones().isEmpty());
    }

    @Test
    void obtenerEstadisticasTorneo_PartidoFinalizado_ContabilizaCorrectamente() {
        PartidoEntity entity = buildPartidoEntity(MatchStatus.FINALIZADO);

        edu.dosw.proyect.core.models.Equipo local =
                new edu.dosw.proyect.core.models.Equipo();
        local.setId(1L);
        local.setNombre("Alpha");

        edu.dosw.proyect.core.models.Equipo visitante =
                new edu.dosw.proyect.core.models.Equipo();
        visitante.setId(2L);
        visitante.setNombre("Beta");

        edu.dosw.proyect.core.models.Partido domain =
                new edu.dosw.proyect.core.models.Partido();
        domain.setId(1L);
        domain.setEstado(MatchStatus.FINALIZADO);
        domain.setGolesLocal(2);
        domain.setGolesVisitante(1);
        domain.setEquipoLocal(local);
        domain.setEquipoVisitante(visitante);

        when(partidoRepository.findByTorneo_TournId("TOURN-1"))
                .thenReturn(List.of(entity));
        when(eventoPartidoRepository.findByPartido_Torneo_TournId("TOURN-1"))
                .thenReturn(List.of());
        when(partidoMapper.toDomain(entity)).thenReturn(domain);

        EstadisticasTorneoDTO result =
                estadisticasService.obtenerEstadisticasTorneo("TOURN-1");

        assertEquals(1, result.getTotalPartidosJugados());
        assertEquals(3, result.getTotalGolesAnotados());
    }

    @Test
    void obtenerEstadisticasTorneo_PartidoProgramado_NoSeCuenta() {
        PartidoEntity entity = buildPartidoEntity(MatchStatus.PROGRAMADO);

        edu.dosw.proyect.core.models.Partido domain =
                new edu.dosw.proyect.core.models.Partido();
        domain.setEstado(MatchStatus.PROGRAMADO);

        when(partidoRepository.findByTorneo_TournId("TOURN-1"))
                .thenReturn(List.of(entity));
        when(eventoPartidoRepository.findByPartido_Torneo_TournId("TOURN-1"))
                .thenReturn(List.of());
        when(partidoMapper.toDomain(entity)).thenReturn(domain);

        EstadisticasTorneoDTO result =
                estadisticasService.obtenerEstadisticasTorneo("TOURN-1");

        assertEquals(0, result.getTotalPartidosJugados());
    }

    @Test
    void obtenerEstadisticasTorneo_ConEventoGol_ApareceEnGoleadores() {
        PartidoEntity partidoEntity = buildPartidoEntity(MatchStatus.FINALIZADO);

        edu.dosw.proyect.core.models.Equipo local =
                new edu.dosw.proyect.core.models.Equipo();
        local.setId(1L);
        local.setNombre("Alpha");
        edu.dosw.proyect.core.models.Equipo visitante =
                new edu.dosw.proyect.core.models.Equipo();
        visitante.setId(2L);
        visitante.setNombre("Beta");

        edu.dosw.proyect.core.models.Partido domain =
                new edu.dosw.proyect.core.models.Partido();
        domain.setEstado(MatchStatus.FINALIZADO);
        domain.setGolesLocal(1);
        domain.setGolesVisitante(0);
        domain.setEquipoLocal(local);
        domain.setEquipoVisitante(visitante);

        UserEntity userEntity = UserEntity.builder()
                .id(10L).name("Juan").email("j@mail.com")
                .password("p").role("PLAYER").build();
        JugadorEntity jugadorEntity = new JugadorEntity();
        jugadorEntity.setId(10L);
        jugadorEntity.setUsuario(userEntity);

        edu.dosw.proyect.core.models.User usuario =
                edu.dosw.proyect.core.models.User.builder()
                        .id(10L).name("Juan").email("j@mail.com")
                        .password("p").role("PLAYER").build();
        edu.dosw.proyect.core.models.Jugador jugadorDomain =
                new edu.dosw.proyect.core.models.Jugador();
        jugadorDomain.setId(10L);
        jugadorDomain.setUsuario(usuario);

        EventoPartidoEntity evento = new EventoPartidoEntity();
        evento.setJugador(jugadorEntity);
        evento.setTipoEvento(TipoEvento.GOL);

        when(partidoRepository.findByTorneo_TournId("TOURN-1"))
                .thenReturn(List.of(partidoEntity));
        when(eventoPartidoRepository.findByPartido_Torneo_TournId("TOURN-1"))
                .thenReturn(List.of(evento));
        when(partidoMapper.toDomain(partidoEntity)).thenReturn(domain);
        when(jugadorMapper.toDomain(jugadorEntity)).thenReturn(jugadorDomain);

        EstadisticasTorneoDTO result =
                estadisticasService.obtenerEstadisticasTorneo("TOURN-1");

        assertEquals(1, result.getTablaGoleadores().size());
        assertEquals(1, result.getTablaGoleadores().get(0).getGoles());
    }

    @Test
    void obtenerEstadisticasTorneo_PartidoFinalizado_VisitanteGana() {
        PartidoEntity entity = buildPartidoEntity(MatchStatus.FINALIZADO);
        entity.setGolesLocal(0);
        entity.setGolesVisitante(2);

        edu.dosw.proyect.core.models.Equipo local =
                new edu.dosw.proyect.core.models.Equipo();
        local.setId(1L);
        local.setNombre("Alpha");
        edu.dosw.proyect.core.models.Equipo visitante =
                new edu.dosw.proyect.core.models.Equipo();
        visitante.setId(2L);
        visitante.setNombre("Beta");

        edu.dosw.proyect.core.models.Partido domain =
                new edu.dosw.proyect.core.models.Partido();
        domain.setEstado(MatchStatus.FINALIZADO);
        domain.setGolesLocal(0);
        domain.setGolesVisitante(2);
        domain.setEquipoLocal(local);
        domain.setEquipoVisitante(visitante);

        when(partidoRepository.findByTorneo_TournId("TOURN-1"))
                .thenReturn(List.of(entity));
        when(eventoPartidoRepository.findByPartido_Torneo_TournId("TOURN-1"))
                .thenReturn(List.of());
        when(partidoMapper.toDomain(entity)).thenReturn(domain);

        EstadisticasTorneoDTO result =
                estadisticasService.obtenerEstadisticasTorneo("TOURN-1");

        assertEquals(1, result.getTotalPartidosJugados());
        var betaStats = result.getTablaPosiciones().get(0);
        assertEquals("Beta", betaStats.getNombreEquipo());
        assertEquals(3, betaStats.getPuntos());
    }

    @Test
    void obtenerEstadisticasTorneo_PartidoFinalizado_Empate() {
        PartidoEntity entity = buildPartidoEntity(MatchStatus.FINALIZADO);
        entity.setGolesLocal(1);
        entity.setGolesVisitante(1);

        edu.dosw.proyect.core.models.Equipo local =
                new edu.dosw.proyect.core.models.Equipo();
        local.setId(1L);
        local.setNombre("Alpha");
        edu.dosw.proyect.core.models.Equipo visitante =
                new edu.dosw.proyect.core.models.Equipo();
        visitante.setId(2L);
        visitante.setNombre("Beta");

        edu.dosw.proyect.core.models.Partido domain =
                new edu.dosw.proyect.core.models.Partido();
        domain.setEstado(MatchStatus.FINALIZADO);
        domain.setGolesLocal(1);
        domain.setGolesVisitante(1);
        domain.setEquipoLocal(local);
        domain.setEquipoVisitante(visitante);

        when(partidoRepository.findByTorneo_TournId("TOURN-1"))
                .thenReturn(List.of(entity));
        when(eventoPartidoRepository.findByPartido_Torneo_TournId("TOURN-1"))
                .thenReturn(List.of());
        when(partidoMapper.toDomain(entity)).thenReturn(domain);

        EstadisticasTorneoDTO result =
                estadisticasService.obtenerEstadisticasTorneo("TOURN-1");

        assertEquals(1, result.getTotalPartidosJugados());
        result.getTablaPosiciones().forEach(e ->
                assertEquals(1, e.getPuntos()));
    }
}