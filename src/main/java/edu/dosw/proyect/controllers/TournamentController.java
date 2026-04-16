package edu.dosw.proyect.controllers;

import edu.dosw.proyect.core.exceptions.TournamentException;
import edu.dosw.proyect.core.models.Tournament;
import edu.dosw.proyect.controllers.dtos.request.TournamentRequest;
import edu.dosw.proyect.controllers.dtos.response.TournamentResponse;
import edu.dosw.proyect.core.services.TournamentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/tournaments")
@Tag(name = "03 Organizador Torneos")
public class TournamentController {

    private final TournamentService tournamentService;

    public TournamentController(TournamentService tournamentService) {
        this.tournamentService = tournamentService;
    }

    @Operation(summary = "Crear un nuevo torneo", description = "Instancia un torneo vacios con las reglas base requeridas")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Torneo creado exitosamente")})
    @PostMapping
    public ResponseEntity<TournamentResponse> createTournament(@RequestBody TournamentRequest request) {
        return ResponseEntity.ok(tournamentService.createTournament(request));
    }

    @Operation(summary = "Listar torneos", description = "Devuelve el historial completo de torneos inscritos en el sistema")
    @GetMapping
    public ResponseEntity<List<Tournament>> getAllTournaments() {
        return ResponseEntity.ok(tournamentService.getAllTournaments());
    }

    @Operation(summary = "Iniciar un torneo", description = "Abre un torneo. Falla si ya estaba en curso o si las reglas se violan.")
    @PutMapping("/{id}/start")
    public ResponseEntity<?> startTournament(@PathVariable String id) {
        try {
            return ResponseEntity.ok(tournamentService.startTournament(id));
        } catch (TournamentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Finalizar un torneo", description = "Cierra el torneo actual tras la final y consolida metricas")
    @PutMapping("/{id}/finish")
    public ResponseEntity<?> finishTournament(@PathVariable String id) {
        try {
            return ResponseEntity.ok(tournamentService.finishTournament(id));
        } catch (TournamentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
