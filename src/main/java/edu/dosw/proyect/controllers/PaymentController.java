package edu.dosw.proyect.controllers;

import edu.dosw.proyect.controllers.dtos.PaymentResponse;
import edu.dosw.proyect.controllers.dtos.PaymentStatusRequest;
import edu.dosw.proyect.controllers.dtos.PaymentUploadRequest;
import edu.dosw.proyect.core.services.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Tag(name = "Pagos", description = "Endpoints para gestion de pagos")
public class PaymentController {

    private final PaymentService paymentService;

    @Operation(summary = "Subir comprobante de pago")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comprobante subido correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos o faltantes")
    })
    @PostMapping("/upload")
    public ResponseEntity<PaymentResponse> uploadFile(@RequestBody PaymentUploadRequest request) {
        return ResponseEntity.ok(paymentService.uploadPayment(request));
    }

    @Operation(summary = "Cambiar estado de pago")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estado actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos o pago no encontrado")
    })
    @PutMapping("/status")
    public ResponseEntity<PaymentResponse> updateStatus(@RequestBody PaymentStatusRequest request) {
        return ResponseEntity.ok(paymentService.updatePaymentStatus(request));
    }

    @Operation(summary = "Test de conexion")
    @GetMapping("/test")
    public String test() {
        return "funciona";
    }
}