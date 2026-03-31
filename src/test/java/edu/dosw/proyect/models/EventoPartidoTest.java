package edu.dosw.proyect.models;

import edu.dosw.proyect.core.models.*;
import edu.dosw.proyect.core.models.enums.TipoEvento;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EventoPartidoTest {

    @Test
    void eventoPartido_Builder_ConstruyeCorrectamente() {
        Partido partido = new Partido();
        partido.setId(1L);

        Jugador jugador = new Jugador();
        jugador.setNombre("Juan");

        EventoPartido evento = EventoPartido.builder()
                .id(1L)
                .partido(partido)
                .jugador(jugador)
                .tipoEvento(TipoEvento.GOL)
                .minuto(30)
                .descripcion("Gol de cabeza")
                .build();

        assertEquals(1L, evento.getId());
        assertEquals(1L, evento.getPartido().getId());
        assertEquals("Juan", evento.getJugador().getNombre());
        assertEquals(TipoEvento.GOL, evento.getTipoEvento());
        assertEquals(30, evento.getMinuto());
        assertEquals("Gol de cabeza", evento.getDescripcion());
    }

    @Test
    void eventoPartido_NoArgsConstructor_CreaVacio() {
        EventoPartido evento = new EventoPartido();
        assertNull(evento.getId());
        assertNull(evento.getTipoEvento());
        assertEquals(0, evento.getMinuto());
    }

    @Test
    void eventoPartido_CamposTransient_FuncionanCorrectamente() {
        EventoPartido evento = new EventoPartido();
        Equipo equipo = new Equipo();
        equipo.setNombre("Alpha");

        evento.setEquipo(equipo);

        assertEquals("Alpha", evento.getEquipo().getNombre());
    }

    @Test
    void eventoPartido_Setters_FuncionanCorrectamente() {
        EventoPartido evento = new EventoPartido();
        Jugador jugador = new Jugador();
        jugador.setNombre("Carlos");

        evento.setJugador(jugador);
        evento.setTipoEvento(TipoEvento.TARJETA_AMARILLA);
        evento.setMinuto(45);
        evento.setDescripcion("Falta grave");

        assertEquals("Carlos", evento.getJugador().getNombre());
        assertEquals(TipoEvento.TARJETA_AMARILLA, evento.getTipoEvento());
        assertEquals(45, evento.getMinuto());
        assertEquals("Falta grave", evento.getDescripcion());
    }
}
