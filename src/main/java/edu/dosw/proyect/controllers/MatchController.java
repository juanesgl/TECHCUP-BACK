package edu.dosw.proyect.controllers;

import edu.dosw.proyect.controllers.dtos.request.MatchFilterRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.MatchResponseDTO;
import edu.dosw.proyect.core.services.PartidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/partidos")
@RequiredArgsConstructor
@Tag(name = "07 Arbitro Partidos")
public class MatchController {

    private final PartidoService partidoService;

    @Operation(
            summary = "Consultar partidos",
            description = "Retorna la lista de partidos programados. Se puede filtrar por fecha, " +
                    "cancha, nombre de equipo o torneo. Sin filtros retorna todos ordenados " +
                    "por fecha ascendente (TC-18)."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de partidos encontrados"),
            @ApiResponse(responseCode = "404", description = "No hay partidos que coincidan con los filtros")
    })
    @PostMapping("/consultar")
    public ResponseEntity<List<MatchResponseDTO>> consultarPartidos(
            @RequestBody MatchFilterRequestDTO filtro) {

        log.info("Endpoint /api/partidos/consultar invocado");
        List<MatchResponseDTO> response = partidoService.consultarPartidos(filtro);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Consultar detalle de un partido",
            description = "Retorna la información completa de un partido especifico incluyendo arbitro asignado (TC-18)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Detalle del partido encontrado"),
            @ApiResponse(responseCode = "404", description = "Partido no encontrado")
    })
    @GetMapping("/{partidoId}")
    public ResponseEntity<MatchResponseDTO> consultarPartidoPorId(
            @PathVariable Long partidoId) {

        log.info("Endpoint /api/partidos/{} invocado", partidoId);
        MatchResponseDTO response = partidoService.consultarPartidoPorId(partidoId);
        return ResponseEntity.ok(response);
    }
}
