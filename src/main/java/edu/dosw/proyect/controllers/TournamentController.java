package edu.dosw.proyect.controllers;

import edu.dosw.proyect.exceptions.TournamentException;
import edu.dosw.proyect.models.Tournament;
import edu.dosw.proyect.models.TournamentRequest;
import edu.dosw.proyect.models.TournamentResponse;
import edu.dosw.proyect.services.TournamentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tournaments")
public class TournamentController {

    private final TournamentService tournamentService;

    public TournamentController(TournamentService tournamentService) {
        this.tournamentService = tournamentService;
    }

    @PostMapping
    public ResponseEntity<TournamentResponse> createTournament(@RequestBody TournamentRequest request) {
        return ResponseEntity.ok(tournamentService.createTournament(request));
    }

    @GetMapping
    public ResponseEntity<List<Tournament>> getAllTournaments() {
        return ResponseEntity.ok(tournamentService.getAllTournaments());
    }

    @PutMapping("/{id}/start")
    public ResponseEntity<?> startTournament(@PathVariable String id) {
        try {
            return ResponseEntity.ok(tournamentService.startTournament(id));
        } catch (TournamentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/finish")
    public ResponseEntity<?> finishTournament(@PathVariable String id) {
        try {
            return ResponseEntity.ok(tournamentService.finishTournament(id));
        } catch (TournamentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}