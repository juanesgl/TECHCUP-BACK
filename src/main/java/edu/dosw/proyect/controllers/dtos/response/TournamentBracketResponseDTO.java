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
public class TournamentBracketResponseDTO {

    private String tournamentId;
    private String tournamentName;
    private List<BracketMatchDTO> quarterFinals;
    private List<BracketMatchDTO> semiFinals;
    private BracketMatchDTO       finalMatch;
    private String                tournamentWinner;
    private String                message;
}