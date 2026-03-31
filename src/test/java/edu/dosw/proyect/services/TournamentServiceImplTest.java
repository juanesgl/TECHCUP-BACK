package edu.dosw.proyect.services;

import edu.dosw.proyect.controllers.dtos.response.TournamentResponse;
import edu.dosw.proyect.core.exceptions.TournamentException;
import edu.dosw.proyect.core.models.Tournament;
import edu.dosw.proyect.controllers.dtos.request.TournamentRequest;
import edu.dosw.proyect.core.models.enums.TournamentsStatus;
import edu.dosw.proyect.persistence.repository.TournamentRepository;
import edu.dosw.proyect.core.services.impl.TournamentServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TournamentServiceImplTest {

    @Mock
    private TournamentRepository tournamentRepository;

    @InjectMocks
    private TournamentServiceImpl tournamentService;

    private Tournament buildTournament(String id, TournamentsStatus status) {
        Tournament t = new Tournament();
        t.setTournId(id);
        t.setName("TechCup 2026");
        t.setStatus(status);
        t.setMaxTeams(8);
        t.setCostPerTeam(50000);
        return t;
    }

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


    @Test
    void createTournament_HappyPath_RetornaDraft() {
        TournamentRequest request = buildRequest();
        Tournament saved = buildTournament("TOURN-1", TournamentsStatus.DRAFT);

        when(tournamentRepository.save(any())).thenReturn(saved);

        TournamentResponse result = tournamentService.createTournament(request);

        assertNotNull(result);
        assertEquals(TournamentsStatus.DRAFT, result.status());
        verify(tournamentRepository, times(1)).save(any());
    }


    @Test
    void getAllTournaments_HappyPath_RetornaLista() {
        List<Tournament> torneos = List.of(
                buildTournament("TOURN-1", TournamentsStatus.DRAFT),
                buildTournament("TOURN-2", TournamentsStatus.IN_PROGRESS)
        );

        when(tournamentRepository.findAll()).thenReturn(torneos);

        List<Tournament> result = tournamentService.getAllTournaments();

        assertEquals(2, result.size());
        verify(tournamentRepository, times(1)).findAll();
    }

    @Test
    void getAllTournaments_ListaVacia_RetornaVacia() {
        when(tournamentRepository.findAll()).thenReturn(List.of());

        List<Tournament> result = tournamentService.getAllTournaments();

        assertTrue(result.isEmpty());
    }


    @Test
    void startTournament_HappyPath_CambiaEstadoAInProgress() {
        Tournament t = buildTournament("TOURN-1", TournamentsStatus.DRAFT);

        when(tournamentRepository.findByTournId("TOURN-1")).thenReturn(Optional.of(t));
        when(tournamentRepository.save(any())).thenReturn(t);

        TournamentResponse result = tournamentService.startTournament("TOURN-1");

        assertEquals(TournamentsStatus.IN_PROGRESS, result.status());
        verify(tournamentRepository, times(1)).save(any());
    }

    @Test
    void startTournament_YaEnProgreso_LanzaTournamentException() {
        Tournament t = buildTournament("TOURN-1", TournamentsStatus.IN_PROGRESS);

        when(tournamentRepository.findByTournId("TOURN-1")).thenReturn(Optional.of(t));

        assertThrows(TournamentException.class,
                () -> tournamentService.startTournament("TOURN-1"));
        verify(tournamentRepository, never()).save(any());
    }

    @Test
    void startTournament_YaFinalizado_LanzaTournamentException() {
        Tournament t = buildTournament("TOURN-1", TournamentsStatus.FINISHED);

        when(tournamentRepository.findByTournId("TOURN-1")).thenReturn(Optional.of(t));

        assertThrows(TournamentException.class,
                () -> tournamentService.startTournament("TOURN-1"));
    }

    @Test
    void startTournament_NoEncontrado_LanzaTournamentException() {
        when(tournamentRepository.findByTournId("TOURN-999")).thenReturn(Optional.empty());

        assertThrows(TournamentException.class,
                () -> tournamentService.startTournament("TOURN-999"));
    }


    @Test
    void finishTournament_HappyPath_CambiaEstadoAFinished() {
        Tournament t = buildTournament("TOURN-1", TournamentsStatus.IN_PROGRESS);

        when(tournamentRepository.findByTournId("TOURN-1")).thenReturn(Optional.of(t));
        when(tournamentRepository.save(any())).thenReturn(t);

        TournamentResponse result = tournamentService.finishTournament("TOURN-1");

        assertEquals(TournamentsStatus.FINISHED, result.status());
        verify(tournamentRepository, times(1)).save(any());
    }

    @Test
    void finishTournament_NoEnProgreso_LanzaTournamentException() {
        Tournament t = buildTournament("TOURN-1", TournamentsStatus.DRAFT);

        when(tournamentRepository.findByTournId("TOURN-1")).thenReturn(Optional.of(t));

        assertThrows(TournamentException.class,
                () -> tournamentService.finishTournament("TOURN-1"));
        verify(tournamentRepository, never()).save(any());
    }

    @Test
    void finishTournament_NoEncontrado_LanzaTournamentException() {
        when(tournamentRepository.findByTournId("TOURN-999")).thenReturn(Optional.empty());

        assertThrows(TournamentException.class,
                () -> tournamentService.finishTournament("TOURN-999"));
    }
}