package edu.dosw.proyect.controllers;

import edu.dosw.proyect.controllers.dtos.RegisterMatchResultRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.RegisterMatchResultResponseDTO;
import edu.dosw.proyect.controllers.dtos.response.StandingsTableResponseDTO;
import edu.dosw.proyect.core.services.StandingsTableService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Standings Table",
        description = "Match result registration and automatic standings table retrieval")
public class StandingsTableController {

    private final StandingsTableService standingsTableService;

  

    @Operation(
            summary = "Register a match result",
            description = "Records the home and away goals for a match. "
                    + "The match is automatically set to FINISHED status and the "
                    + "tournament standings table is updated immediately. "
                    + "Not applicable to CANCELLED or already FINISHED matches."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Result registered successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid request body (null or negative goals)."),
            @ApiResponse(responseCode = "404", description = "Match not found with the given ID."),
            @ApiResponse(responseCode = "409", description = "Match is CANCELLED or already FINISHED.")
    })
    @PostMapping("/api/matches/{matchId}/result")
    public ResponseEntity<RegisterMatchResultResponseDTO> registerResult(
            @PathVariable Long matchId,
            @Valid @RequestBody RegisterMatchResultRequestDTO request) {

        log.info("Register result request â€” match ID: {}", matchId);
        RegisterMatchResultResponseDTO response =
                standingsTableService.registerResult(matchId, request);
        return ResponseEntity.ok(response);
    }

 
    @Operation(
            summary = "Get automatic standings table",
            description = "Calculates and returns the current standings table for a tournament. "
                    + "Only FINISHED or IN_PROGRESS matches are counted. "
                    + "If no matches have been played yet, returns a table with all values at 0. "
                    + "Sorting order: Points â†’ Goal Difference â†’ Goals Scored â†’ Team Name."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Standings calculated successfully. "
                            + "When totalMatchesPlayed = 0, the front-end should display "
                            + "an informative message.")
    })
    @GetMapping("/api/tournaments/{tournamentId}/standings")
    public ResponseEntity<StandingsTableResponseDTO> getStandings(
            @PathVariable String tournamentId) {

        log.info("Standings request â€” tournament: {}", tournamentId);
        StandingsTableResponseDTO response =
                standingsTableService.getStandings(tournamentId);
        return ResponseEntity.ok(response);
    }
}
