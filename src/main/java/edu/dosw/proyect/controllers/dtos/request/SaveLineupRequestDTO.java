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

    @NotNull(message = "El id del equipo es obligatorio")
    private Long teamId;

    @NotNull(message = "El id del partido es obligatorio")
    private Long matchId;

    @NotNull(message = "Se debe seleccionar una formación táctica")
    private TacticalFormation formation;

    @NotNull(message = "La lista de titulares es obligatoria")
    @Size(min = 7, max = 7, message = "Debe seleccionar 7 jugadores titulares")
    @Valid
    private List<StarterEntryRequestDTO> starters;

    private List<Long> reserveIds;
}
