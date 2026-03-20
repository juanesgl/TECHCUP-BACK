package edu.dosw.proyect.dtos;

import lombok.Data;

@Data
public class PaymentUploadRequest {
    private Integer userId;
    private Integer tournamentId;
    private String fileName;
    private String fileUrl;

}