package edu.dosw.proyect.models;

import edu.dosw.proyect.core.models.Lineup;
import edu.dosw.proyect.core.models.LineupPlayer;
import edu.dosw.proyect.core.models.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LineupPlayerTest {

    @Test
    void alineacionJugador_AllArgsConstructor_ConstruyeCorrectamente() {
        Player jugador = new Player();
        jugador.setNombre("Juan");

        Lineup lineup = new Lineup();

        LineupPlayer aj = new LineupPlayer(
                1L, lineup, jugador, "TITULAR", "Delantero", 10);

        assertEquals(1L, aj.getId());
        assertEquals("TITULAR", aj.getRol());
        assertEquals("Delantero", aj.getPosicionEnCancha());
        assertEquals(10, aj.getNumeroCamiseta());
        assertEquals("Juan", aj.getJugador().getNombre());
    }

    @Test
    void alineacionJugador_NoArgsConstructor_CreaVacio() {
        LineupPlayer aj = new LineupPlayer();
        assertNull(aj.getId());
        assertNull(aj.getRol());
        assertEquals(0, aj.getNumeroCamiseta());
    }

    @Test
    void alineacionJugador_Setters_FuncionanCorrectamente() {
        LineupPlayer aj = new LineupPlayer();
        aj.setRol("RESERVA");
        aj.setPosicionEnCancha("Portero");
        aj.setNumeroCamiseta(1);

        assertEquals("RESERVA", aj.getRol());
        assertEquals("Portero", aj.getPosicionEnCancha());
        assertEquals(1, aj.getNumeroCamiseta());
    }
}