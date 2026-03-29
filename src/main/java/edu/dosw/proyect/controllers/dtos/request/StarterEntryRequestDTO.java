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

    @NotNull(message = "Player ID is required")
    private Long playerId;

    @NotNull(message = "Field position is required for each starter")
    private FieldPosition fieldPosition;
}
