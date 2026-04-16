package edu.dosw.proyect.models;

import edu.dosw.proyect.core.models.SoccerField;
import edu.dosw.proyect.core.models.Tournament;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SoccerFieldTest {

    @Test
    void cancha_Builder_ConstruyeCorrectamente() {
        Tournament torneo = new Tournament();
        torneo.setTournId("TOURN-1");

        SoccerField soccerField = SoccerField.builder()
                .id(1L)
                .nombre("Cancha Principal")
                .direccion("Calle 100")
                .descripcion("Cancha principal del torneo")
                .torneo(torneo)
                .build();

        assertEquals(1L, soccerField.getId());
        assertEquals("Cancha Principal", soccerField.getNombre());
        assertEquals("Calle 100", soccerField.getDireccion());
        assertEquals("TOURN-1", soccerField.getTorneo().getTournId());
    }

    @Test
    void setUbicacion_ActualizaDireccion() {
        SoccerField soccerField = new SoccerField();
        soccerField.setUbicacion("Nueva Direccion 123");

        assertEquals("Nueva Direccion 123", soccerField.getDireccion());
    }

    @Test
    void cancha_NoArgsConstructor_CreaVacio() {
        SoccerField soccerField = new SoccerField();
        assertNull(soccerField.getNombre());
        assertNull(soccerField.getDireccion());
    }
}