package edu.dosw.proyect.core.models;

import edu.dosw.proyect.core.models.enums.FieldPosition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StarterEntry {
    private Long          playerId;
    private String        playerName;
    private FieldPosition fieldPosition;
}
