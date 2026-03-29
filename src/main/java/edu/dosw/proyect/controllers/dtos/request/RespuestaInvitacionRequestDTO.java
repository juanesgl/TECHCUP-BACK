package edu.dosw.proyect.controllers.dtos.request;

import edu.dosw.proyect.core.models.enums.RespuestaInvitacion;
import lombok.Data;
import jakarta.validation.constraints.NotNull;

@Data
public class RespuestaInvitacionRequestDTO {
    @NotNull(message = "La respuesta a la invitaciÃ³n es obligatoria")
    private RespuestaInvitacion respuesta;
}

