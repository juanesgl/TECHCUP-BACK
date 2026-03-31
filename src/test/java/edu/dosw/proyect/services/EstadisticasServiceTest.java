package edu.dosw.proyect.services;

import edu.dosw.proyect.controllers.dtos.response.EstadisticasTorneoDTO;
import edu.dosw.proyect.core.models.*;
import edu.dosw.proyect.core.models.enums.MatchStatus;
import edu.dosw.proyect.core.models.enums.TipoEvento;
import edu.dosw.proyect.persistence.repository.EventoPartidoRepository;
import edu.dosw.proyect.persistence.repository.PartidoRepository;
import edu.dosw.proyect.core.services.EstadisticasService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EstadisticasServiceTest {

    @Mock
    private PartidoRepository partidoRepository;

    @Mock
    private EventoPartidoRepository eventoPartidoRepository;

    @InjectMocks
    private EstadisticasService estadisticasService;

    private Equipo buildEquipo(Long id, String nombre) {
        Equipo e = new Equipo();
        e.setId(id);
        e.setNombre(nombre);
        return e;
    }

    private Partido buildPartido(Equipo local, Equipo visitante,
                                 int golesLocal, int golesVisitante, MatchStatus estado) {
        Partido p = new Partido();
        p.setEquipoLocal(local);
        p.setEquipoVisitante(visitante);
        p.setGolesLocal(golesLocal);
        p.setGolesVisitante(golesVisitante);
        p.setEstado(estado);
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
        assertEquals("TOURN-1", result.getTorneoId());
        assertEquals(0, result.getTotalPartidosJugados());
        assertEquals(0, result.getTotalGolesAnotados());
        assertTrue(result.getTablaPosiciones().isEmpty());
        assertTrue(result.getTablaGoleadores().isEmpty());
    }

    @Test
    void obtenerEstadisticasTorneo_PartidoFinalizado_LocalGana() {
        Equipo alpha = buildEquipo(1L, "Alpha");
        Equipo beta = buildEquipo(2L, "Beta");
        Partido p = buildPartido(alpha, beta, 3, 1, MatchStatus.FINALIZADO);

        when(partidoRepository.findByTorneo_TournId("TOURN-1"))
                .thenReturn(List.of(p));
        when(eventoPartidoRepository.findByPartido_Torneo_TournId("TOURN-1"))
                .thenReturn(List.of());

        EstadisticasTorneoDTO result =
                estadisticasService.obtenerEstadisticasTorneo("TOURN-1");

        assertEquals(1, result.getTotalPartidosJugados());
        assertEquals(4, result.getTotalGolesAnotados());
        assertEquals(2, result.getTablaPosiciones().size());

        var alphaStats = result.getTablaPosiciones().get(0);
        assertEquals("Alpha", alphaStats.getNombreEquipo());
        assertEquals(3, alphaStats.getPuntos());
        assertEquals(1, alphaStats.getVictorias());
    }

    @Test
    void obtenerEstadisticasTorneo_PartidoFinalizado_Empate() {
        Equipo alpha = buildEquipo(1L, "Alpha");
        Equipo beta = buildEquipo(2L, "Beta");
        Partido p = buildPartido(alpha, beta, 1, 1, MatchStatus.FINALIZADO);

        when(partidoRepository.findByTorneo_TournId("TOURN-1"))
                .thenReturn(List.of(p));
        when(eventoPartidoRepository.findByPartido_Torneo_TournId("TOURN-1"))
                .thenReturn(List.of());

        EstadisticasTorneoDTO result =
                estadisticasService.obtenerEstadisticasTorneo("TOURN-1");

        assertEquals(1, result.getTotalPartidosJugados());
        result.getTablaPosiciones().forEach(e ->
                assertEquals(1, e.getPuntos()));
    }

    @Test
    void obtenerEstadisticasTorneo_PartidoFinalizado_VisitanteGana() {
        Equipo alpha = buildEquipo(1L, "Alpha");
        Equipo beta = buildEquipo(2L, "Beta");
        Partido p = buildPartido(alpha, beta, 0, 2, MatchStatus.FINALIZADO);

        when(partidoRepository.findByTorneo_TournId("TOURN-1"))
                .thenReturn(List.of(p));
        when(eventoPartidoRepository.findByPartido_Torneo_TournId("TOURN-1"))
                .thenReturn(List.of());

        EstadisticasTorneoDTO result =
                estadisticasService.obtenerEstadisticasTorneo("TOURN-1");

        var betaStats = result.getTablaPosiciones().get(0);
        assertEquals("Beta", betaStats.getNombreEquipo());
        assertEquals(3, betaStats.getPuntos());
    }

    @Test
    void obtenerEstadisticasTorneo_PartidoProgramado_NoSeCuenta() {
        Equipo alpha = buildEquipo(1L, "Alpha");
        Equipo beta = buildEquipo(2L, "Beta");
        Partido p = buildPartido(alpha, beta, 2, 0, MatchStatus.PROGRAMADO);

        when(partidoRepository.findByTorneo_TournId("TOURN-1"))
                .thenReturn(List.of(p));
        when(eventoPartidoRepository.findByPartido_Torneo_TournId("TOURN-1"))
                .thenReturn(List.of());

        EstadisticasTorneoDTO result =
                estadisticasService.obtenerEstadisticasTorneo("TOURN-1");

        assertEquals(0, result.getTotalPartidosJugados());
        assertTrue(result.getTablaPosiciones().isEmpty());
    }

    @Test
    void obtenerEstadisticasTorneo_ConEventoGol_ApareceEnGoleadores() {
        Equipo alpha = buildEquipo(1L, "Alpha");
        Equipo beta = buildEquipo(2L, "Beta");
        Partido p = buildPartido(alpha, beta, 1, 0, MatchStatus.FINALIZADO);

        User usuario = User.builder()
                .id(10L).name("Juan").email("j@m.com")
                .password("p").role("PLAYER").build();

        Jugador jugador = new Jugador();
        jugador.setUsuario(usuario);

        EventoPartido evento = new EventoPartido();
        evento.setJugador(jugador);
        evento.setEquipo(alpha);
        evento.setTipoEvento(TipoEvento.GOL);

        when(partidoRepository.findByTorneo_TournId("TOURN-1"))
                .thenReturn(List.of(p));
        when(eventoPartidoRepository.findByPartido_Torneo_TournId("TOURN-1"))
                .thenReturn(List.of(evento));

        EstadisticasTorneoDTO result =
                estadisticasService.obtenerEstadisticasTorneo("TOURN-1");

        assertEquals(1, result.getTablaGoleadores().size());
        assertEquals(1, result.getTablaGoleadores().get(0).getGoles());
    }

    @Test
    void obtenerEstadisticasTorneo_ConEventoTarjeta_ApareceEnSancionados() {
        Equipo alpha = buildEquipo(1L, "Alpha");
        Equipo beta = buildEquipo(2L, "Beta");
        Partido p = buildPartido(alpha, beta, 0, 0, MatchStatus.FINALIZADO);

        User usuario = User.builder()
                .id(10L).name("Juan").email("j@m.com")
                .password("p").role("PLAYER").build();

        Jugador jugador = new Jugador();
        jugador.setUsuario(usuario);

        EventoPartido amarilla = new EventoPartido();
        amarilla.setJugador(jugador);
        amarilla.setEquipo(alpha);
        amarilla.setTipoEvento(TipoEvento.TARJETA_AMARILLA);

        EventoPartido roja = new EventoPartido();
        roja.setJugador(jugador);
        roja.setEquipo(alpha);
        roja.setTipoEvento(TipoEvento.TARJETA_ROJA);

        when(partidoRepository.findByTorneo_TournId("TOURN-1"))
                .thenReturn(List.of(p));
        when(eventoPartidoRepository.findByPartido_Torneo_TournId("TOURN-1"))
                .thenReturn(List.of(amarilla, roja));

        EstadisticasTorneoDTO result =
                estadisticasService.obtenerEstadisticasTorneo("TOURN-1");

        assertEquals(1, result.getTablaTarjetas().size());
        assertEquals(1, result.getTablaTarjetas().get(0).getTarjetasAmarillas());
        assertEquals(1, result.getTablaTarjetas().get(0).getTarjetasRojas());
    }
}