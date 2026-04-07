package edu.dosw.proyect.persistence.entitys;

import edu.dosw.proyect.persistence.entity.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LlaveEliminatoriaEntityTest {

    @Test
    void llaveEliminatoriaEntity_NoArgsConstructor_CreaVacio() {
        LlaveEliminatoriaEntity llave = new LlaveEliminatoriaEntity();
        assertNull(llave.getId());
        assertNull(llave.getFase());
    }

    @Test
    void llaveEliminatoriaEntity_AllArgsConstructor_ConstruyeCorrectamente() {
        TournamentEntity torneo = new TournamentEntity();
        torneo.setId(1L);
        EquipoEntity equipo1 = new EquipoEntity();
        equipo1.setId(1L);
        EquipoEntity equipo2 = new EquipoEntity();
        equipo2.setId(2L);
        PartidoEntity partido = new PartidoEntity();
        partido.setId(1L);

        LlaveEliminatoriaEntity llave = new LlaveEliminatoriaEntity(
                1L, torneo, "Semifinal", 1,
                equipo1, equipo2, partido, equipo1);

        assertEquals(1L, llave.getId());
        assertEquals("Semifinal", llave.getFase());
        assertEquals(1, llave.getNumeroLlave());
        assertNotNull(llave.getGanador());
    }

    @Test
    void llaveEliminatoriaEntity_Setters_FuncionanCorrectamente() {
        LlaveEliminatoriaEntity llave = new LlaveEliminatoriaEntity();
        llave.setId(1L);
        llave.setFase("Final");
        llave.setNumeroLlave(1);

        EquipoEntity ganador = new EquipoEntity();
        ganador.setNombre("Alpha FC");
        llave.setGanador(ganador);

        assertEquals("Final", llave.getFase());
        assertEquals(1, llave.getNumeroLlave());
        assertEquals("Alpha FC", llave.getGanador().getNombre());
    }
}
