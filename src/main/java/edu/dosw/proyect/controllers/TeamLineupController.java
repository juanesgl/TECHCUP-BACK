package edu.dosw.proyect.controllers;

import edu.dosw.proyect.controllers.dtos.request.SaveLineupRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.TeamLineupResponseDTO;
import edu.dosw.proyect.core.services.TeamLineupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/lineups")
@RequiredArgsConstructor
@Tag(name = "Team Lineup Management",
        description = "Endpoints for captains to manage their team's tactical lineup")
public class TeamLineupController {

    private final TeamLineupService teamLineupService;

    @Operation(
            summary = "Save a team lineup",
            description = "Allows the team captain to save the tactical lineup for a scheduled match. "
                    + "Requires exactly 7 starters, a selected formation, and a field position for each starter. "
                    + "Only the captain of the team can perform this action."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Lineup saved successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid request body â€” wrong number of starters or missing positions."),
            @ApiResponse(responseCode = "404", description = "Team or match not found."),
            @ApiResponse(responseCode = "409", description = "Business rule violation â€” not captain, match not scheduled, or lineup already exists.")
    })
    @PostMapping
    public ResponseEntity<TeamLineupResponseDTO> saveLineup(
            @RequestHeader("X-Captain-ID") Long captainId,
            @Valid @RequestBody SaveLineupRequestDTO request) {

        log.info("Save lineup request â€” captain: {}, team: {}, match: {}",
                captainId, request.getTeamId(), request.getMatchId());

        TeamLineupResponseDTO response = teamLineupService.saveLineup(captainId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Update a team lineup",
            description = "Allows the team captain to modify a saved lineup. "
                    + "Modifications are blocked once the match has started. "
                    + "The deadline for changes is the match kick-off time."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lineup updated successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid request body."),
            @ApiResponse(responseCode = "404", description = "Lineup not found."),
            @ApiResponse(responseCode = "409", description = "Lineup is locked â€” match has already started.")
    })
    @PutMapping("/{lineupId}")
    public ResponseEntity<TeamLineupResponseDTO> updateLineup(
            @RequestHeader("X-Captain-ID") Long captainId,
            @PathVariable Long lineupId,
            @Valid @RequestBody SaveLineupRequestDTO request) {

        log.info("Update lineup request â€” captain: {}, lineupId: {}", captainId, lineupId);

        TeamLineupResponseDTO response = teamLineupService.updateLineup(captainId, lineupId, request);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Get lineup for a specific match",
            description = "Retrieves the saved lineup for a given team and match. "
                    + "Returns an informative message if no lineup or no upcoming matches exist. "
                    + "Includes full pitch view data: formation, starters with positions, and reserves."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lineup retrieved successfully."),
            @ApiResponse(responseCode = "404", description = "Lineup not found â€” no scheduled matches at the moment.")
    })
    @GetMapping("/team/{teamId}/match/{matchId}")
    public ResponseEntity<TeamLineupResponseDTO> getLineup(
            @RequestHeader("X-Captain-ID") Long captainId,
            @PathVariable Long teamId,
            @PathVariable Long matchId) {

        log.info("Get lineup request â€” captain: {}, team: {}, match: {}", captainId, teamId, matchId);

        TeamLineupResponseDTO response = teamLineupService.getLineup(captainId, teamId, matchId);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Get all lineups for a team",
            description = "Returns all saved lineups for a team. "
                    + "If no lineups exist, returns an empty list with an informative message "
                    + "indicating there are no scheduled matches at the moment."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lineups retrieved successfully (may be empty).")
    })
    @GetMapping("/team/{teamId}")
    public ResponseEntity<List<TeamLineupResponseDTO>> getTeamLineups(
            @RequestHeader("X-Captain-ID") Long captainId,
            @PathVariable Long teamId) {

        log.info("Get all lineups request â€” captain: {}, team: {}", captainId, teamId);

        List<TeamLineupResponseDTO> response = teamLineupService.getTeamLineups(captainId, teamId);
        return ResponseEntity.ok(response);
    }
}
