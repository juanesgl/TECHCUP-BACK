package edu.dosw.proyect.controllers;

import edu.dosw.proyect.dtos.PaymentResponse;
import edu.dosw.proyect.dtos.PaymentStatusRequest;
import edu.dosw.proyect.exceptions.BusinessException;
import edu.dosw.proyect.models.enums.PaymentStatus;
import edu.dosw.proyect.services.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class PaymentControllerTest {

    private PaymentController paymentController;

    @BeforeEach
    void setUp() {
        paymentController = new PaymentController(new PaymentService());
    }

    @Test
    void updateStatus_Approved_Success() {
        PaymentStatusRequest request = new PaymentStatusRequest();
        request.setPaymentId(1L);
        request.setStatus(PaymentStatus.APPROVED);

        ResponseEntity<PaymentResponse> response = paymentController.updateStatus(request);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Estado actualizado correctamente", response.getBody().getMessage());
        assertEquals("APPROVED", response.getBody().getStatus());
    }

    @Test
    void updateStatus_Rejected_Success() {
        PaymentStatusRequest request = new PaymentStatusRequest();
        request.setPaymentId(2L);
        request.setStatus(PaymentStatus.REJECTED);

        ResponseEntity<PaymentResponse> response = paymentController.updateStatus(request);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Estado actualizado correctamente", response.getBody().getMessage());
        assertEquals("REJECTED", response.getBody().getStatus());
    }

    @Test
    void updateStatus_SinPaymentId_LanzaExcepcion() {
        PaymentStatusRequest request = new PaymentStatusRequest();
        request.setStatus(PaymentStatus.APPROVED);

        assertThrows(BusinessException.class,
                () -> paymentController.updateStatus(request));
    }

    @Test
    void updateStatus_SinStatus_LanzaExcepcion() {
        PaymentStatusRequest request = new PaymentStatusRequest();
        request.setPaymentId(1L);

        assertThrows(BusinessException.class,
                () -> paymentController.updateStatus(request));
    }

    // ❌ Error - pago no encontrado
    @Test
    void updateStatus_PagoNoEncontrado_LanzaExcepcion() {
        PaymentStatusRequest request = new PaymentStatusRequest();
        request.setPaymentId(999L);
        request.setStatus(PaymentStatus.APPROVED);

        assertThrows(BusinessException.class,
                () -> paymentController.updateStatus(request));
    }

    @Test
    void updateStatus_StatusPending_LanzaExcepcion() {
        PaymentStatusRequest request = new PaymentStatusRequest();
        request.setPaymentId(1L);
        request.setStatus(PaymentStatus.PENDING);

        assertThrows(BusinessException.class,
                () -> paymentController.updateStatus(request));
    }
}