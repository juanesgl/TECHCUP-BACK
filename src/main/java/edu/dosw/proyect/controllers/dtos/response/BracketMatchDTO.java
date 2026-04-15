package edu.dosw.proyect.controllers.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Representa una llave individual dentro del bracket eliminatorio.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BracketMatchDTO {

    private Long   bracketMatchId;
    private String phase;           // "CUARTOS" | "SEMIFINAL" | "FINAL"
    private int    matchNumber;
    private Long   team1Id;
    private String team1Name;
    private Long   team2Id;
    private String team2Name;
    private Long   matchId;
    private Long   winnerId;
    private String winnerName;
    private String matchStatus;     // PROGRAMADO / FINALIZADO / PENDIENTE
}