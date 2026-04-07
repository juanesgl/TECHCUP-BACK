package edu.dosw.proyect.controllers;

import edu.dosw.proyect.controllers.dtos.request.PartidoFiltroRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.PartidoResponseDTO;
import edu.dosw.proyect.core.services.PartidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jugador/partidos")
@RequiredArgsConstructor
@Tag(name = "Jugador - Partidos")
public class JugadorPartidoController {

    private final PartidoService partidoService;

    @Operation(summary = "Consultar partidos con filtros")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Partidos encontrados"),
            @ApiResponse(responseCode = "404", description = "No hay partidos que coincidan con los filtros")
    })
    @PostMapping("/consultar")
    public ResponseEntity<List<PartidoResponseDTO>> consultarPartidos(
            @RequestBody PartidoFiltroRequestDTO filtro) {
        return ResponseEntity.ok(partidoService.consultarPartidos(filtro));
    }

    @Operation(summary = "Consultar detalle de un partido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Partido encontrado"),
            @ApiResponse(responseCode = "404", description = "Partido no encontrado")
    })
    @GetMapping("/{partidoId}")
    public ResponseEntity<PartidoResponseDTO> consultarPartidoPorId(
            @PathVariable Long partidoId) {
        return ResponseEntity.ok(partidoService.consultarPartidoPorId(partidoId));
    }
}