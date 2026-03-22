package edu.dosw.proyect.controllers;

import edu.dosw.proyect.dtos.request.RespuestaInvitacionRequestDTO;
import edu.dosw.proyect.dtos.response.InvitacionResponseDTO;
import edu.dosw.proyect.services.InvitacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/invitaciones")
@RequiredArgsConstructor
@Tag(name = "Invitaciones", description = "API para gestionar invitaciones de equipo")
public class InvitacionController {

    private final InvitacionService invitacionService;

    @Operation(summary = "Responder a una invitación", description = "Permite a un jugador aceptar o rechazar una invitación.")
    @PostMapping("/{invitacionId}/responder")
    public ResponseEntity<InvitacionResponseDTO> responderInvitacion(
            @PathVariable Long invitacionId,
            @RequestParam Long jugadorId,
            @RequestBody @jakarta.validation.Valid RespuestaInvitacionRequestDTO request) {
        
        InvitacionResponseDTO response = invitacionService.responderInvitacion(jugadorId, invitacionId, request);
        return ResponseEntity.ok(response);
    }
}
