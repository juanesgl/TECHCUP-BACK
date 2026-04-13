package edu.dosw.proyect.controllers;

import edu.dosw.proyect.controllers.dtos.request.RegisterMatchRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.RegisterMatchResponseDTO;
import edu.dosw.proyect.core.services.MatchRegistrationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/matches")
@RequiredArgsConstructor
@Tag(name = "08 Arbitro Resultados")
public class MatchRegistrationController {

    private final MatchRegistrationService matchRegistrationService;

    @Operation(
            summary = "Registrar resultado completo de un partido",
            description = """
                    Registra el marcador final junto con los eventos del partido:
                    goleadores (GOL), tarjetas amarillas (TARJETA_AMARILLA)
                    y tarjetas rojas (TARJETA_ROJA).
                    El partido debe estar en estado PROGRAMADO o EN_CURSO.
                    Solo puede registrarse una vez; una vez FINALIZADO no se puede modificar.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Partido registrado exitosamente con todos sus eventos"),
            @ApiResponse(responseCode = "400",
                    description = "Datos de solicitud inválidos (goles negativos, campos faltantes)"),
            @ApiResponse(responseCode = "404",
                    description = "Partido, jugador o equipo no encontrado"),
            @ApiResponse(responseCode = "409",
                    description = "Partido ya FINALIZADO o CANCELADO")
    })
    @PostMapping("/{matchId}/register")
    public ResponseEntity<RegisterMatchResponseDTO> registerMatch(
            @PathVariable Long matchId,
            @Valid @RequestBody RegisterMatchRequestDTO request) {

        log.info("Registrando partido completo — ID: {}", matchId);
        RegisterMatchResponseDTO response =
                matchRegistrationService.registerMatch(matchId, request);
        return ResponseEntity.ok(response);
    }
}