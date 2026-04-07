package edu.dosw.proyect.models;

import edu.dosw.proyect.core.models.Alineacion;
import edu.dosw.proyect.core.models.AlineacionJugador;
import edu.dosw.proyect.core.models.Jugador;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AlineacionJugadorTest {

    @Test
    void alineacionJugador_AllArgsConstructor_ConstruyeCorrectamente() {
        Jugador jugador = new Jugador();
        jugador.setNombre("Juan");

        Alineacion alineacion = new Alineacion();

        AlineacionJugador aj = new AlineacionJugador(
                1L, alineacion, jugador, "TITULAR", "Delantero", 10);

        assertEquals(1L, aj.getId());
        assertEquals("TITULAR", aj.getRol());
        assertEquals("Delantero", aj.getPosicionEnCancha());
        assertEquals(10, aj.getNumeroCamiseta());
        assertEquals("Juan", aj.getJugador().getNombre());
    }

    @Test
    void alineacionJugador_NoArgsConstructor_CreaVacio() {
        AlineacionJugador aj = new AlineacionJugador();
        assertNull(aj.getId());
        assertNull(aj.getRol());
        assertEquals(0, aj.getNumeroCamiseta());
    }

    @Test
    void alineacionJugador_Setters_FuncionanCorrectamente() {
        AlineacionJugador aj = new AlineacionJugador();
        aj.setRol("RESERVA");
        aj.setPosicionEnCancha("Portero");
        aj.setNumeroCamiseta(1);

        assertEquals("RESERVA", aj.getRol());
        assertEquals("Portero", aj.getPosicionEnCancha());
        assertEquals(1, aj.getNumeroCamiseta());
    }
}