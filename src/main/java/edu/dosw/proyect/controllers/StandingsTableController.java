package edu.dosw.proyect.controllers;

import edu.dosw.proyect.controllers.dtos.request.RegisterMatchResultRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.RegisterMatchResultResponseDTO;
import edu.dosw.proyect.controllers.dtos.response.StandingsTableResponseDTO;
import edu.dosw.proyect.core.services.StandingsTableService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class StandingsTableController {

    private final StandingsTableService standingsTableService;

    @Operation(summary = "Registrar resultado de partido", tags = {"08 Arbitro Resultados"})

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resultado registrado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Goles inválidos en la solicitud"),
            @ApiResponse(responseCode = "404", description = "Partido no encontrado"),
            @ApiResponse(responseCode = "409", description = "Partido cancelado o ya finalizado")
    })
    @PostMapping("/api/matches/{matchId}/result")
    public ResponseEntity<RegisterMatchResultResponseDTO> registerResult(
            @PathVariable Long matchId,
            @Valid @RequestBody RegisterMatchResultRequestDTO request) {
        log.info("Registrando resultado del partido ID: {}", matchId);
        return ResponseEntity.ok(standingsTableService.registerResult(matchId, request));
    }

    @Operation(summary = "Consultar tabla de posiciones", tags = {"06 Organizador Estadisticas"})

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tabla de posiciones retornada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Torneo no encontrado")
    })
    @GetMapping("/api/tournaments/{tournamentId}/standings")
    public ResponseEntity<StandingsTableResponseDTO> getStandings(
            @PathVariable String tournamentId) {
        log.info("Consultando tabla de posiciones del torneo: {}", tournamentId);
        return ResponseEntity.ok(standingsTableService.getStandings(tournamentId));
    }
}