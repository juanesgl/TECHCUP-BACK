package edu.dosw.proyect.core.services;

import edu.dosw.proyect.controllers.dtos.request.PaymentStatusRequest;
import edu.dosw.proyect.controllers.dtos.request.PaymentUploadRequest;
import edu.dosw.proyect.controllers.dtos.response.PaymentResponse;

public interface PaymentService {

    /**
     * Sube comprobante de pago Nequi.
     * Valida que el torneo esté dentro de la fecha límite de pago
     * y que el monto sea exactamente $130.000.
     */
    PaymentResponse uploadPayment(PaymentUploadRequest request);

    /**
     * Aprueba o rechaza un comprobante de pago.
     * Solo el organizador puede realizar esta acción.
     */
    PaymentResponse updatePaymentStatus(PaymentStatusRequest request);
}