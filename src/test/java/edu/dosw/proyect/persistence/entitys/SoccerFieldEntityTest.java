package edu.dosw.proyect.persistence.entitys;


import edu.dosw.proyect.persistence.entity.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class SoccerFieldEntityTest {

    @Test
    void canchaEntity_Builder_ConstruyeCorrectamente() {
        SoccerFieldEntity c = SoccerFieldEntity.builder()
                .id(1L).nombre("Cancha 1")
                .direccion("Calle 100")
                .descripcion("Cancha principal")
                .build();

        assertEquals("Cancha 1", c.getNombre());
        assertEquals("Calle 100", c.getDireccion());
    }

    @Test
    void canchaEntity_Setters_FuncionanCorrectamente() {
        SoccerFieldEntity c = new SoccerFieldEntity();
        c.setNombre("Cancha 2");
        c.setDireccion("Calle 200");

        assertEquals("Cancha 2", c.getNombre());
    }
}
