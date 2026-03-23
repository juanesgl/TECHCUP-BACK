package edu.dosw.proyect.services;

import edu.dosw.proyect.core.exceptions.TournamentException;
import edu.dosw.proyect.core.models.Tournament;
import edu.dosw.proyect.core.models.TournamentRequest;
import edu.dosw.proyect.core.models.TournamentResponse;
import edu.dosw.proyect.core.models.enums.TournamentsStatus;
import edu.dosw.proyect.core.services.TournamentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TournamentServiceTest {

    private TournamentService tournamentService;

    @BeforeEach
    void setUp() {
        tournamentService = new TournamentService();
    }

    @Test
    void createTournament_ShouldReturnResponse() {
        TournamentRequest request = new TournamentRequest(
                "Copa Mundial",
                LocalDate.now(),
                LocalDate.now().plusDays(30),
                32,
                1000.0,
                "Reglas FIFA"
        );

        TournamentResponse response = tournamentService.createTournament(request);

        assertNotNull(response.turnId());
        assertEquals("Copa Mundial", response.name());
        assertEquals(TournamentsStatus.DRAFT, response.status());
        assertEquals("Tournament successfully created", response.message());
        
        List<Tournament> tournaments = tournamentService.getAllTournaments();
        assertEquals(1, tournaments.size());
        assertEquals(response.turnId(), tournaments.get(0).getTournId());
    }

    @Test
    void getAllTournaments_ShouldReturnEmptyListInitially() {
        List<Tournament> tournaments = tournamentService.getAllTournaments();
        assertTrue(tournaments.isEmpty());
    }

    @Test
    void startTournament_ShouldChangeStatusToInProgress() {
        TournamentRequest request = new TournamentRequest("Test", LocalDate.now(), LocalDate.now(), 8, 100, "Reg");
        TournamentResponse created = tournamentService.createTournament(request);

        TournamentResponse started = tournamentService.startTournament(created.turnId());

        assertEquals(TournamentsStatus.IN_PROGRESS, started.status());
        assertEquals("The tournament is now IN PROGRESS", started.message());
    }

    @Test
    void startTournament_ShouldThrowExceptionIfNotFound() {
        assertThrows(TournamentException.class, () -> tournamentService.startTournament("NON-EXISTENT"));
    }

    @Test
    void startTournament_ShouldThrowExceptionIfAlreadyStartedOrFinished() {
        TournamentRequest request = new TournamentRequest("Test", LocalDate.now(), LocalDate.now(), 8, 100, "Reg");
        TournamentResponse created = tournamentService.createTournament(request);
        
        tournamentService.startTournament(created.turnId());

        assertThrows(TournamentException.class, () -> tournamentService.startTournament(created.turnId()));
        
        tournamentService.finishTournament(created.turnId());

        assertThrows(TournamentException.class, () -> tournamentService.startTournament(created.turnId()));
    }

    @Test
    void finishTournament_ShouldChangeStatusToFinished() {
        TournamentRequest request = new TournamentRequest("Test", LocalDate.now(), LocalDate.now(), 8, 100, "Reg");
        TournamentResponse created = tournamentService.createTournament(request);
        tournamentService.startTournament(created.turnId());

        TournamentResponse finished = tournamentService.finishTournament(created.turnId());

        assertEquals(TournamentsStatus.FINISHED, finished.status());
        assertEquals("The tournament has been successfully FINISHED", finished.message());
    }

    @Test
    void finishTournament_ShouldThrowExceptionIfNotFound() {
        assertThrows(TournamentException.class, () -> tournamentService.finishTournament("NON-EXISTENT"));
    }

    @Test
    void finishTournament_ShouldThrowExceptionIfNotInProgress() {
        TournamentRequest request = new TournamentRequest("Test", LocalDate.now(), LocalDate.now(), 8, 100, "Reg");
        TournamentResponse created = tournamentService.createTournament(request);

        assertThrows(TournamentException.class, () -> tournamentService.finishTournament(created.turnId()));
    }
}
