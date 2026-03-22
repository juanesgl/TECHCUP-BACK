package edu.dosw.proyect.controllers;

import edu.dosw.proyect.dtos.DisponibilidadRequestDTO;
import edu.dosw.proyect.dtos.DisponibilidadResponseDTO;
import edu.dosw.proyect.services.JugadorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/jugadores")
@RequiredArgsConstructor
public class JugadorController {

    private final JugadorService jugadorService;

    @PutMapping("/{id}/disponibilidad")
    public ResponseEntity<DisponibilidadResponseDTO> actualizarDisponibilidad(
            @PathVariable Long id,
            @RequestBody DisponibilidadRequestDTO request) {
        
        DisponibilidadResponseDTO response = jugadorService.actualizarDisponibilidad(id, request);
        return ResponseEntity.ok(response);
    }
}
