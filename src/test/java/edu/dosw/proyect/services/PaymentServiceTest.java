package edu.dosw.proyect.services;

import edu.dosw.proyect.controllers.dtos.request.PaymentStatusRequest;
import edu.dosw.proyect.controllers.dtos.request.PaymentUploadRequest;
import edu.dosw.proyect.controllers.dtos.response.PaymentResponse;
import edu.dosw.proyect.core.exceptions.BusinessException;
import edu.dosw.proyect.core.models.enums.PaymentStatus;
import edu.dosw.proyect.core.services.PaymentService;
import edu.dosw.proyect.persistence.entity.PaymentEntity;
import edu.dosw.proyect.persistence.repository.PaymentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentService paymentService;

    @Test
    void uploadPayment_HappyPath_RetornaResponse() {
        PaymentUploadRequest request = new PaymentUploadRequest();
        request.setUserId(1);
        request.setTournamentId(1);
        request.setFileUrl("http://url.com/file.pdf");

        PaymentEntity saved = new PaymentEntity();
        saved.setId(1L);
        saved.setStatus(PaymentStatus.PENDING);

        when(paymentRepository.save(any())).thenReturn(saved);

        PaymentResponse response = paymentService.uploadPayment(request);

        assertNotNull(response);
        assertEquals("PENDING", response.getStatus());
    }

    @Test
    void uploadPayment_SinUserId_LanzaException() {
        PaymentUploadRequest request = new PaymentUploadRequest();
        assertThrows(BusinessException.class,
                () -> paymentService.uploadPayment(request));
    }

    @Test
    void uploadPayment_SinFileUrl_LanzaException() {
        PaymentUploadRequest request = new PaymentUploadRequest();
        request.setUserId(1);
        assertThrows(BusinessException.class,
                () -> paymentService.uploadPayment(request));
    }

    @Test
    void updatePaymentStatus_HappyPath_RetornaApproved() {
        PaymentStatusRequest request = new PaymentStatusRequest();
        request.setPaymentId(1L);
        request.setStatus(PaymentStatus.APPROVED);

        PaymentEntity entity = new PaymentEntity();
        entity.setId(1L);
        entity.setStatus(PaymentStatus.PENDING);

        when(paymentRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(paymentRepository.save(any())).thenReturn(entity);

        PaymentResponse response = paymentService.updatePaymentStatus(request);

        assertNotNull(response);
        assertEquals("APPROVED", response.getStatus());
    }

    @Test
    void updatePaymentStatus_SinPaymentId_LanzaException() {
        PaymentStatusRequest request = new PaymentStatusRequest();
        assertThrows(BusinessException.class,
                () -> paymentService.updatePaymentStatus(request));
    }

    @Test
    void updatePaymentStatus_EstadoPending_LanzaException() {
        PaymentStatusRequest request = new PaymentStatusRequest();
        request.setPaymentId(1L);
        request.setStatus(PaymentStatus.PENDING);
        assertThrows(BusinessException.class,
                () -> paymentService.updatePaymentStatus(request));
    }

    @Test
    void updatePaymentStatus_PagoNoEncontrado_LanzaException() {
        PaymentStatusRequest request = new PaymentStatusRequest();
        request.setPaymentId(99L);
        request.setStatus(PaymentStatus.APPROVED);

        when(paymentRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(BusinessException.class,
                () -> paymentService.updatePaymentStatus(request));
    }
}