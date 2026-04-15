package edu.dosw.proyect.models;

import edu.dosw.proyect.core.models.Invitation;
import edu.dosw.proyect.core.models.Player;
import edu.dosw.proyect.core.models.Team;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class InvitationTest {

    @Test
    void invitacion_Builder_ConstruyeCorrectamente() {
        Team team = new Team();
        team.setNombre("Alpha FC");

        Player jugador = new Player();
        jugador.setNombre("Juan");

        LocalDateTime ahora = LocalDateTime.now();

        Invitation inv = Invitation.builder()
                .id(1L)
                .team(team)
                .jugador(jugador)
                .estado("PENDIENTE")
                .fechaEnvio(ahora)
                .fechaRespuesta(null)
                .build();

        assertEquals(1L, inv.getId());
        assertEquals("Alpha FC", inv.getTeam().getNombre());
        assertEquals("Juan", inv.getJugador().getNombre());
        assertEquals("PENDIENTE", inv.getEstado());
        assertNotNull(inv.getFechaEnvio());
        assertNull(inv.getFechaRespuesta());
    }

    @Test
    void invitacion_NoArgsConstructor_CreaVacio() {
        Invitation inv = new Invitation();
        assertNull(inv.getId());
        assertNull(inv.getEstado());
    }

    @Test
    void invitacion_GetJugadorInvitado_RetornaJugador() {
        Player jugador = new Player();
        jugador.setNombre("Carlos");

        Invitation inv = Invitation.builder()
                .jugador(jugador)
                .estado("PENDIENTE")
                .build();

        assertEquals("Carlos", inv.getJugadorInvitado().getNombre());
    }

    @Test
    void invitacion_GetEquipoInvita_RetornaEquipo() {
        Team team = new Team();
        team.setNombre("Beta FC");

        Invitation inv = Invitation.builder()
                .team(team)
                .estado("PENDIENTE")
                .build();

        assertEquals("Beta FC", inv.getEquipoInvita().getNombre());
    }

    @Test
    void invitacion_Setters_FuncionanCorrectamente() {
        Invitation inv = new Invitation();
        inv.setEstado("ACEPTADA");
        inv.setFechaRespuesta(LocalDateTime.now());

        assertEquals("ACEPTADA", inv.getEstado());
        assertNotNull(inv.getFechaRespuesta());
    }
}