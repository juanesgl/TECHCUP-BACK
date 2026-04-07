package edu.dosw.proyect.persistence.entitys;


import edu.dosw.proyect.persistence.entity.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class EquipoEntityTest {

    @Test
    void equipoEntity_Builder_ConstruyeCorrectamente() {
        EquipoEntity e = EquipoEntity.builder()
                .id(1L).nombre("Alpha FC")
                .escudoUrl("alpha.png")
                .colorUniformeLocal("Rojo")
                .colorUniformeVisita("Blanco")
                .estadoInscripcion("APROBADO")
                .build();

        assertEquals("Alpha FC", e.getNombre());
        assertEquals("Rojo", e.getColorUniformeLocal());
        assertEquals("APROBADO", e.getEstadoInscripcion());
        assertNotNull(e.getEquipoJugadores());
    }

    @Test
    void equipoEntity_Setters_FuncionanCorrectamente() {
        EquipoEntity e = new EquipoEntity();
        e.setNombre("Beta FC");
        e.setColorUniformeLocal("Azul");

        assertEquals("Beta FC", e.getNombre());
        assertEquals("Azul", e.getColorUniformeLocal());
    }
}
