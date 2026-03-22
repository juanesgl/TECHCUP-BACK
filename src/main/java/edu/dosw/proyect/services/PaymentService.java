package edu.dosw.proyect.services;

import edu.dosw.proyect.dtos.*;
import edu.dosw.proyect.exceptions.BusinessException;
import edu.dosw.proyect.models.Payment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PaymentService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    private List<Payment> payments = new ArrayList<>();
    private Long idCounter = 1L;

    public PaymentResponse uploadPayment(PaymentUploadRequest request) {

        logger.info("Iniciando subida de comprobante para userId: {}", request.getUserId());

        if (request.getUserId() == null) {
            logger.warn("Intento de subir comprobante sin userId");
            throw new BusinessException("El usuario es obligatorio");
        }

        if (request.getFileUrl() == null || request.getFileUrl().isEmpty()) {
            logger.warn("Intento de subir comprobante sin fileUrl para userId: {}", request.getUserId());
            throw new BusinessException("El comprobante es obligatorio");
        }

        Payment payment = new Payment();
        payment.setId(idCounter++);
        payment.setUserId(request.getUserId());
        payment.setTournamentId(request.getTournamentId());
        payment.setFileUrl(request.getFileUrl());
        payment.setStatus("PENDING");

        payments.add(payment);

        logger.info("Comprobante subido correctamente. PaymentId: {}, UserId: {}", payment.getId(), payment.getUserId());

        return new PaymentResponse("Comprobante subido correctamente", "PENDING");
    }

    public PaymentResponse updatePaymentStatus(PaymentStatusRequest request) {

        logger.info("Iniciando actualización de estado para paymentId: {}", request.getPaymentId());

        if (request.getPaymentId() == null) {
            logger.warn("Intento de actualizar estado sin paymentId");
            throw new BusinessException("El ID del pago es obligatorio");
        }

        for (Payment payment : payments) {
            if (payment.getId().longValue() == request.getPaymentId()) {
                logger.debug("Pago encontrado. PaymentId: {}, estado anterior: {}", payment.getId(), payment.getStatus());
                payment.setStatus(request.getStatus());
                logger.info("Estado actualizado correctamente. PaymentId: {}, nuevo estado: {}", payment.getId(), request.getStatus());
                return new PaymentResponse("Estado actualizado correctamente", request.getStatus());
            }
        }

        logger.error("Pago no encontrado para paymentId: {}", request.getPaymentId());
        throw new BusinessException("Pago no encontrado");
    }
}