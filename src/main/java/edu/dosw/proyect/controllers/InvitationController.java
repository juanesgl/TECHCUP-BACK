package edu.dosw.proyect.controllers;

import edu.dosw.proyect.controllers.dtos.request.AnswerInvitationRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.InvitationResponseDTO;
import edu.dosw.proyect.core.services.InvitationService;
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
@Tag(name = "14 Jugador Invitaciones")
public class InvitationController {

    private final InvitationService invitationService;

    @Operation(
            summary = "Responder invitacion de equipo",
            description = "Permite a un jugador aceptar o rechazar una invitación. " +
                    "Si acepta, queda vinculado al equipo automáticamente."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Respuesta procesada exitosamente."),
            @ApiResponse(responseCode = "404", description = "El Jugador o la Invitación no existen en el sistema."),
            @ApiResponse(responseCode = "409", description = "Violacion de regla de negocio (Ej: Violacion TH-01, ya perteneces a un equipo).")
    })
    @PostMapping("/{invitacionId}/responder")
    public ResponseEntity<InvitationResponseDTO> responderInvitacion(
            @PathVariable Long invitacionId,
            @RequestParam Long jugadorId,
            @Valid @RequestBody AnswerInvitationRequestDTO request) {
        
        InvitationResponseDTO response = invitationService.responderInvitacion(jugadorId, invitacionId, request);
        return ResponseEntity.ok(response);
    }
}

