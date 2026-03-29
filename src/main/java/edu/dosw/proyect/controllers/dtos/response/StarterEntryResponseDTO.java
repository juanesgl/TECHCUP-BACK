package edu.dosw.proyect.controllers.dtos.response;

import edu.dosw.proyect.core.models.enums.FieldPosition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StarterEntryResponseDTO {
    private Long          playerId;
    private String        playerName;
    private FieldPosition fieldPosition;
}