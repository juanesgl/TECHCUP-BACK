package edu.dosw.proyect.controllers;

import edu.dosw.proyect.core.exceptions.TournamentException;
import edu.dosw.proyect.core.models.Tournament;
import edu.dosw.proyect.core.models.TournamentRequest;
import edu.dosw.proyect.core.models.TournamentResponse;
import edu.dosw.proyect.core.models.enums.TournamentsStatus;
import edu.dosw.proyect.core.services.TournamentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TournamentControllerTest {

    private TournamentController tournamentController;
    private TournamentService tournamentService;

    @BeforeEach
    void setUp() {
        tournamentService = mock(TournamentService.class);
        tournamentController = new TournamentController(tournamentService);
    }

    @Test
    void createTournament_ShouldReturnOk() {
        TournamentRequest request = new TournamentRequest("Test", LocalDate.now(), LocalDate.now(), 10, 50, "Rules");
        TournamentResponse response = new TournamentResponse("ID-1", "Test", TournamentsStatus.DRAFT, "Created");
        when(tournamentService.createTournament(any(TournamentRequest.class))).thenReturn(response);

        ResponseEntity<TournamentResponse> result = (ResponseEntity<TournamentResponse>) tournamentController
                .createTournament(request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("ID-1", result.getBody().turnId());
    }

    @Test
    void getAllTournaments_ShouldReturnList() {
        Tournament t = new Tournament();
        t.setTournId("ID-1");
        t.setName("Test");
        t.setStartDate(LocalDate.now());
        t.setEndDate(LocalDate.now());
        t.setMaxTeams(10);
        t.setCostPerTeam(50);
        t.setStatus(TournamentsStatus.DRAFT);
        t.setRegulation("Rules");
        when(tournamentService.getAllTournaments()).thenReturn(List.of(t));

        ResponseEntity<List<Tournament>> result = (ResponseEntity<List<Tournament>>) tournamentController
                .getAllTournaments();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(1, result.getBody().size());
    }

    @Test
    void startTournament_ShouldReturnOkOnSuccess() {
        TournamentResponse response = new TournamentResponse("ID-1", "Test", TournamentsStatus.IN_PROGRESS, "Started");
        when(tournamentService.startTournament("ID-1")).thenReturn(response);

        ResponseEntity<?> result = tournamentController.startTournament("ID-1");

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    void startTournament_ShouldReturnBadRequestOnException() {
        when(tournamentService.startTournament("ID-1")).thenThrow(new TournamentException("Error"));

        ResponseEntity<?> result = tournamentController.startTournament("ID-1");

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("Error", result.getBody());
    }

    @Test
    void finishTournament_ShouldReturnOkOnSuccess() {
        TournamentResponse response = new TournamentResponse("ID-1", "Test", TournamentsStatus.FINISHED, "Finished");
        when(tournamentService.finishTournament("ID-1")).thenReturn(response);

        ResponseEntity<?> result = tournamentController.finishTournament("ID-1");

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    void finishTournament_ShouldReturnBadRequestOnException() {
        when(tournamentService.finishTournament("ID-1")).thenThrow(new TournamentException("Error"));

        ResponseEntity<?> result = tournamentController.finishTournament("ID-1");

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("Error", result.getBody());
    }
}

