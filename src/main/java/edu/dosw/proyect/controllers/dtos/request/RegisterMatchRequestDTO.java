package edu.dosw.proyect.controllers.dtos.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterMatchRequestDTO {

    @NotNull(message = "Los goles del equipo local son obligatorios")
    @Min(value = 0, message = "Los goles no pueden ser negativos")
    private Integer homeGoals;

    @NotNull(message = "Los goles del equipo visitante son obligatorios")
    @Min(value = 0, message = "Los goles no pueden ser negativos")
    private Integer awayGoals;

    @Valid
    private List<MatchEventRequestDTO> events = new ArrayList<>();
}