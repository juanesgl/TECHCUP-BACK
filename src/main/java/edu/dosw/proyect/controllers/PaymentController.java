package edu.dosw.proyect.controllers;

import edu.dosw.proyect.controllers.dtos.response.PaymentResponse;
import edu.dosw.proyect.controllers.dtos.request.PaymentStatusRequest;
import edu.dosw.proyect.controllers.dtos.request.PaymentUploadRequest;
import edu.dosw.proyect.core.services.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @Operation(summary = "Subir comprobante de pago", tags = {"12 Capitan Pagos"})

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comprobante subido correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o faltantes")
    })
    @PostMapping("/upload")
    public ResponseEntity<PaymentResponse> uploadFile(
            @RequestBody PaymentUploadRequest request) {
        return ResponseEntity.ok(paymentService.uploadPayment(request));
    }

    @Operation(summary = "Aprobar o rechazar comprobante", tags = {"05 Organizador Pagos"})

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estado actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o pago no encontrado")
    })
    @PutMapping("/status")
    public ResponseEntity<PaymentResponse> updateStatus(
            @RequestBody PaymentStatusRequest request) {
        return ResponseEntity.ok(paymentService.updatePaymentStatus(request));
    }
}