package edu.dosw.proyect.controllers;

import edu.dosw.proyect.dtos.PaymentResponse;
import edu.dosw.proyect.dtos.PaymentStatusRequest;
import edu.dosw.proyect.dtos.PaymentUploadRequest;
import edu.dosw.proyect.services.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
@Tag(name = "Pagos", description = "Endpoints para gestión de comprobantes de pago")
public class PaymentController {

    private PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Operation(summary = "Subir comprobante de pago",
            description = "Permite a un usuario subir su comprobante de pago para un torneo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comprobante subido correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o faltantes")
    })
    @PostMapping("/upload")
    public ResponseEntity<PaymentResponse> uploadFile(@RequestBody PaymentUploadRequest request) {
        return ResponseEntity.ok(paymentService.uploadPayment(request));
    }

    @Operation(summary = "Cambiar estado de pago",
            description = "Permite actualizar el estado de un pago existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estado actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "Pago no encontrado o datos inválidos")
    })
    @PutMapping("/status")
    public ResponseEntity<PaymentResponse> updateStatus(@RequestBody PaymentStatusRequest request) {
        return ResponseEntity.ok(paymentService.updatePaymentStatus(request));
    }

    @Operation(summary = "Test de conexión")
    @GetMapping("/test")
    public String test() {
        return "funciona";
    }
}