package edu.dosw.proyect.persistence.entitys;

import edu.dosw.proyect.persistence.entity.*;
import edu.dosw.proyect.core.models.enums.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EventoPartidoEntityTest {

    @Test
    void eventoPartidoEntity_NoArgsConstructor_CreaVacio() {
        EventoPartidoEntity ep = new EventoPartidoEntity();
        assertNull(ep.getId());
        assertNull(ep.getTipoEvento());
    }

    @Test
    void eventoPartidoEntity_Builder_ConstruyeCorrectamente() {
        PartidoEntity partido = new PartidoEntity();
        partido.setId(1L);
        JugadorEntity jugador = new JugadorEntity();
        jugador.setId(1L);

        EventoPartidoEntity ep = EventoPartidoEntity.builder()
                .id(1L)
                .partido(partido)
                .jugador(jugador)
                .tipoEvento(TipoEvento.GOL)
                .minuto(45)
                .descripcion("Gol de cabeza")
                .build();

        assertEquals(1L, ep.getId());
        assertEquals(TipoEvento.GOL, ep.getTipoEvento());
        assertEquals(45, ep.getMinuto());
        assertEquals("Gol de cabeza", ep.getDescripcion());
        assertNotNull(ep.getPartido());
        assertNotNull(ep.getJugador());
    }

    @Test
    void eventoPartidoEntity_AllArgsConstructor_ConstruyeCorrectamente() {
        PartidoEntity partido = new PartidoEntity();
        JugadorEntity jugador = new JugadorEntity();

        EventoPartidoEntity ep = new EventoPartidoEntity(
                1L, partido, jugador,
                TipoEvento.TARJETA_AMARILLA, 30, "Falta");

        assertEquals(TipoEvento.TARJETA_AMARILLA, ep.getTipoEvento());
        assertEquals(30, ep.getMinuto());
    }

    @Test
    void eventoPartidoEntity_Setters_FuncionanCorrectamente() {
        EventoPartidoEntity ep = new EventoPartidoEntity();
        ep.setId(1L);
        ep.setTipoEvento(TipoEvento.TARJETA_ROJA);
        ep.setMinuto(90);
        ep.setDescripcion("Expulsado");

        assertEquals(TipoEvento.TARJETA_ROJA, ep.getTipoEvento());
        assertEquals(90, ep.getMinuto());
        assertEquals("Expulsado", ep.getDescripcion());
    }
}
