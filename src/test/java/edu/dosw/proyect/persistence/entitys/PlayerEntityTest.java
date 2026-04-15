package edu.dosw.proyect.persistence.entitys;

import edu.dosw.proyect.persistence.entity.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerEntityTest {

    @Test
    void jugadorEntity_NoArgsConstructor_CreaVacio() {
        PlayerEntity j = new PlayerEntity();
        assertNull(j.getId());
        assertFalse(j.isDisponible());
        assertFalse(j.isPerfilCompleto());
        assertFalse(j.isTieneEquipo());
    }

    @Test
    void jugadorEntity_AllArgsConstructor_ConstruyeCorrectamente() {
        UserEntity user = UserEntity.builder()
                .id(1L).name("Juan").email("j@mail.com")
                .password("p").role("PLAYER").build();

        PlayerEntity j = new PlayerEntity(
                1L, user, "foto.png", "Delantero", 9,
                true, "5", "MASCULINO", "123456", 22, true, false);

        assertEquals(1L, j.getId());
        assertEquals("Delantero", j.getPosiciones());
        assertTrue(j.isDisponible());
        assertTrue(j.isPerfilCompleto());
        assertFalse(j.isTieneEquipo());
        assertEquals(9, j.getDorsal());
        assertEquals(22, j.getEdad());
    }

    @Test
    void jugadorEntity_Setters_TodosLosCampos() {
        PlayerEntity j = new PlayerEntity();
        j.setId(1L);
        j.setPosiciones("Portero");
        j.setDorsal(1);
        j.setDisponible(true);
        j.setSemestre("8");
        j.setGenero("MASCULINO");
        j.setIdentificacion("987654");
        j.setEdad(25);
        j.setPerfilCompleto(true);
        j.setTieneEquipo(false);
        j.setFotoUrl("foto.jpg");

        assertEquals("Portero", j.getPosiciones());
        assertEquals(1, j.getDorsal());
        assertTrue(j.isDisponible());
        assertEquals("8", j.getSemestre());
        assertEquals(25, j.getEdad());
        assertTrue(j.isPerfilCompleto());
    }
}
