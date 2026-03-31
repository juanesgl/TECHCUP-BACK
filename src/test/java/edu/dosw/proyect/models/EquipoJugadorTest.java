package edu.dosw.proyect.models;

import edu.dosw.proyect.core.models.Equipo;
import edu.dosw.proyect.core.models.EquipoJugador;
import edu.dosw.proyect.core.models.Jugador;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EquipoJugadorTest {

    @Test
    void equipoJugador_Constructor_ConstruyeCorrectamente() {
        Equipo equipo = new Equipo();
        equipo.setNombre("Alpha");

        Jugador jugador = new Jugador();
        jugador.setNombre("Juan");

        EquipoJugador ej = new EquipoJugador(equipo, jugador);

        assertEquals("Alpha", ej.getEquipo().getNombre());
        assertEquals("Juan", ej.getJugador().getNombre());
        assertTrue(ej.isActivo());
        assertNotNull(ej.getFechaUnion());
    }

    @Test
    void equipoJugador_NoArgsConstructor_CreaVacio() {
        EquipoJugador ej = new EquipoJugador();
        assertNull(ej.getId());
        assertNull(ej.getEquipo());
    }

    @Test
    void equipoJugador_AllArgsConstructor_ConstruyeCorrectamente() {
        Equipo equipo = new Equipo();
        Jugador jugador = new Jugador();
        LocalDateTime ahora = LocalDateTime.now();

        EquipoJugador ej = new EquipoJugador(1L, equipo, jugador, ahora, true);

        assertEquals(1L, ej.getId());
        assertTrue(ej.isActivo());
        assertEquals(ahora, ej.getFechaUnion());
    }

    @Test
    void equipoJugador_SetActivo_CambiaEstado() {
        EquipoJugador ej = new EquipoJugador();
        ej.setActivo(false);
        assertFalse(ej.isActivo());
    }
}