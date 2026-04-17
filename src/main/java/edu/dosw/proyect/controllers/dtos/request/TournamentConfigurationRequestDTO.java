package edu.dosw.proyect.controllers.dtos.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TournamentConfigurationRequestDTO {

    @NotNull(message = "La fecha de cierre de inscripciones es obligatoria")
    private LocalDate registrationCloseDate;

    @NotEmpty(message = "Debe configurar al menos una cancha para el torneo")
    @Valid
    private List<SoccerFieldDTO> canchas;

    private String importantDates;
    private String matchSchedules;
    private String sanctions;
    private String regulation;

    @NotNull(message = "El organizador ID es obligatorio")
    private Long organizerId; 
}

