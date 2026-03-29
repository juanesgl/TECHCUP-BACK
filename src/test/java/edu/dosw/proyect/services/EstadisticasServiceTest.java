package edu.dosw.proyect.services;

import edu.dosw.proyect.controllers.dtos.response.EstadisticasEquipoDTO;
import edu.dosw.proyect.controllers.dtos.response.EstadisticasJugadorDTO;
import edu.dosw.proyect.controllers.dtos.response.EstadisticasTorneoDTO;
import edu.dosw.proyect.core.models.Equipo;
import edu.dosw.proyect.core.models.EventoPartido;
import edu.dosw.proyect.core.models.Jugador;
import edu.dosw.proyect.core.models.Partido;
import edu.dosw.proyect.core.models.Tournament;
import edu.dosw.proyect.core.models.User;
import edu.dosw.proyect.core.models.enums.MatchStatus;
import edu.dosw.proyect.core.models.enums.TipoEvento;
import edu.dosw.proyect.core.repositories.EventoPartidoRepository;
import edu.dosw.proyect.core.repositories.PartidoRepository;
import edu.dosw.proyect.core.services.EstadisticasService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
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

    private Tournament torneo;
    private Equipo equipoLocal;
    private Equipo equipoVisitante;
    private Partido partido;
    private Jugador mockJugador1;
    private Jugador mockJugador2;

    @BeforeEach
    void setUp() {
        torneo = new Tournament();
        torneo.setTournId("TORNEO-123");

        equipoLocal = Equipo.builder().id(1L).nombre("Ingenieros FC").build();
        equipoVisitante = Equipo.builder().id(2L).nombre("Ciber Sec").build();

        partido = Partido.builder()
                .id(1L)
                .torneo(torneo)
                .equipoLocal(equipoLocal)
                .equipoVisitante(equipoVisitante)
                .golesLocal(2)
                .golesVisitante(1)
                .estado(MatchStatus.FINALIZADO)
                .fechaHora(LocalDateTime.now())
                .build();

        mockJugador1 = mock(Jugador.class);
        lenient().when(mockJugador1.getId()).thenReturn(10L);
        User u1 = mock(User.class);
        lenient().when(u1.getName()).thenReturn("Carlos");
        lenient().when(u1.getId()).thenReturn(10L);
        lenient().when(mockJugador1.getUsuario()).thenReturn(u1);

        mockJugador2 = mock(Jugador.class);
        lenient().when(mockJugador2.getId()).thenReturn(20L);
        User u2 = mock(User.class);
        lenient().when(u2.getName()).thenReturn("Andres");
        lenient().when(u2.getId()).thenReturn(20L);
        lenient().when(mockJugador2.getUsuario()).thenReturn(u2);
    }

    @Test
    void testObtenerEstadisticas_ConTorneoVacio() {
        when(partidoRepository.findByTorneo_TournId("TORNEO-123")).thenReturn(Collections.emptyList());
        when(eventoPartidoRepository.findByPartido_Torneo_TournId("TORNEO-123")).thenReturn(Collections.emptyList());

        EstadisticasTorneoDTO resultado = estadisticasService.obtenerEstadisticasTorneo("TORNEO-123");

        assertNotNull(resultado);
        assertEquals(0, resultado.getTotalPartidosJugados());
        assertEquals(0, resultado.getTotalGolesAnotados());
        assertTrue(resultado.getTablaPosiciones().isEmpty());
    }

    @Test
    void testObtenerEstadisticas_CalculaPuntosGoleadoresYTarjetas() {
        when(partidoRepository.findByTorneo_TournId("TORNEO-123")).thenReturn(List.of(partido));

        EventoPartido gol1 = EventoPartido.builder().id(1L).partido(partido).jugador(mockJugador1).equipo(equipoLocal).tipoEvento(TipoEvento.GOL).build();
        EventoPartido gol2 = EventoPartido.builder().id(2L).partido(partido).jugador(mockJugador1).equipo(equipoLocal).tipoEvento(TipoEvento.GOL).build();
        EventoPartido gol3 = EventoPartido.builder().id(3L).partido(partido).jugador(mockJugador2).equipo(equipoVisitante).tipoEvento(TipoEvento.GOL).build();
        EventoPartido tarjeta = EventoPartido.builder().id(4L).partido(partido).jugador(mockJugador2).equipo(equipoVisitante).tipoEvento(TipoEvento.TARJETA_AMARILLA).build();

        when(eventoPartidoRepository.findByPartido_Torneo_TournId("TORNEO-123")).thenReturn(Arrays.asList(gol1, gol2, gol3, tarjeta));

        EstadisticasTorneoDTO resultado = estadisticasService.obtenerEstadisticasTorneo("TORNEO-123");

        assertEquals(1, resultado.getTotalPartidosJugados());
        assertEquals(3, resultado.getTotalGolesAnotados());

        List<EstadisticasEquipoDTO> posiciones = resultado.getTablaPosiciones();
        assertEquals(2, posiciones.size());
        
        EstadisticasEquipoDTO primero = posiciones.get(0);
        assertEquals(1L, primero.getEquipoId());
        assertEquals(3, primero.getPuntos());
        assertEquals(1, primero.getVictorias());
        assertEquals(2, primero.getGolesFavor());
        assertEquals(1, primero.getDiferenciaGol());

        EstadisticasEquipoDTO segundo = posiciones.get(1);
        assertEquals(2L, segundo.getEquipoId());
        assertEquals(0, segundo.getPuntos());
        assertEquals(1, segundo.getDerrotas());
        assertEquals(-1, segundo.getDiferenciaGol());

        List<EstadisticasJugadorDTO> goleadores = resultado.getTablaGoleadores();
        assertEquals(2, goleadores.size());
        assertEquals(10L, goleadores.get(0).getJugadorId());
        assertEquals(2, goleadores.get(0).getGoles());

        List<EstadisticasJugadorDTO> sancionados = resultado.getTablaTarjetas();
        assertEquals(1, sancionados.size());
        assertEquals(20L, sancionados.get(0).getJugadorId());
        assertEquals(1, sancionados.get(0).getTarjetasAmarillas());
    }

    @Test
    void testObtenerEstadisticas_EmpateYOrdenamientoPorDiferenciaGoles() {
        partido.setGolesLocal(1);
        partido.setGolesVisitante(1);

        Partido partido2 = Partido.builder()
                .id(2L)
                .torneo(torneo)
                .equipoLocal(equipoLocal)
                .equipoVisitante(Equipo.builder().id(3L).nombre("Extra FC").build())
                .golesLocal(3)
                .golesVisitante(0)
                .estado(MatchStatus.FINALIZADO)
                .build();

        when(partidoRepository.findByTorneo_TournId("TORNEO-123")).thenReturn(Arrays.asList(partido, partido2));
        when(eventoPartidoRepository.findByPartido_Torneo_TournId("TORNEO-123")).thenReturn(Collections.emptyList());

        EstadisticasTorneoDTO resultado = estadisticasService.obtenerEstadisticasTorneo("TORNEO-123");

        assertEquals(2, resultado.getTotalPartidosJugados());

        List<EstadisticasEquipoDTO> posiciones = resultado.getTablaPosiciones();
        assertEquals(3, posiciones.size());

        assertEquals("Ingenieros FC", posiciones.get(0).getNombreEquipo());
        assertEquals(4, posiciones.get(0).getPuntos());
        assertEquals(3, posiciones.get(0).getDiferenciaGol());

        assertEquals("Ciber Sec", posiciones.get(1).getNombreEquipo());
        assertEquals(1, posiciones.get(1).getPuntos());

        assertEquals("Extra FC", posiciones.get(2).getNombreEquipo());
        assertEquals(0, posiciones.get(2).getPuntos());
    }
}

