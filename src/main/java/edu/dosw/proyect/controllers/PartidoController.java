package edu.dosw.proyect.controllers;

import edu.dosw.proyect.controllers.dtos.request.PartidoFiltroRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.PartidoResponseDTO;
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
@Tag(name = "Arbitro - Partidos", description = "Endpoints para consulta de partidos programados del torneo")
public class PartidoController {

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
    public ResponseEntity<List<PartidoResponseDTO>> consultarPartidos(
            @RequestBody PartidoFiltroRequestDTO filtro) {

        log.info("Endpoint /api/partidos/consultar invocado");
        List<PartidoResponseDTO> response = partidoService.consultarPartidos(filtro);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Consultar detalle de un partido",
            description = "Retorna la informacion completa de un partido especifico incluyendo arbitro asignado (TC-18)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Detalle del partido encontrado"),
            @ApiResponse(responseCode = "404", description = "Partido no encontrado")
    })
    @GetMapping("/{partidoId}")
    public ResponseEntity<PartidoResponseDTO> consultarPartidoPorId(
            @PathVariable Long partidoId) {

        log.info("Endpoint /api/partidos/{} invocado", partidoId);
        PartidoResponseDTO response = partidoService.consultarPartidoPorId(partidoId);
        return ResponseEntity.ok(response);
    }
}
