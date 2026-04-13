package edu.dosw.proyect.core.services.impl;

import edu.dosw.proyect.controllers.dtos.request.PaymentStatusRequest;
import edu.dosw.proyect.controllers.dtos.request.PaymentUploadRequest;
import edu.dosw.proyect.controllers.dtos.response.PaymentResponse;
import edu.dosw.proyect.core.exceptions.BusinessRuleException;
import edu.dosw.proyect.core.exceptions.ResourceNotFoundException;
import edu.dosw.proyect.core.models.enums.PaymentStatus;
import edu.dosw.proyect.core.services.PaymentService;
import edu.dosw.proyect.persistence.entity.PaymentEntity;
import edu.dosw.proyect.persistence.entity.TournamentEntity;
import edu.dosw.proyect.persistence.repository.PaymentRepository;
import edu.dosw.proyect.persistence.repository.TournamentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private static final BigDecimal MONTO_INSCRIPCION   = new BigDecimal("130000");
    private static final String     METODO_PAGO_VALIDO  = "NEQUI";

    private final PaymentRepository    paymentRepository;
    private final TournamentRepository tournamentRepository;

    @Override
    @Transactional
    public PaymentResponse uploadPayment(PaymentUploadRequest request) {
        log.info("Procesando pago — usuario: {}, torneo: {}",
                request.getUserId(), request.getTournamentId());

        if (!METODO_PAGO_VALIDO.equalsIgnoreCase(request.getMetodoPago())) {
            throw new BusinessRuleException(
                    "Solo se acepta pago por Nequi. Método recibido: " +
                            request.getMetodoPago());
        }

        if (request.getMonto() == null ||
                request.getMonto().compareTo(MONTO_INSCRIPCION) != 0) {
            throw new BusinessRuleException(
                    "El monto debe ser exactamente $130.000 COP. " +
                            "No se aceptan pagos parciales ni montos diferentes.");
        }

        TournamentEntity torneo = tournamentRepository
                .findById(request.getTournamentId().longValue())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Torneo no encontrado con ID: " + request.getTournamentId()));

        if (torneo.getRegistrationCloseDate() != null) {
            if (LocalDate.now().isAfter(torneo.getRegistrationCloseDate())) {
                throw new BusinessRuleException(
                        "La fecha límite de pago fue el " +
                                torneo.getRegistrationCloseDate() +
                                ". Ya no se aceptan pagos de inscripción.");
            }
        }

        boolean yaAprobado = paymentRepository
                .findByUserId(request.getUserId().longValue())
                .stream()
                .anyMatch(p -> p.getTournamentId()
                        .equals(request.getTournamentId().longValue())
                        && p.getStatus() == PaymentStatus.APPROVED);

        if (yaAprobado) {
            throw new BusinessRuleException(
                    "Ya existe un pago aprobado para este torneo.");
        }

        PaymentEntity payment = new PaymentEntity();
        payment.setUserId(request.getUserId().longValue());
        payment.setTournamentId(request.getTournamentId().longValue());
        payment.setFileUrl(request.getFileUrl());
        payment.setStatus(PaymentStatus.PENDING);
        paymentRepository.save(payment);

        log.info("Comprobante de pago guardado exitosamente — ID: {}", payment.getId());
        return new PaymentResponse(
                "Comprobante subido exitosamente. El organizador lo revisará pronto.",
                "PENDING");
    }

    @Override
    @Transactional
    public PaymentResponse updatePaymentStatus(PaymentStatusRequest request) {
        log.info("Actualizando estado de pago ID: {} a {}",
                request.getPaymentId(), request.getStatus());

        PaymentEntity payment = paymentRepository.findById(request.getPaymentId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Pago no encontrado con ID: " + request.getPaymentId()));

        if (payment.getStatus() == PaymentStatus.APPROVED) {
            throw new BusinessRuleException(
                    "Este pago ya fue aprobado y no puede modificarse.");
        }

        payment.setStatus(request.getStatus());
        paymentRepository.save(payment);

        String mensaje = request.getStatus() == PaymentStatus.APPROVED
                ? "Pago aprobado. El equipo queda inscrito en el torneo."
                : "Pago rechazado. El capitán debe subir un nuevo comprobante.";

        log.info("Estado de pago ID {} actualizado a {}", payment.getId(), request.getStatus());
        return new PaymentResponse(mensaje, request.getStatus().name());
    }
}