package edu.dosw.proyect.dtos;

import edu.dosw.proyect.models.enums.PaymentStatus;
import lombok.Data;

@Data
public class PaymentStatusRequest {
    private Long paymentId;
    private PaymentStatus status;
}
