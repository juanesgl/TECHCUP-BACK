package edu.dosw.proyect.services;

import edu.dosw.proyect.core.exceptions.TournamentException;
import edu.dosw.proyect.core.models.Tournament;
import edu.dosw.proyect.core.models.TournamentRequest;
import edu.dosw.proyect.core.models.TournamentResponse;
import edu.dosw.proyect.core.models.enums.TournamentsStatus;
import edu.dosw.proyect.core.repositories.TournamentRepository;
import edu.dosw.proyect.core.services.TournamentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;

class TournamentServiceTest {

    private TournamentService tournamentService;
    private TournamentRepository tournamentRepository;

    @BeforeEach
    void setUp() {
        tournamentRepository = mock(TournamentRepository.class);
        tournamentService = new TournamentService(tournamentRepository);
    }

    @Test
    void createTournament_ShouldReturnResponse() {
        TournamentRequest request = new TournamentRequest(
                "Copa Mundial",
                LocalDate.now(),
                LocalDate.now().plusDays(30),
                32,
                1000.0,
                "Reglas FIFA");

        when(tournamentRepository.save(any(Tournament.class))).thenAnswer(invocation -> {
            Tournament t = invocation.getArgument(0);
            if (t.getTournId() == null)
                t.setTournId("T123");
            return t;
        });

        TournamentResponse response = tournamentService.createTournament(request);

        assertNotNull(response.turnId());
        assertEquals("Copa Mundial", response.name());
        assertEquals(TournamentsStatus.DRAFT, response.status());
        assertEquals("Tournament successfully created", response.message());

        Tournament t = new Tournament();
        t.setTournId(response.turnId());
        when(tournamentRepository.findAll()).thenReturn(java.util.List.of(t));
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
        when(tournamentRepository.save(any(Tournament.class))).thenAnswer(invocation -> {
            Tournament t = invocation.getArgument(0);
            if (t.getTournId() == null)
                t.setTournId("T123");
            return t;
        });
        TournamentResponse created = tournamentService.createTournament(request);

        Tournament t = new Tournament();
        t.setTournId(created.turnId());
        t.setName("Test");
        t.setStatus(TournamentsStatus.DRAFT);
        when(tournamentRepository.findByTournId(created.turnId())).thenReturn(java.util.Optional.of(t));

        TournamentResponse started = tournamentService.startTournament(created.turnId());

        assertEquals(TournamentsStatus.IN_PROGRESS, started.status());
        assertEquals("The tournament is now IN PROGRESS", started.message());
    }

    @Test
    void startTournament_ShouldThrowExceptionIfNotFound() {
        when(tournamentRepository.findByTournId("NON-EXISTENT")).thenReturn(java.util.Optional.empty());
        assertThrows(TournamentException.class, () -> tournamentService.startTournament("NON-EXISTENT"));
    }

    @Test
    void startTournament_ShouldThrowExceptionIfAlreadyStartedOrFinished() {
        TournamentRequest request = new TournamentRequest("Test", LocalDate.now(), LocalDate.now(), 8, 100, "Reg");
        when(tournamentRepository.save(any(Tournament.class))).thenAnswer(invocation -> {
            Tournament t = invocation.getArgument(0);
            if (t.getTournId() == null)
                t.setTournId("T123");
            return t;
        });
        TournamentResponse created = tournamentService.createTournament(request);

        Tournament t1 = new Tournament();
        t1.setTournId(created.turnId());
        t1.setStatus(TournamentsStatus.DRAFT);
        when(tournamentRepository.findByTournId(created.turnId())).thenReturn(java.util.Optional.of(t1));

        tournamentService.startTournament(created.turnId());

        t1.setStatus(TournamentsStatus.IN_PROGRESS);
        assertThrows(TournamentException.class, () -> tournamentService.startTournament(created.turnId()));

        t1.setStatus(TournamentsStatus.FINISHED);
        assertThrows(TournamentException.class, () -> tournamentService.startTournament(created.turnId()));
    }

    @Test
    void finishTournament_ShouldChangeStatusToFinished() {
        TournamentRequest request = new TournamentRequest("Test", LocalDate.now(), LocalDate.now(), 8, 100, "Reg");
        when(tournamentRepository.save(any(Tournament.class))).thenAnswer(invocation -> {
            Tournament t = invocation.getArgument(0);
            if (t.getTournId() == null)
                t.setTournId("T123");
            return t;
        });
        TournamentResponse created = tournamentService.createTournament(request);

        Tournament t = new Tournament();
        t.setTournId(created.turnId());
        t.setStatus(TournamentsStatus.IN_PROGRESS);
        when(tournamentRepository.findByTournId(created.turnId())).thenReturn(java.util.Optional.of(t));

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
        when(tournamentRepository.save(any(Tournament.class))).thenAnswer(invocation -> {
            Tournament t = invocation.getArgument(0);
            if (t.getTournId() == null)
                t.setTournId("T123");
            return t;
        });
        TournamentResponse created = tournamentService.createTournament(request);

        Tournament t = new Tournament();
        t.setTournId(created.turnId());
        t.setStatus(TournamentsStatus.DRAFT);
        when(tournamentRepository.findByTournId(created.turnId())).thenReturn(java.util.Optional.of(t));

        assertThrows(TournamentException.class, () -> tournamentService.finishTournament(created.turnId()));
    }
}
