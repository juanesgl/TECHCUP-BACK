package edu.dosw.proyect.models;

import edu.dosw.proyect.models.enums.PaymentStatus;

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
    private Integer userId;
    private Integer tournamentId;
    private String fileUrl;
    private PaymentStatus status;
    private String status;


}
