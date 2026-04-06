package edu.dosw.proyect.persistence.entitys;

import edu.dosw.proyect.persistence.entity.*;
import edu.dosw.proyect.core.models.enums.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PaymentEntityTest {

    @Test
    void paymentEntity_NoArgsConstructor_CreaVacio() {
        PaymentEntity p = new PaymentEntity();
        assertNull(p.getId());
        assertNull(p.getStatus());
    }

    @Test
    void paymentEntity_AllArgsConstructor_ConstruyeCorrectamente() {
        PaymentEntity p = new PaymentEntity(
                1L, 2L, 3L, "http://file.pdf", PaymentStatus.PENDING);

        assertEquals(1L, p.getId());
        assertEquals(2L, p.getUserId());
        assertEquals(3L, p.getTournamentId());
        assertEquals("http://file.pdf", p.getFileUrl());
        assertEquals(PaymentStatus.PENDING, p.getStatus());
    }

    @Test
    void paymentEntity_Setters_FuncionanCorrectamente() {
        PaymentEntity p = new PaymentEntity();
        p.setId(1L);
        p.setUserId(2L);
        p.setTournamentId(3L);
        p.setFileUrl("http://file.pdf");
        p.setStatus(PaymentStatus.APPROVED);

        assertEquals(PaymentStatus.APPROVED, p.getStatus());
        assertEquals(2L, p.getUserId());
    }
}
