package edu.dosw.proyect.controllers;

import edu.dosw.proyect.controllers.dtos.request.CrearEquipoRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.CrearEquipoResponseDTO;
import edu.dosw.proyect.core.services.EquipoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/equipos")
@RequiredArgsConstructor
@Tag(name = "Equipos", description = "Operaciones relacionadas con la gestiÃ³n dinÃ¡mica de equipos del torneo")
public class EquipoController {

    private final EquipoService equipoService;

    @Operation(summary = "Crear un nuevo equipo", description = "Habilita a un capitÃ¡n para registrar y crear un nuevo equipo en TechCup validado estrictamente mediante TH-01, TH-02, y TH-03.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Equipo validado y creado de forma exitosa. Notificaciones de integrantes distribuidas."),
            @ApiResponse(responseCode = "400", description = "Argumento RequestBody malformado o error en los constraints."),
            @ApiResponse(responseCode = "404", description = "Identificador del capitÃ¡n no encontrado dentro de los repositorios de usuarios."),
            @ApiResponse(responseCode = "409", description = "LÃ³gica bloqueada por violaciones a reglas de competencia. Ejemplo, nombre en uso o cuotas deficientes (TH-03).")
    })
    @PostMapping("/crear")
    public ResponseEntity<CrearEquipoResponseDTO> crearEquipo(
            @RequestHeader("X-Capitan-ID") Long capitanId,
            @Valid @RequestBody CrearEquipoRequestDTO request) {
        
        log.info("Endpoint invocado para la creaciÃ³n de un nuevo equipo: '{}'", request.getNombreEquipo());
        
        CrearEquipoResponseDTO response = equipoService.crearEquipo(capitanId, request);
        
        log.info("OperaciÃ³n interceptada como exitosa: equipo '{}' resuelto con ID de HTTP 201", request.getNombreEquipo());
        
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}

