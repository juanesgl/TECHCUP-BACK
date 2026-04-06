package edu.dosw.proyect.persistence.entitys;

import edu.dosw.proyect.persistence.entity.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EquipoJugadorEntityTest {

    @Test
    void equipoJugadorEntity_NoArgsConstructor_CreaVacio() {
        EquipoJugadorEntity ej = new EquipoJugadorEntity();
        assertNull(ej.getId());
        assertTrue(ej.isActivo());
    }

    @Test
    void equipoJugadorEntity_AllArgsConstructor_ConstruyeCorrectamente() {
        EquipoEntity equipo = new EquipoEntity();
        equipo.setId(1L);
        JugadorEntity jugador = new JugadorEntity();
        jugador.setId(1L);
        LocalDateTime now = LocalDateTime.now();

        EquipoJugadorEntity ej = new EquipoJugadorEntity(
                1L, equipo, jugador, now, true);

        assertEquals(1L, ej.getId());
        assertTrue(ej.isActivo());
        assertNotNull(ej.getFechaUnion());
    }

    @Test
    void equipoJugadorEntity_Setters_FuncionanCorrectamente() {
        EquipoJugadorEntity ej = new EquipoJugadorEntity();
        ej.setId(1L);
        ej.setActivo(true);
        ej.setFechaUnion(LocalDateTime.now());

        assertEquals(1L, ej.getId());
        assertTrue(ej.isActivo());
        assertNotNull(ej.getFechaUnion());

    }
}
