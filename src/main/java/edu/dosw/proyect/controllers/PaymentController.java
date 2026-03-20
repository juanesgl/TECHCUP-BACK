package edu.dosw.proyect.controllers;


import edu.dosw.proyect.dtos.PaymentResponse;
import edu.dosw.proyect.dtos.PaymentStatusRequest;
import edu.dosw.proyect.dtos.PaymentUploadRequest;
import edu.dosw.proyect.services.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/upload")
    public ResponseEntity<PaymentResponse> uploadFile(@RequestBody PaymentUploadRequest request) {
        return ResponseEntity.ok(paymentService.uploadPayment(request));
    }

    @PutMapping("/status")
    public ResponseEntity<PaymentResponse> updateStatus(@RequestBody PaymentStatusRequest request) {
        return ResponseEntity.ok(paymentService.updatePaymentStatus(request));
    }
}