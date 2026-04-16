package edu.dosw.proyect.controllers;

import edu.dosw.proyect.controllers.dtos.response.OpponentLineupResponseDTO;
import edu.dosw.proyect.core.services.LineupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/alineaciones")
@RequiredArgsConstructor
@Tag(name = "16 Jugador Alineacion Rival")
public class LineupController {

    private final LineupService lineupService;

    @Operation(
            summary = "Consultar alineación del equipo rival",
            description = "Permite a un capitán o jugador ver la alineación del equipo rival " +
                    "para un partido programado. Requiere que el rival haya registrado " +
                    "su alineación previamente (TC-16)."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Alineación del rival retornada exitosamente"),
            @ApiResponse(responseCode = "404",
                    description = "Partido no encontrado o rival aún no registró su alineación (TH-01)"),
            @ApiResponse(responseCode = "409",
                    description = "El equipo solicitante no participa en este partido")
    })
    @GetMapping("/rival")
    public ResponseEntity<OpponentLineupResponseDTO> consultarAlineacionRival(
            @RequestParam Long partidoId,
            @RequestParam Long equipoSolicitanteId) {

        log.info("Endpoint /api/alineaciones/rival invocado — partido: {}, equipo: {}",
                partidoId, equipoSolicitanteId);

        OpponentLineupResponseDTO response =
                lineupService.consultarAlineacionRival(partidoId, equipoSolicitanteId);

        return ResponseEntity.ok(response);
    }
}