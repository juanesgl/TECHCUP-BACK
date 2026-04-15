package edu.dosw.proyect.persistence.entitys;


import edu.dosw.proyect.persistence.entity.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LineupPlayerEntityTest {

    @Test
    void alineacionJugadorEntity_NoArgsConstructor_CreaVacio() {
        LineupPlayerEntity aj = new LineupPlayerEntity();
        assertNull(aj.getId());
        assertNull(aj.getRol());
    }

    @Test
    void alineacionJugadorEntity_AllArgsConstructor_ConstruyeCorrectamente() {
        LineupEntity alineacion = new LineupEntity();
        alineacion.setId(1L);
        PlayerEntity jugador = new PlayerEntity();
        jugador.setId(1L);

        LineupPlayerEntity aj = new LineupPlayerEntity(
                1L, alineacion, jugador, "TITULAR", "Delantero", 9);

        assertEquals(1L, aj.getId());
        assertEquals("TITULAR", aj.getRol());
        assertEquals("Delantero", aj.getPosicionEnCancha());
        assertEquals(9, aj.getNumeroCamiseta());
    }

    @Test
    void alineacionJugadorEntity_Setters_FuncionanCorrectamente() {
        LineupPlayerEntity aj = new LineupPlayerEntity();
        aj.setId(1L);
        aj.setRol("RESERVA");
        aj.setPosicionEnCancha("Mediocampista");
        aj.setNumeroCamiseta(10);

        assertEquals("RESERVA", aj.getRol());
        assertEquals("Mediocampista", aj.getPosicionEnCancha());
        assertEquals(10, aj.getNumeroCamiseta());
    }
}
