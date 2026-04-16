package edu.dosw.proyect.core.services;

import edu.dosw.proyect.controllers.dtos.request.PaymentStatusRequest;
import edu.dosw.proyect.controllers.dtos.request.PaymentUploadRequest;
import edu.dosw.proyect.controllers.dtos.response.PaymentResponse;

public interface PaymentService {

    PaymentResponse uploadPayment(PaymentUploadRequest request);

    PaymentResponse updatePaymentStatus(PaymentStatusRequest request);
}