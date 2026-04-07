package edu.dosw.proyect.persistence.entitys;

import edu.dosw.proyect.persistence.entity.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class InvitacionEntityTest {

    @Test
    void invitacionEntity_Builder_ConstruyeCorrectamente() {
        InvitacionEntity inv = InvitacionEntity.builder()
                .id(1L).estado("PENDIENTE")
                .fechaEnvio(LocalDateTime.now())
                .build();

        assertEquals(1L, inv.getId());
        assertEquals("PENDIENTE", inv.getEstado());
    }

    @Test
    void invitacionEntity_Setters_FuncionanCorrectamente() {
        InvitacionEntity inv = new InvitacionEntity();
        inv.setEstado("ACEPTADA");
        inv.setFechaRespuesta(LocalDateTime.now());

        assertEquals("ACEPTADA", inv.getEstado());
        assertNotNull(inv.getFechaRespuesta());
    }
}
