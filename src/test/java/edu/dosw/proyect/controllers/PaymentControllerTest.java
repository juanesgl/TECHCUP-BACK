package edu.dosw.proyect.controllers;

import edu.dosw.proyect.controllers.dtos.PaymentResponse;
import edu.dosw.proyect.controllers.dtos.PaymentStatusRequest;
import edu.dosw.proyect.core.exceptions.BusinessException;
import edu.dosw.proyect.core.models.Payment;
import edu.dosw.proyect.core.models.enums.PaymentStatus;
import edu.dosw.proyect.persistence.repository.PaymentRepository;
import edu.dosw.proyect.core.services.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class PaymentControllerTest {

    private PaymentController paymentController;
    
    @Mock
    private PaymentRepository paymentRepository;
    
    private PaymentService paymentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        paymentService = new PaymentService(paymentRepository);
        paymentController = new PaymentController(paymentService);
    }

    @Test
    void updateStatus_Approved_Success() {
        PaymentStatusRequest request = new PaymentStatusRequest();
        request.setPaymentId(1L);
        request.setStatus(PaymentStatus.APPROVED);

        Payment mockPayment = new Payment();
        mockPayment.setId(1L);
        mockPayment.setStatus(PaymentStatus.PENDING);

        when(paymentRepository.findById(1L)).thenReturn(Optional.of(mockPayment));
        when(paymentRepository.save(any(Payment.class))).thenReturn(mockPayment);

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

        Payment mockPayment = new Payment();
        mockPayment.setId(2L);
        mockPayment.setStatus(PaymentStatus.PENDING);

        when(paymentRepository.findById(2L)).thenReturn(Optional.of(mockPayment));
        when(paymentRepository.save(any(Payment.class))).thenReturn(mockPayment);

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

    @Test
    void updateStatus_PagoNoEncontrado_LanzaExcepcion() {
        PaymentStatusRequest request = new PaymentStatusRequest();
        request.setPaymentId(999L);
        request.setStatus(PaymentStatus.APPROVED);

        when(paymentRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(BusinessException.class,
                () -> paymentController.updateStatus(request));
    }
}

