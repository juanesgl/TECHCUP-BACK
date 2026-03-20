package edu.dosw.proyect.controllers;

import edu.dosw.proyect.dtos.PaymentResponse;
import edu.dosw.proyect.dtos.PaymentStatusRequest;
import edu.dosw.proyect.services.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class PaymentController {

    private PaymentService paymentService = new PaymentService();

    @PutMapping("/status")
    public ResponseEntity<PaymentResponse> updateStatus(@RequestBody PaymentStatusRequest request) {
        return ResponseEntity.ok(paymentService.updateStatus(request));
    }
}
