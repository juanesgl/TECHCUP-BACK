package edu.dosw.proyect.dtos.request;

import edu.dosw.proyect.models.enums.RespuestaInvitacion;
import lombok.Data;
import jakarta.validation.constraints.NotNull;

@Data
public class RespuestaInvitacionRequestDTO {
    @NotNull(message = "La respuesta a la invitación es obligatoria")
    private RespuestaInvitacion respuesta;
}
