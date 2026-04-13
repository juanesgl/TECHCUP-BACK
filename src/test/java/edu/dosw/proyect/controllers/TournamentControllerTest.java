package edu.dosw.proyect.controllers;

import edu.dosw.proyect.core.exceptions.TournamentException;
import edu.dosw.proyect.core.models.Tournament;
import edu.dosw.proyect.controllers.dtos.request.TournamentRequest;
import edu.dosw.proyect.controllers.dtos.response.TournamentResponse;
import edu.dosw.proyect.core.models.enums.TournamentsStatus;
import edu.dosw.proyect.core.services.TournamentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TournamentControllerTest {

    @Mock
    private TournamentService tournamentService;

    @InjectMocks
    private TournamentController tournamentController;

    private TournamentRequest buildRequest() {
        return new TournamentRequest(
                "TechCup 2026",
                LocalDate.now(),
                LocalDate.now().plusMonths(2),
                8,
                50000,
                "Reglamento general"
        );
    }

    private TournamentResponse buildResponse(String id, TournamentsStatus status) {
        return new TournamentResponse(id, "TechCup 2026", status, "OK");
    }


    @Test
    void createTournament_HappyPath_RetornaOk() {
        TournamentRequest request = buildRequest();
        TournamentResponse response = buildResponse("TOURN-1", TournamentsStatus.DRAFT);

        when(tournamentService.createTournament(request)).thenReturn(response);

        ResponseEntity<TournamentResponse> result =
                tournamentController.createTournament(request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals("TOURN-1", result.getBody().tournId()); // ✅ record usa método sin get
        verify(tournamentService, times(1)).createTournament(request);
    }
    @Test
    void getAllTournaments_HappyPath_RetornaLista() {
        Tournament t1 = new Tournament();
        t1.setTournId("TOURN-1");
        Tournament t2 = new Tournament();
        t2.setTournId("TOURN-2");

        when(tournamentService.getAllTournaments()).thenReturn(List.of(t1, t2));

        ResponseEntity<List<Tournament>> result =
                tournamentController.getAllTournaments();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(2, result.getBody().size());
    }

    @Test
    void getAllTournaments_ListaVacia_RetornaListaVacia() {
        when(tournamentService.getAllTournaments()).thenReturn(List.of());

        ResponseEntity<List<Tournament>> result =
                tournamentController.getAllTournaments();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertTrue(result.getBody().isEmpty());
    }

    @Test
    void startTournament_HappyPath_RetornaOk() {
        TournamentResponse response = buildResponse(
                "TOURN-1", TournamentsStatus.IN_PROGRESS);

        when(tournamentService.startTournament("TOURN-1")).thenReturn(response);

        ResponseEntity<?> result = tournamentController.startTournament("TOURN-1");

        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(tournamentService, times(1)).startTournament("TOURN-1");
    }

    @Test
    void startTournament_Error_RetornaBadRequest() {
        when(tournamentService.startTournament("TOURN-1"))
                .thenThrow(new TournamentException("Ya iniciado"));

        ResponseEntity<?> result = tournamentController.startTournament("TOURN-1");

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("Ya iniciado", result.getBody());
    }

    @Test
    void finishTournament_HappyPath_RetornaOk() {
        TournamentResponse response = buildResponse(
                "TOURN-1", TournamentsStatus.FINISHED);

        when(tournamentService.finishTournament("TOURN-1")).thenReturn(response);

        ResponseEntity<?> result = tournamentController.finishTournament("TOURN-1");

        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(tournamentService, times(1)).finishTournament("TOURN-1");
    }

    @Test
    void finishTournament_Error_RetornaBadRequest() {
        when(tournamentService.finishTournament("TOURN-1"))
                .thenThrow(new TournamentException("No en progreso"));

        ResponseEntity<?> result = tournamentController.finishTournament("TOURN-1");

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("No en progreso", result.getBody());
    }
}