package edu.dosw.proyect.models;

import edu.dosw.proyect.core.models.Cancha;
import edu.dosw.proyect.core.models.Tournament;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CanchaTest {

    @Test
    void cancha_Builder_ConstruyeCorrectamente() {
        Tournament torneo = new Tournament();
        torneo.setTournId("TOURN-1");

        Cancha cancha = Cancha.builder()
                .id(1L)
                .nombre("Cancha Principal")
                .direccion("Calle 100")
                .descripcion("Cancha principal del torneo")
                .torneo(torneo)
                .build();

        assertEquals(1L, cancha.getId());
        assertEquals("Cancha Principal", cancha.getNombre());
        assertEquals("Calle 100", cancha.getDireccion());
        assertEquals("TOURN-1", cancha.getTorneo().getTournId());
    }

    @Test
    void setUbicacion_ActualizaDireccion() {
        Cancha cancha = new Cancha();
        cancha.setUbicacion("Nueva Direccion 123");

        assertEquals("Nueva Direccion 123", cancha.getDireccion());
    }

    @Test
    void cancha_NoArgsConstructor_CreaVacio() {
        Cancha cancha = new Cancha();
        assertNull(cancha.getNombre());
        assertNull(cancha.getDireccion());
    }
}