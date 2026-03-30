package edu.dosw.proyect.services;

import edu.dosw.proyect.controllers.dtos.PaymentResponse;
import edu.dosw.proyect.controllers.dtos.PaymentStatusRequest;
import edu.dosw.proyect.controllers.dtos.PaymentUploadRequest;
import edu.dosw.proyect.core.exceptions.BusinessException;
import edu.dosw.proyect.core.models.Payment;
import edu.dosw.proyect.core.models.enums.PaymentStatus;
import edu.dosw.proyect.persistence.repository.PaymentRepository;
import edu.dosw.proyect.core.services.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentService paymentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void uploadPayment_Success() {
        PaymentUploadRequest request = new PaymentUploadRequest();
        request.setUserId(1);
        request.setTournamentId(1);
        request.setFileUrl("comprobante.png");

        Payment mockPayment = new Payment();
        mockPayment.setId(1L);
        mockPayment.setUserId(1L);
        mockPayment.setTournamentId(1L);
        mockPayment.setFileUrl("comprobante.png");
        mockPayment.setStatus(PaymentStatus.PENDING);

        when(paymentRepository.save(any(Payment.class))).thenReturn(mockPayment);

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

        Payment mockPayment = new Payment();
        mockPayment.setId(1L);
        mockPayment.setStatus(PaymentStatus.PENDING);

        when(paymentRepository.findById(1L)).thenReturn(Optional.of(mockPayment));
        when(paymentRepository.save(any(Payment.class))).thenReturn(mockPayment);

        PaymentResponse response = paymentService.updatePaymentStatus(request);

        assertEquals("Estado actualizado correctamente", response.getMessage());
        assertEquals("APPROVED", response.getStatus());
    }

    @Test
    void updatePaymentStatus_Rejected_Success() {
        PaymentStatusRequest request = new PaymentStatusRequest();
        request.setPaymentId(2L);
        request.setStatus(PaymentStatus.REJECTED);

        Payment mockPayment = new Payment();
        mockPayment.setId(2L);
        mockPayment.setStatus(PaymentStatus.PENDING);

        when(paymentRepository.findById(2L)).thenReturn(Optional.of(mockPayment));
        when(paymentRepository.save(any(Payment.class))).thenReturn(mockPayment);

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

        when(paymentRepository.findById(999L)).thenReturn(Optional.empty());

        BusinessException ex = assertThrows(BusinessException.class,
                () -> paymentService.updatePaymentStatus(request));

        assertEquals("Pago no encontrado", ex.getMessage());
    }
}

