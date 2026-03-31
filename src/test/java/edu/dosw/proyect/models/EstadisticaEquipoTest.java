package edu.dosw.proyect.models;

import edu.dosw.proyect.core.models.Equipo;
import edu.dosw.proyect.core.models.EstadisticaEquipo;
import edu.dosw.proyect.core.models.Tournament;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EstadisticaEquipoTest {

    @Test
    void estadisticaEquipo_AllArgsConstructor_ConstruyeCorrectamente() {
        Equipo equipo = new Equipo();
        equipo.setNombre("Alpha");

        Tournament torneo = new Tournament();
        torneo.setTournId("TOURN-1");

        EstadisticaEquipo e = new EstadisticaEquipo(
                1L, equipo, torneo,
                10, 6, 2, 2,
                20, 10, 10, 20
        );

        assertEquals(1L, e.getId());
        assertEquals("Alpha", e.getEquipo().getNombre());
        assertEquals(10, e.getPartidosJugados());
        assertEquals(6, e.getPartidosGanados());
        assertEquals(2, e.getPartidosEmpatados());
        assertEquals(2, e.getPartidosPerdidos());
        assertEquals(20, e.getGolesFavor());
        assertEquals(10, e.getGolesContra());
        assertEquals(10, e.getDiferenciaGol());
        assertEquals(20, e.getPuntos());
    }

    @Test
    void estadisticaEquipo_NoArgsConstructor_CreaVacio() {
        EstadisticaEquipo e = new EstadisticaEquipo();
        assertNull(e.getId());
        assertEquals(0, e.getPartidosJugados());
        assertEquals(0, e.getPuntos());
    }

    @Test
    void estadisticaEquipo_Setters_FuncionanCorrectamente() {
        EstadisticaEquipo e = new EstadisticaEquipo();
        e.setPartidosJugados(5);
        e.setPartidosGanados(3);
        e.setGolesFavor(10);
        e.setPuntos(9);

        assertEquals(5, e.getPartidosJugados());
        assertEquals(3, e.getPartidosGanados());
        assertEquals(10, e.getGolesFavor());
        assertEquals(9, e.getPuntos());
    }
}