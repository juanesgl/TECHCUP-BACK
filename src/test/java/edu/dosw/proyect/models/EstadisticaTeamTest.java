package edu.dosw.proyect.models;

import edu.dosw.proyect.core.models.Team;
import edu.dosw.proyect.core.models.TeamStatistics;
import edu.dosw.proyect.core.models.Tournament;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EstadisticaTeamTest {

    @Test
    void estadisticaEquipo_AllArgsConstructor_ConstruyeCorrectamente() {
        Team team = new Team();
        team.setNombre("Alpha");

        Tournament torneo = new Tournament();
        torneo.setTournId("TOURN-1");

        TeamStatistics e = new TeamStatistics(
                1L, team, torneo,
                10, 6, 2, 2,
                20, 10, 10, 20
        );

        assertEquals(1L, e.getId());
        assertEquals("Alpha", e.getTeam().getNombre());
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
        TeamStatistics e = new TeamStatistics();
        assertNull(e.getId());
        assertEquals(0, e.getPartidosJugados());
        assertEquals(0, e.getPuntos());
    }

    @Test
    void estadisticaEquipo_Setters_FuncionanCorrectamente() {
        TeamStatistics e = new TeamStatistics();
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