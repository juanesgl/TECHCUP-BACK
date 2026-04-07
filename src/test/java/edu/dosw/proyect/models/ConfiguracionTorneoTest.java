package edu.dosw.proyect.models;

import edu.dosw.proyect.core.models.ConfiguracionTorneo;
import edu.dosw.proyect.core.models.Tournament;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ConfiguracionTorneoTest {

    @Test
    void configuracionTorneo_AllArgsConstructor_ConstruyeCorrectamente() {
        Tournament torneo = new Tournament();
        torneo.setTournId("TOURN-1");

        ConfiguracionTorneo config = new ConfiguracionTorneo(
                1L, torneo,
                "Reglamento general",
                LocalDate.now().plusMonths(1),
                "Sancion por falta",
                "Fechas importantes"
        );

        assertEquals(1L, config.getId());
        assertEquals("TOURN-1", config.getTorneo().getTournId());
        assertEquals("Reglamento general", config.getReglamento());
        assertNotNull(config.getCierreInscripciones());
        assertEquals("Sancion por falta", config.getSanciones());
        assertEquals("Fechas importantes", config.getFechasImportantes());
    }

    @Test
    void configuracionTorneo_NoArgsConstructor_CreaVacio() {
        ConfiguracionTorneo config = new ConfiguracionTorneo();
        assertNull(config.getId());
        assertNull(config.getReglamento());
        assertNull(config.getSanciones());
    }

    @Test
    void configuracionTorneo_Setters_FuncionanCorrectamente() {
        ConfiguracionTorneo config = new ConfiguracionTorneo();
        config.setReglamento("Nuevo reglamento");
        config.setSanciones("Nueva sancion");
        config.setCierreInscripciones(LocalDate.now());
        config.setFechasImportantes("Fecha final");

        assertEquals("Nuevo reglamento", config.getReglamento());
        assertEquals("Nueva sancion", config.getSanciones());
        assertNotNull(config.getCierreInscripciones());
        assertEquals("Fecha final", config.getFechasImportantes());
    }
}