package edu.dosw.proyect.controllers.dtos.response;

import edu.dosw.proyect.controllers.dtos.request.SoccerFieldDTO;
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
public class TournamentConfigurationResponseDTO {
    private String message;
    private String tournId;
    private LocalDate registrationCloseDate;
    private List<SoccerFieldDTO> canchas;
}

