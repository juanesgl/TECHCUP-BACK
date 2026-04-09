package edu.dosw.proyect.core.services;

import edu.dosw.proyect.controllers.dtos.response.BracketMatchDTO;
import edu.dosw.proyect.controllers.dtos.response.TournamentBracketResponseDTO;

import java.util.List;

public interface KnockoutBracketService {

    /**
     * Genera el bracket inicial del torneo de forma aleatoria.
     * Toma los equipos inscritos (con pago APROBADO) y crea las llaves
     * de cuartos de final, semifinal y final.
     */
    TournamentBracketResponseDTO generateBracket(String tournamentId);

    / /**
     * Consulta el bracket actual de un torneo.
     */
    TournamentBracketResponseDTO getBracket(String tournamentId);

    /**
     * Avanza el bracket promoviendo al ganador a la siguiente fase.
     */
    void advanceBracket(Long matchId);

    /**
     * Consulta todas las llaves de una fase específica.
     */
    List<BracketMatchDTO> getPhase(String tournamentId, String phase);
}