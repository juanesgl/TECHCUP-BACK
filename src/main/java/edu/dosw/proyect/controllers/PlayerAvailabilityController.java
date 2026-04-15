package edu.dosw.proyect.controllers;

import edu.dosw.proyect.controllers.dtos.request.AvailabilityRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.AvailabilityResponseDTO;
import edu.dosw.proyect.core.services.PlayerAvailabilityService;
import jakarta.validation.Valid;
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
@Tag(name = "13 Jugador Disponibilidad")
public class PlayerAvailabilityController {

    private final PlayerAvailabilityService playerAvailabilityService;

    @Operation(summary = "Actualizar disponibilidad de un jugador", description = "Permite a un jugador entrar o salir" +
            " de la agencia libre para ser invitado a equipos, validando si su perfil esta completo.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "El estado de disponibilidad ha sido modificado exitosamente."),
            @ApiResponse(responseCode = "404", description = "Identificador del jugador no encontrado en el sistema."),
            @ApiResponse(responseCode = "409", description = "Violacion de regla: El jugador ya tiene equipo o su perfil no cumple las condiciones.")
    })
    @PutMapping("/{id}/disponibilidad")
    public ResponseEntity<AvailabilityResponseDTO> actualizarDisponibilidad(
            @PathVariable Long id,
            @Valid @RequestBody AvailabilityRequestDTO request) {
        
        AvailabilityResponseDTO response = playerAvailabilityService.actualizarDisponibilidad(id, request);
        return ResponseEntity.ok(response);
    }
}

