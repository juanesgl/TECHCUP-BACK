package edu.dosw.proyect.services;

import edu.dosw.proyect.dtos.*;
import edu.dosw.proyect.exceptions.BusinessException;
import edu.dosw.proyect.models.Payment;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PaymentService {

    private List<Payment> payments = new ArrayList<>();

    public PaymentResponse updateStatus(PaymentStatusRequest request) {

        if (request.getPaymentId() == null) {
            throw new BusinessException("El ID del pago es obligatorio");
        }

        if (request.getStatus() == null) {
            throw new BusinessException("El estado es obligatorio");
        }

        if (!request.getStatus().equals("APPROVED") && !request.getStatus().equals("REJECTED")) {
            throw new BusinessException("Estado inválido");
        }

        for (Payment payment : payments) {

            if (payment.getId().equals(request.getPaymentId())) {

                payment.setStatus(request.getStatus());

                return new PaymentResponse("Estado actualizado correctamente");
            }
        }

        throw new BusinessException("Pago no encontrado");
    }
}
