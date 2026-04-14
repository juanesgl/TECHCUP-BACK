package edu.dosw.proyect.controllers;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/capitan/invitaciones")
@RequiredArgsConstructor
@Tag(name = "10 Capitan Invitaciones")
public class CapitanInvitacionesController {

    @Operation(summary = "Enviar invitacion a jugador disponible",
            description = "Las invitaciones se envian automáticamente al crear el equipo. " +
                    "Ver endpoint Crear Equipo en Capitan - Equipos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Invitaciones enviadas al crear el equipo")
    })
    @GetMapping("/info")
    public ResponseEntity<String> info() {
        return ResponseEntity.ok(
                "Las invitaciones se gestionan automáticamente al crear el equipo. " +
                        "Use POST /api/equipos/crear para crear el equipo e invitar jugadores.");
    }
}