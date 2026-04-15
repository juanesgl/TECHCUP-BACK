package edu.dosw.proyect.persistence.entitys;

import edu.dosw.proyect.persistence.entity.TournamentConfigurationEntity;
import edu.dosw.proyect.persistence.entity.TournamentEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TournamentConfigurationEntityTest {

    @Test
    void configuracionTorneoEntity_NoArgsConstructor_CreaVacio() {
        TournamentConfigurationEntity c = new TournamentConfigurationEntity();
        assertNull(c.getId());
        assertNull(c.getReglamento());
    }

    @Test
    void configuracionTorneoEntity_Setters_FuncionanCorrectamente() {
        TournamentEntity torneo = new TournamentEntity();
        torneo.setId(1L);
        torneo.setName("TechCup");

        TournamentConfigurationEntity c = new TournamentConfigurationEntity();
        c.setId(1L);
        c.setTorneo(torneo);
        c.setReglamento("Reglamento oficial");
        c.setCierreInscripciones(LocalDate.now().plusMonths(1));
        c.setSanciones("Tarjeta roja = 1 partido");
        c.setFechasImportantes("Inicio: Abril 2026");

        assertEquals(1L, c.getId());
        assertNotNull(c.getTorneo());
        assertEquals("Reglamento oficial", c.getReglamento());
        assertNotNull(c.getCierreInscripciones());
        assertEquals("Tarjeta roja = 1 partido", c.getSanciones());
        assertEquals("Inicio: Abril 2026", c.getFechasImportantes());
    }

    @Test
    void configuracionTorneoEntity_AllArgsConstructor_ConstruyeCorrectamente() {
        TournamentEntity torneo = new TournamentEntity();
        torneo.setId(1L);

        TournamentConfigurationEntity c = new TournamentConfigurationEntity(
                1L, torneo, "Reglamento", LocalDate.now(),
                "Sanciones", "Fechas");

        assertEquals(1L, c.getId());
        assertEquals("Reglamento", c.getReglamento());
        assertEquals("Sanciones", c.getSanciones());
    }


}