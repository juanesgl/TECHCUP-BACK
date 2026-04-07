package edu.dosw.proyect.core.services;

import edu.dosw.proyect.controllers.dtos.response.PaymentResponse;
import edu.dosw.proyect.controllers.dtos.PaymentStatusRequest;
import edu.dosw.proyect.controllers.dtos.request.PaymentUploadRequest;
import edu.dosw.proyect.core.exceptions.BusinessException;
import edu.dosw.proyect.core.models.enums.PaymentStatus;
import edu.dosw.proyect.persistence.entity.PaymentEntity;
import edu.dosw.proyect.persistence.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);
    private final PaymentRepository paymentRepository;

    public PaymentResponse uploadPayment(PaymentUploadRequest request) {
        if (request.getUserId() == null) {
            throw new BusinessException("El usuario es obligatorio");
        }
        if (request.getFileUrl() == null || request.getFileUrl().isEmpty()) {
            throw new BusinessException("El comprobante es obligatorio");
        }

        PaymentEntity payment = new PaymentEntity();
        payment.setUserId(Long.valueOf(request.getUserId()));
        payment.setTournamentId(Long.valueOf(request.getTournamentId()));
        payment.setFileUrl(request.getFileUrl());
        payment.setStatus(PaymentStatus.PENDING);

        PaymentEntity saved = paymentRepository.save(payment);
        logger.info("Comprobante subido. PaymentId: {}", saved.getId());
        return new PaymentResponse("Comprobante subido correctamente", "PENDING");
    }

    public PaymentResponse updatePaymentStatus(PaymentStatusRequest request) {
        if (request.getPaymentId() == null) {
            throw new BusinessException("El ID del pago es obligatorio");
        }
        if (request.getStatus() == null) {
            throw new BusinessException("El estado es obligatorio");
        }
        if (request.getStatus() == PaymentStatus.PENDING) {
            throw new BusinessException("No se puede asignar PENDING manualmente");
        }

        PaymentEntity payment = paymentRepository.findById(request.getPaymentId())
                .orElseThrow(() -> new BusinessException("Pago no encontrado"));

        payment.setStatus(request.getStatus());
        paymentRepository.save(payment);

        logger.info("Estado actualizado. PaymentId: {}, nuevo estado: {}", payment.getId(), request.getStatus());
        return new PaymentResponse("Estado actualizado correctamente", request.getStatus().name());
    }
}