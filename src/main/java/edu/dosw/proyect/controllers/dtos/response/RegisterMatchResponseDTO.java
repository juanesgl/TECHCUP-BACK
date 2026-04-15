package edu.dosw.proyect.controllers.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Respuesta tras registrar un partido completo con eventos.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterMatchResponseDTO {

    private Long         matchId;
    private String       homeTeam;
    private String       awayTeam;
    private int          homeGoals;
    private int          awayGoals;
    private String       outcome;          // "LOCAL" | "VISITANTE" | "EMPATE"
    private int          totalGoals;
    private int          totalYellowCards;
    private int          totalRedCards;
    private List<String> scorers;
    private List<String> yellowCardPlayers;
    private List<String> redCardPlayers;
    private String       message;
}