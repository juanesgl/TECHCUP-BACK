package edu.dosw.proyect.controllers.dtos;

import edu.dosw.proyect.core.models.enums.PaymentStatus;
import lombok.Data;

@Data
public class PaymentStatusRequest {
    private Long paymentId;
    private PaymentStatus status;
}
