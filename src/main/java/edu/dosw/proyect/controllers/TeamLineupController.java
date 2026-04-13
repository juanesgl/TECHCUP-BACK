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
@Tag(name = "11 Capitan Alineaciones")
public class TeamLineupController {

    private final TeamLineupService teamLineupService;

    @Operation(summary = "Guardar alineación del equipo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Alineación guardada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos"),
            @ApiResponse(responseCode = "404", description = "Equipo o partido no encontrado"),
            @ApiResponse(responseCode = "409", description = "Alineación ya existe o partido no programado")
    })
    @PostMapping
    public ResponseEntity<TeamLineupResponseDTO> saveLineup(
            @RequestHeader("X-Captain-ID") Long captainId,
            @Valid @RequestBody SaveLineupRequestDTO request) {
        log.info("Guardando alineación — capitan: {}, equipo: {}, partido: {}",
                captainId, request.getTeamId(), request.getMatchId());
        TeamLineupResponseDTO response = teamLineupService.saveLineup(captainId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar alineación del equipo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Alineación actualizada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Alineación no encontrada"),
            @ApiResponse(responseCode = "409", description = "Partido ya comenzo")
    })
    @PutMapping("/{lineupId}")
    public ResponseEntity<TeamLineupResponseDTO> updateLineup(
            @RequestHeader("X-Captain-ID") Long captainId,
            @PathVariable Long lineupId,
            @Valid @RequestBody SaveLineupRequestDTO request) {
        log.info("Actualizando alineación — capitán: {}, alineacionId: {}", captainId, lineupId);
        TeamLineupResponseDTO response = teamLineupService.updateLineup(captainId, lineupId, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Consultar alineacion por partido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Alineación retornada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Alineación no encontrada")
    })
    @GetMapping("/team/{teamId}/match/{matchId}")
    public ResponseEntity<TeamLineupResponseDTO> getLineup(
            @RequestHeader("X-Captain-ID") Long captainId,
            @PathVariable Long teamId,
            @PathVariable Long matchId) {
        log.info("Consultando alineación — capitan: {}, equipo: {}, partido: {}",
                captainId, teamId, matchId);
        TeamLineupResponseDTO response = teamLineupService.getLineup(captainId, teamId, matchId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Consultar todas las alineaciones del equipo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Alineaciones retornadas exitosamente")
    })
    @GetMapping("/team/{teamId}")
    public ResponseEntity<List<TeamLineupResponseDTO>> getTeamLineups(
            @RequestHeader("X-Captain-ID") Long captainId,
            @PathVariable Long teamId) {
        log.info("Consultando alineaciones — capitan: {}, equipo: {}", captainId, teamId);
        List<TeamLineupResponseDTO> response = teamLineupService.getTeamLineups(captainId, teamId);
        return ResponseEntity.ok(response);
    }
}