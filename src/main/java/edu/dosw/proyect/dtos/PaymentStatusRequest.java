package edu.dosw.proyect.dtos;

import lombok.Data;

@Data
public class PaymentStatusRequest {

    private Long paymentId;
    private String status;
}
