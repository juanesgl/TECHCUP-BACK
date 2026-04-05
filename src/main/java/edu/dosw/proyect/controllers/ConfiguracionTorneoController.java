package edu.dosw.proyect.controllers;

import edu.dosw.proyect.controllers.dtos.request.ConfiguracionTorneoRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.ConfiguracionTorneoResponseDTO;
import edu.dosw.proyect.core.services.ConfiguracionTorneoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/torneos")
@RequiredArgsConstructor
@Tag(name = "Organizador - Configuracion", description = "Gestion de reglas, canchas y fechas del torneo")
public class ConfiguracionTorneoController {

    private final ConfiguracionTorneoService configuracionService;

    @PutMapping("/{tournId}/configuracion")
    @Operation(summary = "Configurar parametros del torneo", description = "Permite al organizador definir canchas, cierre de inscripciones, reglas y fechas importantes.")
    public ResponseEntity<ConfiguracionTorneoResponseDTO> configurarTorneo(
            @PathVariable String tournId,
            @Valid @RequestBody ConfiguracionTorneoRequestDTO configDto) {

        ConfiguracionTorneoResponseDTO response = configuracionService.configurarTorneo(tournId, configDto);
        return ResponseEntity.ok(response);
    }
}

