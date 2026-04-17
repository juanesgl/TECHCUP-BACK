package edu.dosw.proyect.controllers.dtos.request;

import edu.dosw.proyect.core.models.enums.EventType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchEventRequestDTO {

    @NotNull(message = "El tipo de evento es obligatorio")
    private EventType eventType;

    @NotNull(message = "El ID del jugador es obligatorio")
    private Long playerId;

    @NotNull(message = "El ID del equipo es obligatorio")
    private Long teamId;

    @Min(value = 0, message = "El minuto no puede ser negativo")
    private int minute;

    private String description;
}