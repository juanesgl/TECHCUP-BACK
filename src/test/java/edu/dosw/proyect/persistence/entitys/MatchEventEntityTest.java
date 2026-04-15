package edu.dosw.proyect.persistence.entitys;

import edu.dosw.proyect.persistence.entity.*;
import edu.dosw.proyect.core.models.enums.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MatchEventEntityTest {

    @Test
    void eventoPartidoEntity_NoArgsConstructor_CreaVacio() {
        MatchEventEntity ep = new MatchEventEntity();
        assertNull(ep.getId());
        assertNull(ep.getEventType());
    }

    @Test
    void eventoPartidoEntity_Builder_ConstruyeCorrectamente() {
        MatchEntity partido = new MatchEntity();
        partido.setId(1L);
        PlayerEntity jugador = new PlayerEntity();
        jugador.setId(1L);

        MatchEventEntity ep = MatchEventEntity.builder()
                .id(1L)
                .partido(partido)
                .jugador(jugador)
                .eventType(EventType.GOL)
                .minuto(45)
                .descripcion("Gol de cabeza")
                .build();

        assertEquals(1L, ep.getId());
        assertEquals(EventType.GOL, ep.getEventType());
        assertEquals(45, ep.getMinuto());
        assertEquals("Gol de cabeza", ep.getDescripcion());
        assertNotNull(ep.getPartido());
        assertNotNull(ep.getJugador());
    }

    @Test
    void eventoPartidoEntity_AllArgsConstructor_ConstruyeCorrectamente() {
        MatchEntity partido = new MatchEntity();
        PlayerEntity jugador = new PlayerEntity();

        MatchEventEntity ep = new MatchEventEntity(
                1L, partido, jugador,
                EventType.TARJETA_AMARILLA, 30, "Falta");

        assertEquals(EventType.TARJETA_AMARILLA, ep.getEventType());
        assertEquals(30, ep.getMinuto());
    }

    @Test
    void eventoPartidoEntity_Setters_FuncionanCorrectamente() {
        MatchEventEntity ep = new MatchEventEntity();
        ep.setId(1L);
        ep.setEventType(EventType.TARJETA_ROJA);
        ep.setMinuto(90);
        ep.setDescripcion("Expulsado");

        assertEquals(EventType.TARJETA_ROJA, ep.getEventType());
        assertEquals(90, ep.getMinuto());
        assertEquals("Expulsado", ep.getDescripcion());
    }
}
