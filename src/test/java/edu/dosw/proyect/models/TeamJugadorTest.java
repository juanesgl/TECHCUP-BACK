package edu.dosw.proyect.models;

import edu.dosw.proyect.core.models.Player;
import edu.dosw.proyect.core.models.Team;
import edu.dosw.proyect.core.models.TeamPlayer;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TeamJugadorTest {

    @Test
    void equipoJugador_Constructor_ConstruyeCorrectamente() {
        Team team = new Team();
        team.setNombre("Alpha");

        Player jugador = new Player();
        jugador.setNombre("Juan");

        TeamPlayer ej = new TeamPlayer(team, jugador);

        assertEquals("Alpha", ej.getTeam().getNombre());
        assertEquals("Juan", ej.getJugador().getNombre());
        assertTrue(ej.isActivo());
        assertNotNull(ej.getFechaUnion());
    }

    @Test
    void equipoJugador_NoArgsConstructor_CreaVacio() {
        TeamPlayer ej = new TeamPlayer();
        assertNull(ej.getId());
        assertNull(ej.getTeam());
    }

    @Test
    void equipoJugador_AllArgsConstructor_ConstruyeCorrectamente() {
        Team team = new Team();
        Player jugador = new Player();
        LocalDateTime ahora = LocalDateTime.now();

        TeamPlayer ej = new TeamPlayer(1L, team, jugador, ahora, true);

        assertEquals(1L, ej.getId());
        assertTrue(ej.isActivo());
        assertEquals(ahora, ej.getFechaUnion());
    }

    @Test
    void equipoJugador_SetActivo_CambiaEstado() {
        TeamPlayer ej = new TeamPlayer();
        ej.setActivo(false);
        assertFalse(ej.isActivo());
    }
}