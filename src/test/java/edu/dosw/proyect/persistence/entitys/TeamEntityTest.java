package edu.dosw.proyect.persistence.entitys;


import edu.dosw.proyect.persistence.entity.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class TeamEntityTest {

    @Test
    void equipoEntity_Builder_ConstruyeCorrectamente() {
        TeamEntity e = TeamEntity.builder()
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
        TeamEntity e = new TeamEntity();
        e.setNombre("Beta FC");
        e.setColorUniformeLocal("Azul");

        assertEquals("Beta FC", e.getNombre());
        assertEquals("Azul", e.getColorUniformeLocal());
    }
}
