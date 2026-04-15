package edu.dosw.proyect.persistence.entitys;

import edu.dosw.proyect.persistence.entity.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class InvitationEntityTest {

    @Test
    void invitacionEntity_Builder_ConstruyeCorrectamente() {
        InvitationEntity inv = InvitationEntity.builder()
                .id(1L).estado("PENDIENTE")
                .fechaEnvio(LocalDateTime.now())
                .build();

        assertEquals(1L, inv.getId());
        assertEquals("PENDIENTE", inv.getEstado());
    }

    @Test
    void invitacionEntity_Setters_FuncionanCorrectamente() {
        InvitationEntity inv = new InvitationEntity();
        inv.setEstado("ACEPTADA");
        inv.setFechaRespuesta(LocalDateTime.now());

        assertEquals("ACEPTADA", inv.getEstado());
        assertNotNull(inv.getFechaRespuesta());
    }
}
