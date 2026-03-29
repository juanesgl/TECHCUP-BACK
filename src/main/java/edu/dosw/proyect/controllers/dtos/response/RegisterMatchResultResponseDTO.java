package edu.dosw.proyect.controllers.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response DTO returned after registering or updating a match result.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterMatchResultResponseDTO {
    private Long   matchId;
    private String homeTeam;
    private String awayTeam;
    private int    homeGoals;
    private int    awayGoals;
    private String outcome;   // "HOME" | "AWAY" | "DRAW"
    private String message;
}