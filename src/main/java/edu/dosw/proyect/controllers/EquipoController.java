package edu.dosw.proyect.controllers;

import edu.dosw.proyect.controllers.dtos.request.ActualizarEquipoRequestDTO;
import edu.dosw.proyect.controllers.dtos.request.CrearEquipoRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.CrearEquipoResponseDTO;
import edu.dosw.proyect.controllers.dtos.response.EquipoResponseDTO;
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

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/equipos")
@RequiredArgsConstructor
@Tag(name = "09 Capitan Equipos")
public class EquipoController {

    private final EquipoService equipoService;


    @Operation(summary = "Crear un nuevo equipo",
            description = """
                    Crea un equipo e invita jugadores. Reglas del torneo:
                    - Mínimo 8 integrantes (incluyendo el capitán).
                    - Máximo 12 integrantes.
                    - Más de la mitad deben ser de Sistemas, IA, Ciberseguridad o Estadística.
                    - Ningún jugador puede pertenecer a dos equipos.
                    """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Equipo creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Capitán o jugador no encontrado"),
            @ApiResponse(responseCode = "409",
                    description = "Nombre en uso, equipo lleno o jugador ya tiene equipo")
    })
    @PostMapping("/crear")
    public ResponseEntity<CrearEquipoResponseDTO> crearEquipo(
            @RequestHeader("X-Capitan-ID") Long capitanId,
            @Valid @RequestBody CrearEquipoRequestDTO request) {
        log.info("Creando equipo: '{}'", request.getNombreEquipo());
        return new ResponseEntity<>(
                equipoService.crearEquipo(capitanId, request), HttpStatus.CREATED);
    }


    @Operation(summary = "Consultar equipo por ID",
            description = "Retorna la información completa de un equipo.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Equipo encontrado"),
            @ApiResponse(responseCode = "404", description = "Equipo no encontrado")
    })
    @GetMapping("/{equipoId}")
    public ResponseEntity<EquipoResponseDTO> consultarEquipo(
            @PathVariable Long equipoId) {
        log.info("Consultando equipo ID: {}", equipoId);
        return ResponseEntity.ok(equipoService.consultarEquipo(equipoId));
    }


    @Operation(summary = "Consultar equipos de un torneo",
            description = "Retorna todos los equipos inscritos en el torneo.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Equipos retornados exitosamente"),
            @ApiResponse(responseCode = "404", description = "Torneo no encontrado")
    })
    @GetMapping("/torneo/{tournamentId}")
    public ResponseEntity<List<EquipoResponseDTO>> consultarEquiposPorTorneo(
            @PathVariable String tournamentId) {
        log.info("Consultando equipos del torneo: {}", tournamentId);
        return ResponseEntity.ok(equipoService.consultarEquiposPorTorneo(tournamentId));
    }


    @Operation(summary = "Consultar jugadores del equipo",
            description = "Lista todos los jugadores activos vinculados al equipo.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Jugadores retornados exitosamente"),
            @ApiResponse(responseCode = "404", description = "Equipo no encontrado")
    })
    @GetMapping("/{equipoId}/jugadores")
    public ResponseEntity<List<String>> consultarJugadores(
            @PathVariable Long equipoId) {
        log.info("Consultando jugadores del equipo ID: {}", equipoId);
        return ResponseEntity.ok(equipoService.consultarJugadoresEquipo(equipoId));
    }


    @Operation(summary = "Actualizar datos del equipo",
            description = """
                    Solo el capitán puede actualizar su equipo.
                    Campos actualizables: nombre, escudo y colores del uniforme.
                    """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Equipo actualizado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "403", description = "Solo el capitán puede actualizar el equipo"),
            @ApiResponse(responseCode = "404", description = "Equipo no encontrado"),
            @ApiResponse(responseCode = "409", description = "El nombre ya está en uso")
    })
    @PutMapping("/{equipoId}")
    public ResponseEntity<EquipoResponseDTO> actualizarEquipo(
            @PathVariable Long equipoId,
            @RequestHeader("X-Capitan-ID") Long capitanId,
            @Valid @RequestBody ActualizarEquipoRequestDTO request) {
        log.info("Actualizando equipo ID: {}", equipoId);
        return ResponseEntity.ok(
                equipoService.actualizarEquipo(equipoId, capitanId, request));
    }


    @Operation(summary = "Eliminar equipo",
            description = """
                    Solo el capitán puede eliminar su equipo.
                    Al eliminarlo, todos los jugadores quedan liberados y disponibles.
                    """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Equipo eliminado exitosamente"),
            @ApiResponse(responseCode = "403", description = "Solo el capitán puede eliminar el equipo"),
            @ApiResponse(responseCode = "404", description = "Equipo no encontrado")
    })
    @DeleteMapping("/{equipoId}")
    public ResponseEntity<Void> eliminarEquipo(
            @PathVariable Long equipoId,
            @RequestHeader("X-Capitan-ID") Long capitanId) {
        log.info("Eliminando equipo ID: {}", equipoId);
        equipoService.eliminarEquipo(equipoId, capitanId);
        return ResponseEntity.noContent().build();
    }
}