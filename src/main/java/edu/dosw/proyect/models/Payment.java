package edu.dosw.proyect.models;

import edu.dosw.proyect.models.enums.PaymentStatus;
import lombok.Data;

@Data
public class Payment {
    private Long id;
    private Long userId;
    private Long tournamentId;
    private String fileUrl;
    private PaymentStatus status;
}
