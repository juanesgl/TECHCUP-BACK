package edu.dosw.proyect.controllers;

import edu.dosw.proyect.controllers.dtos.response.BracketMatchDTO;
import edu.dosw.proyect.controllers.dtos.response.TournamentBracketResponseDTO;
import edu.dosw.proyect.core.services.KnockoutBracketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/tournaments/{tournamentId}/bracket")
@RequiredArgsConstructor
@Tag(name = "Organizador - Torneos",
        description = "Knockout bracket generation and consultation")
public class KnockoutBracketController {

    private final KnockoutBracketService bracketService;


    @Operation(
            summary = "Generar bracket eliminatorio del torneo",
            description = """
                    Genera el bracket de forma aleatoria con los equipos inscritos y aprobados.
                    Requisitos:
                    - Mínimo 4 equipos con estado de inscripción APROBADO.
                    - Con 8 equipos se generan cuartos, semifinales y final.
                    - Con 4-7 equipos se generan directamente semifinales y final.
                    Solo puede generarse una vez por torneo.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Bracket generado exitosamente"),
            @ApiResponse(responseCode = "404",
                    description = "Torneo no encontrado"),
            @ApiResponse(responseCode = "409",
                    description = "bracket ya generado o equipos insuficientes")
    })
    @PostMapping("/generate")
    public ResponseEntity<TournamentBracketResponseDTO> generateBracket(
            @PathVariable String tournamentId) {

        log.info("Generando bracket para torneo: {}", tournamentId);
        return ResponseEntity.ok(bracketService.generateBracket(tournamentId));
    }

    // -----------------------------------------------------------------------

    @Operation(
            summary = "Consultar bracket completo del torneo",
            description = "Retorna el estado actual de todas las llaves:" +
                    "cuartos de final, semifinales y final, con sus resultados."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Bracket retornado exitosamente"),
            @ApiResponse(responseCode = "404",
                    description = "Torneo no encontrado o bracket no generado aún")
    })
    @GetMapping
    public ResponseEntity<TournamentBracketResponseDTO> getBracket(
            @PathVariable String tournamentId) {

        log.info("Consultando bracket del torneo: {}", tournamentId);
        return ResponseEntity.ok(bracketService.getBracket(tournamentId));
    }


    @Operation(
            summary = "Consultar llaves de una fase específica",
            description = "Retorna solo las llaves de la fase solicitada: " +
                    "CUARTOS, SEMIFINAL o FINAL."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Llaves retornadas exitosamente"),
            @ApiResponse(responseCode = "404",
                    description = "Torneo no encontrado")
    })
    @GetMapping("/phase/{phase}")
    public ResponseEntity<List<BracketMatchDTO>> getPhase(
            @PathVariable String tournamentId,
            @PathVariable String phase) {

        log.info("Consultando fase {} del torneo {}", phase, tournamentId);
        return ResponseEntity.ok(bracketService.getPhase(tournamentId, phase));
    }

    // -----------------------------------------------------------------------

    @Operation(
            summary = "Avanzar bracket tras resultado de un partido",
            description = """
                    Promueve al ganador de una llave a la siguiente fase.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Bracket actualizado exitosamente"),
            @ApiResponse(responseCode = "404",
                    description = "Partido no encontrado"),
            @ApiResponse(responseCode = "409",
                    description = "El partido aún no está FINALIZADO")
    })
    @PostMapping("/advance/{matchId}")
    public ResponseEntity<String> advanceBracket(
            @PathVariable String tournamentId,
            @PathVariable Long matchId) {

        log.info("Avanzando bracket — torneo: {}, partido: {}", tournamentId, matchId);
        bracketService.advanceBracket(matchId);
        return ResponseEntity.ok("Bracket actualizado exitosamente");
    }
}