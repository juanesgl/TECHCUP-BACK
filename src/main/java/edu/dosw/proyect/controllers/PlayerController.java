package edu.dosw.proyect.controllers;

import edu.dosw.proyect.controllers.dtos.PlayerFilterRequest;
import edu.dosw.proyect.controllers.dtos.PlayerResponse;
import edu.dosw.proyect.core.services.PlayerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/players")
@RequiredArgsConstructor
@Tag(name = "Jugadores", description = "Endpoints para busqueda de jugadores")
public class PlayerController {

    private final PlayerService playerService;

    @Operation(summary = "Buscar jugadores por filtro",
            description = "Permite buscar jugadores por nombre, posicion y/o edad")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Jugadores encontrados"),
            @ApiResponse(responseCode = "400", description = "Sin filtros o sin resultados")
    })
    @PostMapping("/filter")
    public ResponseEntity<List<PlayerResponse>> filterPlayers(@RequestBody PlayerFilterRequest request) {
        return ResponseEntity.ok(playerService.filterPlayers(request));
    }
}