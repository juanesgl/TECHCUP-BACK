package edu.dosw.proyect.controllers.dtos.request;

import edu.dosw.proyect.core.models.enums.FieldPosition;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StarterEntryRequestDTO {

    @NotNull(message = "El id del jugador es obligatorio")
    private Long playerId;

    @NotNull(message = "La posicion de campo de cada titular es obligatoria")
    private FieldPosition fieldPosition;
}
