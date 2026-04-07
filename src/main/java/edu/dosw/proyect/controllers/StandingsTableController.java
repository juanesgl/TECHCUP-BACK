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
@Tag(name = "Arbitro - Resultados")
public class StandingsTableController {

    private final StandingsTableService standingsTableService;

    @Operation(summary = "Registrar resultado de partido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resultado registrado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Goles invalidos en la solicitud"),
            @ApiResponse(responseCode = "404", description = "Partido no encontrado"),
            @ApiResponse(responseCode = "409", description = "Partido cancelado o ya finalizado")
    })
    @PostMapping("/api/matches/{matchId}/result")
    public ResponseEntity<RegisterMatchResultResponseDTO> registerResult(
            @PathVariable Long matchId,
            @Valid @RequestBody RegisterMatchResultRequestDTO request) {
        log.info("Registrando resultado del partido ID: {}", matchId);
        RegisterMatchResultResponseDTO response =
                standingsTableService.registerResult(matchId, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Consultar tabla de posiciones del torneo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tabla de posiciones retornada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Torneo no encontrado")
    })
    @GetMapping("/api/tournaments/{tournamentId}/standings")
    public ResponseEntity<StandingsTableResponseDTO> getStandings(
            @PathVariable String tournamentId) {
        log.info("Consultando tabla de posiciones del torneo: {}", tournamentId);
        StandingsTableResponseDTO response =
                standingsTableService.getStandings(tournamentId);
        return ResponseEntity.ok(response);
    }
}