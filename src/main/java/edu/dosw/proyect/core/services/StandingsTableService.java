package edu.dosw.proyect.core.services;

import edu.dosw.proyect.controllers.dtos.RegisterMatchResultRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.RegisterMatchResultResponseDTO;
import edu.dosw.proyect.controllers.dtos.response.StandingsTableResponseDTO;

/**
 * Contract for the Automatic Standings Table service.
 *
 * Responsibilities:
 *  - Register / update a match result
 *  - Calculate and return the up-to-date standings for a tournament
 */
public interface StandingsTableService {

    /**
     * Registers the result of a match (home / away goals).
     * Sets the match status to FINISHED and automatically
     * refreshes the tournament standings table.
     *
     * @param matchId match identifier
     * @param request DTO with home and away goals
     * @return summary of the registered result
     */
    RegisterMatchResultResponseDTO registerResult(Long matchId,
                                                  RegisterMatchResultRequestDTO request);

    /**
     * Calculates and returns the current standings table for a tournament.
     *
     * Sorting criteria (in priority order):
     *   1. Points (desc)
     *   2. Goal difference (desc)
     *   3. Goals scored (desc)
     *   4. Team name (asc — final tiebreaker)
     *
     * If no matches have been played yet, returns a table with all values at 0.
     *
     * @param tournamentId tournament identifier
     * @return full standings table
     */
    StandingsTableResponseDTO getStandings(String tournamentId);
}