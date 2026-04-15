package edu.dosw.proyect.models;

import edu.dosw.proyect.core.models.*;
import edu.dosw.proyect.core.models.enums.EventType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MatchEventTest {

    @Test
    void eventoPartido_Builder_ConstruyeCorrectamente() {
        Partido partido = new Partido();
        partido.setId(1L);

        Player jugador = new Player();
        jugador.setNombre("Juan");

        MatchEvent evento = MatchEvent.builder()
                .id(1L)
                .partido(partido)
                .jugador(jugador)
                .eventType(EventType.GOL)
                .minuto(30)
                .descripcion("Gol de cabeza")
                .build();

        assertEquals(1L, evento.getId());
        assertEquals(1L, evento.getPartido().getId());
        assertEquals("Juan", evento.getJugador().getNombre());
        assertEquals(EventType.GOL, evento.getEventType());
        assertEquals(30, evento.getMinuto());
        assertEquals("Gol de cabeza", evento.getDescripcion());
    }

    @Test
    void eventoPartido_NoArgsConstructor_CreaVacio() {
        MatchEvent evento = new MatchEvent();
        assertNull(evento.getId());
        assertNull(evento.getEventType());
        assertEquals(0, evento.getMinuto());
    }

    @Test
    void eventoPartido_CamposTransient_FuncionanCorrectamente() {
        MatchEvent evento = new MatchEvent();
        Team team = new Team();
        team.setNombre("Alpha");

        evento.setTeam(team);

        assertEquals("Alpha", evento.getTeam().getNombre());
    }

    @Test
    void eventoPartido_Setters_FuncionanCorrectamente() {
        MatchEvent evento = new MatchEvent();
        Player jugador = new Player();
        jugador.setNombre("Carlos");

        evento.setJugador(jugador);
        evento.setEventType(EventType.TARJETA_AMARILLA);
        evento.setMinuto(45);
        evento.setDescripcion("Falta grave");

        assertEquals("Carlos", evento.getJugador().getNombre());
        assertEquals(EventType.TARJETA_AMARILLA, evento.getEventType());
        assertEquals(45, evento.getMinuto());
        assertEquals("Falta grave", evento.getDescripcion());
    }
}
