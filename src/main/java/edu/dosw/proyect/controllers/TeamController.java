package edu.dosw.proyect.controllers;

import edu.dosw.proyect.controllers.dtos.request.UpdateTeamRequestDTO;
import edu.dosw.proyect.controllers.dtos.request.CreateTeamRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.CreateTeamResponseDTO;
import edu.dosw.proyect.controllers.dtos.response.TeamResponseDTO;
import edu.dosw.proyect.core.services.TeamService;
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
@RequestMapping("/api/teams")
@RequiredArgsConstructor
@Tag(name = "09 Capitan Equipos")
public class TeamController {

    private final TeamService teamService;


    @Operation(summary = "Crear un nuevo team",
            description = """
                    Crea un team e invita jugadores. Reglas del torneo:
                    - Mínimo 8 integrantes (incluyendo el capitán).
                    - Máximo 12 integrantes.
                    - Más de la mitad deben ser de Sistemas, IA, Ciberseguridad o Estadística.
                    - Ningún jugador puede pertenecer a dos teams.
                    """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Equipo creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Capitán o jugador no encontrado"),
            @ApiResponse(responseCode = "409",
                    description = "Nombre en uso, team lleno o jugador ya tiene team")
    })
    @PostMapping("/crear")
    public ResponseEntity<CreateTeamResponseDTO> crearEquipo(
            @RequestHeader("X-Capitan-ID") Long capitanId,
            @Valid @RequestBody CreateTeamRequestDTO request) {
        log.info("Creando team: '{}'", request.getNombreEquipo());
        return new ResponseEntity<>(
                teamService.crearEquipo(capitanId, request), HttpStatus.CREATED);
    }


    @Operation(summary = "Consultar team por ID",
            description = "Retorna la información completa de un team.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Equipo encontrado"),
            @ApiResponse(responseCode = "404", description = "Equipo no encontrado")
    })
    @GetMapping("/{equipoId}")
    public ResponseEntity<TeamResponseDTO> consultarEquipo(
            @PathVariable Long equipoId) {
        log.info("Consultando team ID: {}", equipoId);
        return ResponseEntity.ok(teamService.consultarEquipo(equipoId));
    }


    @Operation(summary = "Consultar teams de un torneo",
            description = "Retorna todos los teams inscritos en el torneo.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Equipos retornados exitosamente"),
            @ApiResponse(responseCode = "404", description = "Torneo no encontrado")
    })
    @GetMapping("/torneo/{tournamentId}")
    public ResponseEntity<List<TeamResponseDTO>> consultarEquiposPorTorneo(
            @PathVariable String tournamentId) {
        log.info("Consultando teams del torneo: {}", tournamentId);
        return ResponseEntity.ok(teamService.consultarEquiposPorTorneo(tournamentId));
    }


    @Operation(summary = "Consultar jugadores del team",
            description = "Lista todos los jugadores activos vinculados al team.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Jugadores retornados exitosamente"),
            @ApiResponse(responseCode = "404", description = "Equipo no encontrado")
    })
    @GetMapping("/{equipoId}/jugadores")
    public ResponseEntity<List<String>> consultarJugadores(
            @PathVariable Long equipoId) {
        log.info("Consultando jugadores del team ID: {}", equipoId);
        return ResponseEntity.ok(teamService.consultarJugadoresEquipo(equipoId));
    }


    @Operation(summary = "Actualizar datos del team",
            description = """
                    Solo el capitán puede actualizar su team.
                    Campos actualizables: nombre, escudo y colores del uniforme.
                    """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Equipo actualizado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "403", description = "Solo el capitán puede actualizar el team"),
            @ApiResponse(responseCode = "404", description = "Equipo no encontrado"),
            @ApiResponse(responseCode = "409", description = "El nombre ya está en uso")
    })
    @PutMapping("/{equipoId}")
    public ResponseEntity<TeamResponseDTO> actualizarEquipo(
            @PathVariable Long equipoId,
            @RequestHeader("X-Capitan-ID") Long capitanId,
            @Valid @RequestBody UpdateTeamRequestDTO request) {
        log.info("Actualizando team ID: {}", equipoId);
        return ResponseEntity.ok(
                teamService.actualizarEquipo(equipoId, capitanId, request));
    }


    @Operation(summary = "Eliminar team",
            description = """
                    Solo el capitán puede eliminar su team.
                    Al eliminarlo, todos los jugadores quedan liberados y disponibles.
                    """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Equipo eliminado exitosamente"),
            @ApiResponse(responseCode = "403", description = "Solo el capitán puede eliminar el team"),
            @ApiResponse(responseCode = "404", description = "Equipo no encontrado")
    })
    @DeleteMapping("/{equipoId}")
    public ResponseEntity<Void> eliminarEquipo(
            @PathVariable Long equipoId,
            @RequestHeader("X-Capitan-ID") Long capitanId) {
        log.info("Eliminando team ID: {}", equipoId);
        teamService.eliminarEquipo(equipoId, capitanId);
        return ResponseEntity.noContent().build();
    }
}