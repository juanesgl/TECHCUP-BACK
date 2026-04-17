package edu.dosw.proyect.controllers;

import edu.dosw.proyect.controllers.dtos.request.PaymentStatusRequest;
import edu.dosw.proyect.controllers.dtos.request.PaymentUploadRequest;
import edu.dosw.proyect.controllers.dtos.response.PaymentResponse;
import edu.dosw.proyect.core.models.enums.PaymentStatus;
import edu.dosw.proyect.core.services.PaymentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentControllerTest {

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private PaymentController paymentController;

    @Test
    void uploadFile_HappyPath_RetornaOk() {
        PaymentUploadRequest request = new PaymentUploadRequest();
        request.setUserId(1);
        request.setTournamentId(1);
        request.setFileName("voucher.pdf");
        request.setFileUrl("https://cdn/voucher.pdf");
        request.setMetodoPago("NEQUI");
        request.setMonto(new BigDecimal("130000"));

        PaymentResponse response = new PaymentResponse("Comprobante subido", "PENDING");
        when(paymentService.uploadPayment(request)).thenReturn(response);

        ResponseEntity<PaymentResponse> result = paymentController.uploadFile(request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals("PENDING", result.getBody().getStatus());
        verify(paymentService).uploadPayment(request);
    }

    @Test
    void updateStatus_HappyPath_RetornaOk() {
        PaymentStatusRequest request = new PaymentStatusRequest();
        request.setPaymentId(1L);
        request.setStatus(PaymentStatus.APPROVED);

        PaymentResponse response = new PaymentResponse("Estado actualizado", "APPROVED");
        when(paymentService.updatePaymentStatus(request)).thenReturn(response);

        ResponseEntity<PaymentResponse> result = paymentController.updateStatus(request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals("APPROVED", result.getBody().getStatus());
        verify(paymentService).updatePaymentStatus(request);
    }
}
