package edu.dosw.proyect.services;

import edu.dosw.proyect.dtos.PaymentResponse;
import edu.dosw.proyect.dtos.PaymentStatusRequest;
import edu.dosw.proyect.exceptions.BusinessException;
import edu.dosw.proyect.models.enums.PaymentStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PaymentServiceTest {

    private PaymentService paymentService;

    @BeforeEach
    void setUp() {
        paymentService = new PaymentService();
    }

    @Test
    void updateStatus_Approved_Success() {
        PaymentStatusRequest request = new PaymentStatusRequest();
        request.setPaymentId(1L);
        request.setStatus(PaymentStatus.APPROVED);

        PaymentResponse response = paymentService.updateStatus(request);

        assertEquals("Estado actualizado correctamente", response.getMessage());
        assertEquals("APPROVED", response.getStatus());
    }

    @Test
    void updateStatus_Rejected_Success() {
        PaymentStatusRequest request = new PaymentStatusRequest();
        request.setPaymentId(2L);
        request.setStatus(PaymentStatus.REJECTED);

        PaymentResponse response = paymentService.updateStatus(request);

        assertEquals("Estado actualizado correctamente", response.getMessage());
        assertEquals("REJECTED", response.getStatus());
    }

    @Test
    void updateStatus_SinPaymentId_LanzaExcepcion() {
        PaymentStatusRequest request = new PaymentStatusRequest();
        request.setStatus(PaymentStatus.APPROVED);

        BusinessException ex = assertThrows(BusinessException.class,
                () -> paymentService.updateStatus(request));

        assertEquals("El ID del pago es obligatorio", ex.getMessage());
    }

    @Test
    void updateStatus_SinStatus_LanzaExcepcion() {
        PaymentStatusRequest request = new PaymentStatusRequest();
        request.setPaymentId(1L);

        BusinessException ex = assertThrows(BusinessException.class,
                () -> paymentService.updateStatus(request));

        assertEquals("El estado es obligatorio", ex.getMessage());
    }

    @Test
    void updateStatus_PagoNoEncontrado_LanzaExcepcion() {
        PaymentStatusRequest request = new PaymentStatusRequest();
        request.setPaymentId(999L);
        request.setStatus(PaymentStatus.APPROVED);

        BusinessException ex = assertThrows(BusinessException.class,
                () -> paymentService.updateStatus(request));

        assertEquals("Pago no encontrado", ex.getMessage());
    }

    @Test
    void updateStatus_StatusPending_LanzaExcepcion() {
        PaymentStatusRequest request = new PaymentStatusRequest();
        request.setPaymentId(1L);
        request.setStatus(PaymentStatus.PENDING);

        BusinessException ex = assertThrows(BusinessException.class,
                () -> paymentService.updateStatus(request));

        assertEquals("No se puede asignar el estado PENDING manualmente", ex.getMessage());
    }
}