package edu.dosw.proyect.controllers;

import edu.dosw.proyect.controllers.dtos.response.EstadisticasTorneoDTO;
import edu.dosw.proyect.core.services.EstadisticasService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/estadisticas")
@RequiredArgsConstructor
@Tag(name = "Organizador - Estadisticas", description = "Endpoints para consultar las estadisticas del torneo, equipos y jugadores")
public class EstadisticasController {

    private final EstadisticasService estadisticasService;

    @Operation(summary = "Obtener todas las estadisticas de un torneo", description = "Devuelve la tabla de posiciones, goleadores y sanciones. Segun el criterio de aceptacion, si totalPartidosJugados es 0, el FrontEnd debe mostrar un mensaje informativo.")
    @GetMapping("/torneo/{tournId}")
    public ResponseEntity<EstadisticasTorneoDTO> obtenerEstadisticasTorneo(@PathVariable String tournId) {

        EstadisticasTorneoDTO estadisticas = estadisticasService.obtenerEstadisticasTorneo(tournId);

        return ResponseEntity.ok(estadisticas);
    }
}

