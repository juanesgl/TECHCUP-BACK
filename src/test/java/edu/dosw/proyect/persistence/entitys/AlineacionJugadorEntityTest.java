package edu.dosw.proyect.persistence.entitys;


import edu.dosw.proyect.persistence.entity.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AlineacionJugadorEntityTest {

    @Test
    void alineacionJugadorEntity_NoArgsConstructor_CreaVacio() {
        AlineacionJugadorEntity aj = new AlineacionJugadorEntity();
        assertNull(aj.getId());
        assertNull(aj.getRol());
    }

    @Test
    void alineacionJugadorEntity_AllArgsConstructor_ConstruyeCorrectamente() {
        AlineacionEntity alineacion = new AlineacionEntity();
        alineacion.setId(1L);
        JugadorEntity jugador = new JugadorEntity();
        jugador.setId(1L);

        AlineacionJugadorEntity aj = new AlineacionJugadorEntity(
                1L, alineacion, jugador, "TITULAR", "Delantero", 9);

        assertEquals(1L, aj.getId());
        assertEquals("TITULAR", aj.getRol());
        assertEquals("Delantero", aj.getPosicionEnCancha());
        assertEquals(9, aj.getNumeroCamiseta());
    }

    @Test
    void alineacionJugadorEntity_Setters_FuncionanCorrectamente() {
        AlineacionJugadorEntity aj = new AlineacionJugadorEntity();
        aj.setId(1L);
        aj.setRol("RESERVA");
        aj.setPosicionEnCancha("Mediocampista");
        aj.setNumeroCamiseta(10);

        assertEquals("RESERVA", aj.getRol());
        assertEquals("Mediocampista", aj.getPosicionEnCancha());
        assertEquals(10, aj.getNumeroCamiseta());
    }
}
