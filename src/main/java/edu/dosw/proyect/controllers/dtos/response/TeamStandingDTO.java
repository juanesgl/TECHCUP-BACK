package edu.dosw.proyect.controllers.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamStandingDTO {
    private int    position;
    private Long   teamId;
    private String teamName;
    private int    matchesPlayed;
    private int    wins;
    private int    draws;
    private int    losses;
    private int    goalsFor;
    private int    goalsAgainst;
    private int    goalDifference;
    private int    points;
}
