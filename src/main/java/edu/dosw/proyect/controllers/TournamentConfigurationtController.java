package edu.dosw.proyect.controllers;

import edu.dosw.proyect.controllers.dtos.request.TournamentConfigurationRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.TournamentConfigurationResponseDTO;
import edu.dosw.proyect.core.services.TournamentConfigurationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/torneos")
@RequiredArgsConstructor
@Tag(name = "04 Organizador Configuracion")
public class TournamentConfigurationtController {

    private final TournamentConfigurationService configuracionService;

    @PutMapping("/{tournId}/configuracion")
    @Operation(summary = "Configurar parámetros del torneo", description = "Permite al organizador definir canchas, cierre de inscripciones, reglas y fechas importantes.")
    public ResponseEntity<TournamentConfigurationResponseDTO> configurarTorneo(
            @PathVariable String tournId,
            @Valid @RequestBody TournamentConfigurationRequestDTO configDto) {

        TournamentConfigurationResponseDTO response = configuracionService.configurarTorneo(tournId, configDto);
        return ResponseEntity.ok(response);
    }
}