package edu.dosw.proyect.services;

import edu.dosw.proyect.dtos.*;
import edu.dosw.proyect.exceptions.BusinessException;
import edu.dosw.proyect.models.Payment;

import java.util.ArrayList;
import java.util.List;


public class PaymentService {

    private List<Payment> payments = new ArrayList<>();
    private Integer idCounter = 1;

    public PaymentResponse uploadPayment(PaymentUploadRequest request) {

        if (request.getUserId() == null) {
            throw new BusinessException("El usuario es obligatorio");
        }

        if (request.getFileUrl() == null || request.getFileUrl().isEmpty()) {
            throw new BusinessException("El comprobante es obligatorio");
        }

        Payment payment = new Payment();
        payment.setId(idCounter++);
        payment.setUserId(request.getUserId());
        payment.setTournamentId(request.getTournamentId());
        payment.setFileUrl(request.getFileUrl());
        payment.setStatus("PENDING");

        payments.add(payment);

        return new PaymentResponse("Comprobante subido correctamente","PENDING");
    }

    public PaymentResponse updatePaymentStatus(PaymentStatusRequest request) {

        if (request.getPaymentId() == null) {
            throw new BusinessException("El ID del pago es obligatorio");
        }

        for (Payment payment : payments) {

            if (payment.getId().equals(request.getPaymentId())) {

                payment.setStatus(request.getStatus());

                return new PaymentResponse("Estado actualizado correctamente","PENDING");
            }
        }

        throw new BusinessException("Pago no encontrado");
    }
}