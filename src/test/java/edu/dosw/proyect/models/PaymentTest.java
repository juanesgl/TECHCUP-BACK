package edu.dosw.proyect.models;

import edu.dosw.proyect.core.models.Payment;
import edu.dosw.proyect.core.models.enums.PaymentStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PaymentTest {

    @Test
    void payment_AllArgsConstructor_ConstruyeCorrectamente() {
        Payment payment = new Payment(
                1L, 10L, 5L,
                "file.pdf", PaymentStatus.PENDING
        );

        assertEquals(1L, payment.getId());
        assertEquals(10L, payment.getUserId());
        assertEquals(5L, payment.getTournamentId());
        assertEquals("file.pdf", payment.getFileUrl());
        assertEquals(PaymentStatus.PENDING, payment.getStatus());
    }

    @Test
    void payment_NoArgsConstructor_CreaVacio() {
        Payment payment = new Payment();
        assertNull(payment.getId());
        assertNull(payment.getStatus());
    }

    @Test
    void payment_Setters_FuncionanCorrectamente() {
        Payment payment = new Payment();
        payment.setUserId(3L);
        payment.setTournamentId(7L);
        payment.setFileUrl("comprobante.jpg");
        payment.setStatus(PaymentStatus.APPROVED);

        assertEquals(3L, payment.getUserId());
        assertEquals(7L, payment.getTournamentId());
        assertEquals("comprobante.jpg", payment.getFileUrl());
        assertEquals(PaymentStatus.APPROVED, payment.getStatus());
    }
}