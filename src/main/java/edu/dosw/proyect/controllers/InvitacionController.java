package edu.dosw.proyect.controllers;

import edu.dosw.proyect.controllers.dtos.request.RespuestaInvitacionRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.InvitacionResponseDTO;
import edu.dosw.proyect.core.services.InvitacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/invitaciones")
@RequiredArgsConstructor
@Tag(name = "Invitaciones", description = "Endpoints para la gestión de invitaciones a equipos")
public class InvitacionController {

    private final InvitacionService invitacionService;

    @Operation(summary = "Responder a una invitación de equipo (Aceptar / Rechazar)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Respuesta procesada exitosamente."),
            @ApiResponse(responseCode = "404", description = "El Jugador o la Invitación no existen en el sistema."),
            @ApiResponse(responseCode = "409", description = "Violación de regla de negocio (Ej: Violación TH-01, ya perteneces a un equipo).")
    })
    @PostMapping("/{invitacionId}/responder")
    public ResponseEntity<InvitacionResponseDTO> responderInvitacion(
            @PathVariable Long invitacionId,
            @RequestParam Long jugadorId,
            @Valid @RequestBody RespuestaInvitacionRequestDTO request) {
        
        InvitacionResponseDTO response = invitacionService.responderInvitacion(jugadorId, invitacionId, request);
        return ResponseEntity.ok(response);
    }
}
