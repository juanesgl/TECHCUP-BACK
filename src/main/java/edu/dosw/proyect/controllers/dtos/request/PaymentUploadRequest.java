package edu.dosw.proyect.controllers.dtos.request;

import lombok.Data;

@Data
public class PaymentUploadRequest {
    private Integer userId;
    private Integer tournamentId;
    private String fileName;
    private String fileUrl;

}
