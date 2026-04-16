package edu.dosw.proyect.core.services;

import edu.dosw.proyect.controllers.dtos.response.BracketMatchDTO;
import edu.dosw.proyect.controllers.dtos.response.TournamentBracketResponseDTO;

import java.util.List;

public interface KnockoutBracketService {


    TournamentBracketResponseDTO generateBracket(String tournamentId);


    TournamentBracketResponseDTO getBracket(String tournamentId);


    void advanceBracket(Long matchId);


    List<BracketMatchDTO> getPhase(String tournamentId, String phase);
}