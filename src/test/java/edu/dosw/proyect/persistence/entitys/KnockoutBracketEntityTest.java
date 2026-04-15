package edu.dosw.proyect.persistence.entitys;

import edu.dosw.proyect.persistence.entity.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class KnockoutBracketEntityTest {

    @Test
    void llaveEliminatoriaEntity_NoArgsConstructor_CreaVacio() {
        KnockoutBracketEntity llave = new KnockoutBracketEntity();
        assertNull(llave.getId());
        assertNull(llave.getFase());
    }

    @Test
    void llaveEliminatoriaEntity_AllArgsConstructor_ConstruyeCorrectamente() {
        TournamentEntity torneo = new TournamentEntity();
        torneo.setId(1L);
        TeamEntity equipo1 = new TeamEntity();
        equipo1.setId(1L);
        TeamEntity equipo2 = new TeamEntity();
        equipo2.setId(2L);
        MatchEntity partido = new MatchEntity();
        partido.setId(1L);

        KnockoutBracketEntity llave = new KnockoutBracketEntity(
                1L, torneo, "Semifinal", 1,
                equipo1, equipo2, partido, equipo1);

        assertEquals(1L, llave.getId());
        assertEquals("Semifinal", llave.getFase());
        assertEquals(1, llave.getNumeroLlave());
        assertNotNull(llave.getGanador());
    }

    @Test
    void llaveEliminatoriaEntity_Setters_FuncionanCorrectamente() {
        KnockoutBracketEntity llave = new KnockoutBracketEntity();
        llave.setId(1L);
        llave.setFase("Final");
        llave.setNumeroLlave(1);

        TeamEntity ganador = new TeamEntity();
        ganador.setNombre("Alpha FC");
        llave.setGanador(ganador);

        assertEquals("Final", llave.getFase());
        assertEquals(1, llave.getNumeroLlave());
        assertEquals("Alpha FC", llave.getGanador().getNombre());
    }
}
