package edu.dosw.proyect.controllers.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StandingsTableResponseDTO {
    private String              tournamentId;
    private String              tournamentName;
    private int                 totalTeams;
    private int                 totalMatchesPlayed;
    private List<TeamStandingDTO> standings;
}
