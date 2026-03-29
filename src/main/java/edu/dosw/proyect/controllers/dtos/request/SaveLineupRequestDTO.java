package edu.dosw.proyect.controllers.dtos.request;

import edu.dosw.proyect.core.models.enums.TacticalFormation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaveLineupRequestDTO {

    @NotNull(message = "Team ID is required")
    private Long teamId;

    @NotNull(message = "Match ID is required")
    private Long matchId;

    @NotNull(message = "A tactical formation must be selected")
    private TacticalFormation formation;

    @NotNull(message = "Starters list is required")
    @Size(min = 7, max = 7, message = "You must select exactly 7 starter players")
    @Valid
    private List<StarterEntryRequestDTO> starters;

    private List<Long> reserveIds;
}
