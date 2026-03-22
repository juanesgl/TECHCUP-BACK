package edu.dosw.proyect.services;

import edu.dosw.proyect.dtos.PaymentResponse;
import edu.dosw.proyect.dtos.PaymentStatusRequest;
import edu.dosw.proyect.exceptions.BusinessException;
import edu.dosw.proyect.models.Payment;
import edu.dosw.proyect.models.enums.PaymentStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PaymentService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    private List<Payment> payments = new ArrayList<>();

    public PaymentService() {
        // Datos simulados
        Payment p1 = new Payment();
        p1.setId(1L); p1.setUserId(1L);
        p1.setTournamentId(1L);
        p1.setFileUrl("comprobante1.png");
        p1.setStatus(PaymentStatus.PENDING);

        Payment p2 = new Payment();
        p2.setId(2L); p2.setUserId(2L);
        p2.setTournamentId(1L);
        p2.setFileUrl("comprobante2.png");
        p2.setStatus(PaymentStatus.PENDING);

        payments.add(p1);
        payments.add(p2);
    }

    public PaymentResponse updateStatus(PaymentStatusRequest request) {

        logger.info("Iniciando actualizacion de estado para paymentId: {}", request.getPaymentId());

        if (request.getPaymentId() == null) {
            logger.warn("Intento de actualizar estado sin paymentId");
            throw new BusinessException("El ID del pago es obligatorio");
        }

        if (request.getStatus() == null) {
            logger.warn("Intento de actualizar estado sin status para paymentId: {}", request.getPaymentId());
            throw new BusinessException("El estado es obligatorio");
        }

        if (request.getStatus() == PaymentStatus.PENDING) {
            logger.warn("Estado PENDING no permitido para actualizacion manual");
            throw new BusinessException("No se puede asignar el estado PENDING manualmente");
        }

        for (Payment payment : payments) {
            if (payment.getId().equals(request.getPaymentId())) {
                logger.debug("Pago encontrado. PaymentId: {}, estado anterior: {}", payment.getId(), payment.getStatus());
                payment.setStatus(request.getStatus());
                logger.info("Estado actualizado. PaymentId: {}, nuevo estado: {}", payment.getId(), request.getStatus());
                return new PaymentResponse("Estado actualizado correctamente", request.getStatus().name());
            }
        }

        logger.error("Pago no encontrado para paymentId: {}", request.getPaymentId());
        throw new BusinessException("Pago no encontrado");
    }
}