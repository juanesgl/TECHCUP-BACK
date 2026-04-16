package edu.dosw.proyect.controllers.dtos.request;

import edu.dosw.proyect.core.models.enums.PaymentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PaymentStatusRequest {
    @NotNull(message = "El ID del pago es obligatorio")
    private Long paymentId;

    @NotNull(message = "El estado del pago es obligatorio")
    private PaymentStatus status;
}
