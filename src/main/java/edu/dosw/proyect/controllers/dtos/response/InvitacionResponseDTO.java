package edu.dosw.proyect.controllers.dtos.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InvitacionResponseDTO {
    private Long invitacionId;
    private String mensajeCapitan;
    private String estadoActualizado;
}

