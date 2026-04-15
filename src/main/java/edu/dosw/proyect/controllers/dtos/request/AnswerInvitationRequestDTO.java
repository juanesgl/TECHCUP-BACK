package edu.dosw.proyect.controllers.dtos.request;

import edu.dosw.proyect.core.models.enums.InvitationResponse;
import lombok.Data;
import jakarta.validation.constraints.NotNull;

@Data
public class AnswerInvitationRequestDTO {
    @NotNull(message = "La respuesta a la invitación es obligatoria")
    private InvitationResponse respuesta;
}

