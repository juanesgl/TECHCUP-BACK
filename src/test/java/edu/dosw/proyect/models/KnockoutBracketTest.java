package edu.dosw.proyect.models;

import edu.dosw.proyect.core.models.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class KnockoutBracketTest {

    @Test
    void llaveEliminatoria_AllArgsConstructor_ConstruyeCorrectamente() {
        Tournament torneo = new Tournament();
        torneo.setTournId("TOURN-1");

        Team team1 = new Team();
        team1.setNombre("Alpha");

        Team team2 = new Team();
        team2.setNombre("Beta");

        Team ganador = new Team();
        ganador.setNombre("Alpha");

        Partido partido = new Partido();
        partido.setId(1L);

        KnockoutBracket llave = new KnockoutBracket(
                1L, torneo, "Semifinal", 1,
                team1, team2, partido, ganador
        );

        assertEquals(1L, llave.getId());
        assertEquals("TOURN-1", llave.getTorneo().getTournId());
        assertEquals("Semifinal", llave.getFase());
        assertEquals(1, llave.getNumeroLlave());
        assertEquals("Alpha", llave.getTeam1().getNombre());
        assertEquals("Beta", llave.getTeam2().getNombre());
        assertEquals("Alpha", llave.getGanador().getNombre());
        assertNotNull(llave.getPartido());
    }

    @Test
    void llaveEliminatoria_NoArgsConstructor_CreaVacio() {
        KnockoutBracket llave = new KnockoutBracket();
        assertNull(llave.getId());
        assertNull(llave.getFase());
        assertEquals(0, llave.getNumeroLlave());
    }

    @Test
    void llaveEliminatoria_Setters_FuncionanCorrectamente() {
        KnockoutBracket llave = new KnockoutBracket();
        llave.setFase("Final");
        llave.setNumeroLlave(2);

        assertEquals("Final", llave.getFase());
        assertEquals(2, llave.getNumeroLlave());
    }
}