package edu.dosw.proyect.controllers.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for registering or updating the result of a match.
 * Used in: POST /api/matches/{id}/result
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterMatchResultRequestDTO {

    @NotNull(message = "Home team goals are required")
    @Min(value = 0, message = "Goals cannot be negative")
    private Integer homeGoals;

    @NotNull(message = "Away team goals are required")
    @Min(value = 0, message = "Goals cannot be negative")
    private Integer awayGoals;
}