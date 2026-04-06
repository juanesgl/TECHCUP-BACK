package edu.dosw.proyect.persistence.entitys;


import edu.dosw.proyect.persistence.entity.*;
import edu.dosw.proyect.core.models.enums.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class AlineacionEntityTest {

    @Test
    void alineacionEntity_Builder_ConstruyeCorrectamente() {
        AlineacionEntity a = AlineacionEntity.builder()
                .id(1L)
                .formacion(TacticalFormation.F_1_2_3_1)
                .fechaRegistro(LocalDateTime.now())
                .build();

        assertEquals(1L, a.getId());
        assertEquals(TacticalFormation.F_1_2_3_1, a.getFormacion());
        assertNotNull(a.getJugadores());
    }
}
