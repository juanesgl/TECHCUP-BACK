package edu.dosw.proyect.services;

import edu.dosw.proyect.controllers.dtos.request.PaymentStatusRequest;
import edu.dosw.proyect.controllers.dtos.request.PaymentUploadRequest;
import edu.dosw.proyect.controllers.dtos.response.PaymentResponse;
import edu.dosw.proyect.core.exceptions.ResourceNotFoundException;
import edu.dosw.proyect.core.models.enums.PaymentStatus;
import edu.dosw.proyect.core.services.impl.PaymentServiceImpl;
import edu.dosw.proyect.persistence.entity.PaymentEntity;
import edu.dosw.proyect.persistence.entity.TournamentEntity;
import edu.dosw.proyect.persistence.repository.PaymentRepository;
import edu.dosw.proyect.persistence.repository.TournamentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private TournamentRepository tournamentRepository;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Test
    void uploadPayment_HappyPath_RetornaResponse() {
        PaymentUploadRequest request = new PaymentUploadRequest();
        request.setUserId(1);
        request.setTournamentId(1);
        request.setFileName("comp.pdf");
        request.setFileUrl("http://url.com/file.pdf");
        request.setMetodoPago("NEQUI");
        request.setMonto(new BigDecimal("130000"));

        PaymentEntity saved = new PaymentEntity();
        saved.setId(1L);
        saved.setStatus(PaymentStatus.PENDING);
        TournamentEntity tournament = new TournamentEntity();
        tournament.setId(1L);

        when(tournamentRepository.findById(1L)).thenReturn(Optional.of(tournament));
        when(paymentRepository.findByUserId(1L)).thenReturn(java.util.List.of());
        when(paymentRepository.save(any())).thenReturn(saved);

        PaymentResponse response = paymentService.uploadPayment(request);

        assertNotNull(response);
        assertEquals("PENDING", response.getStatus());
    }

    @Test
    void uploadPayment_SinUserId_LanzaException() {
        PaymentUploadRequest request = new PaymentUploadRequest();
        request.setTournamentId(1);
        request.setFileName("comp.pdf");
        request.setFileUrl("http://url.com/file.pdf");
        request.setMetodoPago("NEQUI");
        request.setMonto(new BigDecimal("130000"));
        TournamentEntity tournament = new TournamentEntity();
        tournament.setId(1L);
        when(tournamentRepository.findById(1L)).thenReturn(Optional.of(tournament));
        assertThrows(NullPointerException.class,
                () -> paymentService.uploadPayment(request));
    }

    @Test
    void uploadPayment_SinFileUrl_LanzaException() {
        PaymentUploadRequest request = new PaymentUploadRequest();
        request.setUserId(1);
        request.setTournamentId(1);
        request.setMetodoPago("NEQUI");
        request.setMonto(new BigDecimal("130000"));
        assertThrows(ResourceNotFoundException.class,
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
        assertThrows(ResourceNotFoundException.class,
                () -> paymentService.updatePaymentStatus(request));
    }

    @Test
    void updatePaymentStatus_EstadoPending_LanzaException() {
        PaymentStatusRequest request = new PaymentStatusRequest();
        request.setPaymentId(1L);
        request.setStatus(PaymentStatus.PENDING);
        PaymentEntity entity = new PaymentEntity();
        entity.setId(1L);
        entity.setStatus(PaymentStatus.PENDING);
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(entity));
        assertDoesNotThrow(() -> paymentService.updatePaymentStatus(request));
    }

    @Test
    void updatePaymentStatus_PagoNoEncontrado_LanzaException() {
        PaymentStatusRequest request = new PaymentStatusRequest();
        request.setPaymentId(99L);
        request.setStatus(PaymentStatus.APPROVED);

        when(paymentRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class,
                () -> paymentService.updatePaymentStatus(request));
    }
}