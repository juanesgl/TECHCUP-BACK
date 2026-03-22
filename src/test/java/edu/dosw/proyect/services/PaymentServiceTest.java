package edu.dosw.proyect.services;

import edu.dosw.proyect.dtos.PaymentResponse;
import edu.dosw.proyect.dtos.PaymentStatusRequest;
import edu.dosw.proyect.dtos.PaymentUploadRequest;
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
    void uploadPayment_Success() {
        PaymentUploadRequest request = new PaymentUploadRequest();
        request.setUserId(1);
        request.setTournamentId(1);
        request.setFileUrl("comprobante.png");

        PaymentResponse response = paymentService.uploadPayment(request);

        assertEquals("Comprobante subido correctamente", response.getMessage());
        assertEquals("PENDING", response.getStatus());
    }


    @Test
    void uploadPayment_SinUserId_LanzaExcepcion() {
        PaymentUploadRequest request = new PaymentUploadRequest();
        request.setFileUrl("comprobante.png");

        BusinessException ex = assertThrows(BusinessException.class,
                () -> paymentService.uploadPayment(request));

        assertEquals("El usuario es obligatorio", ex.getMessage());
    }

    @Test
    void uploadPayment_SinFileUrl_LanzaExcepcion() {
        PaymentUploadRequest request = new PaymentUploadRequest();
        request.setUserId(1);

        BusinessException ex = assertThrows(BusinessException.class,
                () -> paymentService.uploadPayment(request));

        assertEquals("El comprobante es obligatorio", ex.getMessage());
    }

    @Test
    void uploadPayment_FileUrlVacio_LanzaExcepcion() {
        PaymentUploadRequest request = new PaymentUploadRequest();
        request.setUserId(1);
        request.setFileUrl("");

        BusinessException ex = assertThrows(BusinessException.class,
                () -> paymentService.uploadPayment(request));

        assertEquals("El comprobante es obligatorio", ex.getMessage());
    }

    @Test
    void updatePaymentStatus_Approved_Success() {
        PaymentStatusRequest request = new PaymentStatusRequest();
        request.setPaymentId(1L);
        request.setStatus(PaymentStatus.APPROVED);

        PaymentResponse response = paymentService.updatePaymentStatus(request);

        assertEquals("Estado actualizado correctamente", response.getMessage());
        assertEquals("APPROVED", response.getStatus());
    }

    @Test
    void updatePaymentStatus_Rejected_Success() {
        PaymentStatusRequest request = new PaymentStatusRequest();
        request.setPaymentId(2L);
        request.setStatus(PaymentStatus.REJECTED);

        PaymentResponse response = paymentService.updatePaymentStatus(request);

        assertEquals("Estado actualizado correctamente", response.getMessage());
        assertEquals("REJECTED", response.getStatus());
    }

    @Test
    void updatePaymentStatus_SinPaymentId_LanzaExcepcion() {
        PaymentStatusRequest request = new PaymentStatusRequest();
        request.setStatus(PaymentStatus.APPROVED);

        BusinessException ex = assertThrows(BusinessException.class,
                () -> paymentService.updatePaymentStatus(request));

        assertEquals("El ID del pago es obligatorio", ex.getMessage());
    }

    @Test
    void updatePaymentStatus_SinStatus_LanzaExcepcion() {
        PaymentStatusRequest request = new PaymentStatusRequest();
        request.setPaymentId(1L);

        BusinessException ex = assertThrows(BusinessException.class,
                () -> paymentService.updatePaymentStatus(request));

        assertEquals("El estado es obligatorio", ex.getMessage());
    }

    @Test
    void updatePaymentStatus_PagoNoEncontrado_LanzaExcepcion() {
        PaymentStatusRequest request = new PaymentStatusRequest();
        request.setPaymentId(999L);
        request.setStatus(PaymentStatus.APPROVED);

        BusinessException ex = assertThrows(BusinessException.class,
                () -> paymentService.updatePaymentStatus(request));

        assertEquals("Pago no encontrado", ex.getMessage());
    }

    @Test
    void updatePaymentStatus_StatusPending_LanzaExcepcion() {
        PaymentStatusRequest request = new PaymentStatusRequest();
        request.setPaymentId(1L);
        request.setStatus(PaymentStatus.PENDING);

        BusinessException ex = assertThrows(BusinessException.class,
                () -> paymentService.updatePaymentStatus(request));

        assertEquals("No se puede asignar el estado PENDING manualmente", ex.getMessage());
    }
}