package edu.dosw.proyect.models;

import edu.dosw.proyect.core.models.Equipo;
import edu.dosw.proyect.core.models.Invitacion;
import edu.dosw.proyect.core.models.Jugador;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class InvitacionTest {

    @Test
    void invitacion_Builder_ConstruyeCorrectamente() {
        Equipo equipo = new Equipo();
        equipo.setNombre("Alpha FC");

        Jugador jugador = new Jugador();
        jugador.setNombre("Juan");

        LocalDateTime ahora = LocalDateTime.now();

        Invitacion inv = Invitacion.builder()
                .id(1L)
                .equipo(equipo)
                .jugador(jugador)
                .estado("PENDIENTE")
                .fechaEnvio(ahora)
                .fechaRespuesta(null)
                .build();

        assertEquals(1L, inv.getId());
        assertEquals("Alpha FC", inv.getEquipo().getNombre());
        assertEquals("Juan", inv.getJugador().getNombre());
        assertEquals("PENDIENTE", inv.getEstado());
        assertNotNull(inv.getFechaEnvio());
        assertNull(inv.getFechaRespuesta());
    }

    @Test
    void invitacion_NoArgsConstructor_CreaVacio() {
        Invitacion inv = new Invitacion();
        assertNull(inv.getId());
        assertNull(inv.getEstado());
    }

    @Test
    void invitacion_GetJugadorInvitado_RetornaJugador() {
        Jugador jugador = new Jugador();
        jugador.setNombre("Carlos");

        Invitacion inv = Invitacion.builder()
                .jugador(jugador)
                .estado("PENDIENTE")
                .build();

        assertEquals("Carlos", inv.getJugadorInvitado().getNombre());
    }

    @Test
    void invitacion_GetEquipoInvita_RetornaEquipo() {
        Equipo equipo = new Equipo();
        equipo.setNombre("Beta FC");

        Invitacion inv = Invitacion.builder()
                .equipo(equipo)
                .estado("PENDIENTE")
                .build();

        assertEquals("Beta FC", inv.getEquipoInvita().getNombre());
    }

    @Test
    void invitacion_Setters_FuncionanCorrectamente() {
        Invitacion inv = new Invitacion();
        inv.setEstado("ACEPTADA");
        inv.setFechaRespuesta(LocalDateTime.now());

        assertEquals("ACEPTADA", inv.getEstado());
        assertNotNull(inv.getFechaRespuesta());
    }
}