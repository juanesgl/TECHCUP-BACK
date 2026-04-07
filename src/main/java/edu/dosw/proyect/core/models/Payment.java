package edu.dosw.proyect.core.models;

import edu.dosw.proyect.core.models.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payment {

    private Long id;
    private Long userId;
    private Long tournamentId;
    private String fileUrl;
    private PaymentStatus status;
}
