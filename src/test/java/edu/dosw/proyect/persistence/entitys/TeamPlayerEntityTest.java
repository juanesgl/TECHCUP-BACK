package edu.dosw.proyect.persistence.entitys;

import edu.dosw.proyect.persistence.entity.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TeamPlayerEntityTest {

    @Test
    void equipoJugadorEntity_NoArgsConstructor_CreaVacio() {
        TeamPlayerEntity ej = new TeamPlayerEntity();
        assertNull(ej.getId());
        assertTrue(ej.isActivo());
    }

    @Test
    void equipoJugadorEntity_AllArgsConstructor_ConstruyeCorrectamente() {
        TeamEntity equipo = new TeamEntity();
        equipo.setId(1L);
        PlayerEntity jugador = new PlayerEntity();
        jugador.setId(1L);
        LocalDateTime now = LocalDateTime.now();

        TeamPlayerEntity ej = new TeamPlayerEntity(
                1L, equipo, jugador, now, true);

        assertEquals(1L, ej.getId());
        assertTrue(ej.isActivo());
        assertNotNull(ej.getFechaUnion());
    }

    @Test
    void equipoJugadorEntity_Setters_FuncionanCorrectamente() {
        TeamPlayerEntity ej = new TeamPlayerEntity();
        ej.setId(1L);
        ej.setActivo(true);
        ej.setFechaUnion(LocalDateTime.now());

        assertEquals(1L, ej.getId());
        assertTrue(ej.isActivo());
        assertNotNull(ej.getFechaUnion());

    }
}
