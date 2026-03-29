package edu.dosw.proyect.controllers;

import edu.dosw.proyect.controllers.dtos.DisponibilidadRequestDTO;
import edu.dosw.proyect.controllers.dtos.DisponibilidadResponseDTO;
import edu.dosw.proyect.core.services.JugadorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/jugadores")
@RequiredArgsConstructor
@Tag(name = "Jugadores y Disponibilidad", description = "Endpoints para actualizar el estado de disponibilidad y agencia libre en el torneo")
public class JugadorController {

    private final JugadorService jugadorService;

    @Operation(summary = "Actualizar disponibilidad de un jugador", description = "Permite a un jugador entrar o salir de la agencia libre para ser invitado a equipos, validando si su perfil estÃ¡ completo.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "El estado de disponibilidad ha sido modificado exitosamente."),
            @ApiResponse(responseCode = "404", description = "Identificador del jugador no encontrado en el sistema."),
            @ApiResponse(responseCode = "409", description = "ViolaciÃ³n de regla: El jugador ya tiene equipo o su perfil no cumple las condiciones.")
    })
    @PutMapping("/{id}/disponibilidad")
    public ResponseEntity<DisponibilidadResponseDTO> actualizarDisponibilidad(
            @PathVariable Long id,
            @RequestBody DisponibilidadRequestDTO request) {
        
        DisponibilidadResponseDTO response = jugadorService.actualizarDisponibilidad(id, request);
        return ResponseEntity.ok(response);
    }
}

