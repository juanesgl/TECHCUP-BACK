package edu.dosw.proyect.models;

import edu.dosw.proyect.core.models.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LlaveEliminatoriaTest {

    @Test
    void llaveEliminatoria_AllArgsConstructor_ConstruyeCorrectamente() {
        Tournament torneo = new Tournament();
        torneo.setTournId("TOURN-1");

        Equipo equipo1 = new Equipo();
        equipo1.setNombre("Alpha");

        Equipo equipo2 = new Equipo();
        equipo2.setNombre("Beta");

        Equipo ganador = new Equipo();
        ganador.setNombre("Alpha");

        Partido partido = new Partido();
        partido.setId(1L);

        LlaveEliminatoria llave = new LlaveEliminatoria(
                1L, torneo, "Semifinal", 1,
                equipo1, equipo2, partido, ganador
        );

        assertEquals(1L, llave.getId());
        assertEquals("TOURN-1", llave.getTorneo().getTournId());
        assertEquals("Semifinal", llave.getFase());
        assertEquals(1, llave.getNumeroLlave());
        assertEquals("Alpha", llave.getEquipo1().getNombre());
        assertEquals("Beta", llave.getEquipo2().getNombre());
        assertEquals("Alpha", llave.getGanador().getNombre());
        assertNotNull(llave.getPartido());
    }

    @Test
    void llaveEliminatoria_NoArgsConstructor_CreaVacio() {
        LlaveEliminatoria llave = new LlaveEliminatoria();
        assertNull(llave.getId());
        assertNull(llave.getFase());
        assertEquals(0, llave.getNumeroLlave());
    }

    @Test
    void llaveEliminatoria_Setters_FuncionanCorrectamente() {
        LlaveEliminatoria llave = new LlaveEliminatoria();
        llave.setFase("Final");
        llave.setNumeroLlave(2);

        assertEquals("Final", llave.getFase());
        assertEquals(2, llave.getNumeroLlave());
    }
}