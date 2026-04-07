package edu.dosw.proyect.services;

import edu.dosw.proyect.controllers.dtos.request.TournamentRequest;
import edu.dosw.proyect.controllers.dtos.response.TournamentResponse;
import edu.dosw.proyect.core.exceptions.TournamentException;
import edu.dosw.proyect.core.models.Tournament;
import edu.dosw.proyect.core.models.enums.TournamentsStatus;
import edu.dosw.proyect.core.services.impl.TournamentServiceImpl;
import edu.dosw.proyect.persistence.entity.TournamentEntity;
import edu.dosw.proyect.persistence.mapper.TournamentPersistenceMapper;
import edu.dosw.proyect.persistence.repository.TournamentRepository;
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

    @Mock
    private TournamentPersistenceMapper tournamentMapper;

    @InjectMocks
    private TournamentServiceImpl tournamentService;

    private TournamentEntity buildEntity(String tournId, TournamentsStatus status) {
        TournamentEntity e = new TournamentEntity();
        e.setId(1L);
        e.setTournId(tournId);
        e.setName("TechCup 2026");
        e.setStatus(status);
        e.setStartDate(LocalDate.now());
        e.setEndDate(LocalDate.now().plusMonths(3));
        return e;
    }

    private Tournament buildDomain(String tournId, TournamentsStatus status) {
        Tournament t = new Tournament();
        t.setId(1L);
        t.setTournId(tournId);
        t.setName("TechCup 2026");
        t.setStatus(status);
        return t;
    }

    @Test
    void createTournament_HappyPath_RetornaTournamentResponse() {
        TournamentRequest request = new TournamentRequest(
                "TechCup 2026", LocalDate.now(),
                LocalDate.now().plusMonths(3), 8, 150000, "Reglamento");

        TournamentEntity entity = buildEntity("TOURN-1", TournamentsStatus.DRAFT);
        Tournament domain = buildDomain("TOURN-1", TournamentsStatus.DRAFT);

        when(tournamentMapper.toEntity(any())).thenReturn(entity);
        when(tournamentRepository.save(any())).thenReturn(entity);
        when(tournamentMapper.toDomain(entity)).thenReturn(domain);

        TournamentResponse response = tournamentService.createTournament(request);

        assertNotNull(response);
        assertEquals("TOURN-1", response.tournId());
        verify(tournamentRepository, times(1)).save(any());
    }

    @Test
    void getAllTournaments_RetornaLista() {
        TournamentEntity entity = buildEntity("TOURN-1", TournamentsStatus.DRAFT);
        Tournament domain = buildDomain("TOURN-1", TournamentsStatus.DRAFT);

        when(tournamentRepository.findAll()).thenReturn(List.of(entity));
        when(tournamentMapper.toDomain(entity)).thenReturn(domain);

        List<Tournament> result = tournamentService.getAllTournaments();

        assertEquals(1, result.size());
    }

    @Test
    void getTournamentById_HappyPath_RetornaTorneo() {
        TournamentEntity entity = buildEntity("TOURN-1", TournamentsStatus.DRAFT);
        Tournament domain = buildDomain("TOURN-1", TournamentsStatus.DRAFT);

        when(tournamentRepository.findByTournId("TOURN-1")).thenReturn(Optional.of(entity));
        when(tournamentMapper.toDomain(entity)).thenReturn(domain);

        Tournament result = tournamentService.getTournamentById("TOURN-1");

        assertNotNull(result);
        assertEquals("TOURN-1", result.getTournId());
    }

    @Test
    void getTournamentById_NoExiste_LanzaException() {
        when(tournamentRepository.findByTournId("TOURN-999")).thenReturn(Optional.empty());
        assertThrows(TournamentException.class,
                () -> tournamentService.getTournamentById("TOURN-999"));
    }

    @Test
    void startTournament_HappyPath_RetornaInProgress() {
        TournamentEntity entity = buildEntity("TOURN-1", TournamentsStatus.DRAFT);
        Tournament domain = buildDomain("TOURN-1", TournamentsStatus.IN_PROGRESS);

        when(tournamentRepository.findByTournId("TOURN-1")).thenReturn(Optional.of(entity));
        when(tournamentRepository.save(any())).thenReturn(entity);
        when(tournamentMapper.toDomain(entity)).thenReturn(domain);

        TournamentResponse response = tournamentService.startTournament("TOURN-1");

        assertNotNull(response);
        verify(tournamentRepository, times(1)).save(entity);
    }

    @Test
    void startTournament_YaIniciado_LanzaException() {
        TournamentEntity entity = buildEntity("TOURN-1", TournamentsStatus.IN_PROGRESS);
        when(tournamentRepository.findByTournId("TOURN-1")).thenReturn(Optional.of(entity));

        assertThrows(TournamentException.class,
                () -> tournamentService.startTournament("TOURN-1"));
    }

    @Test
    void finishTournament_HappyPath_RetornaFinished() {
        TournamentEntity entity = buildEntity("TOURN-1", TournamentsStatus.IN_PROGRESS);
        Tournament domain = buildDomain("TOURN-1", TournamentsStatus.FINISHED);

        when(tournamentRepository.findByTournId("TOURN-1")).thenReturn(Optional.of(entity));
        when(tournamentRepository.save(any())).thenReturn(entity);
        when(tournamentMapper.toDomain(entity)).thenReturn(domain);

        TournamentResponse response = tournamentService.finishTournament("TOURN-1");

        assertNotNull(response);
        verify(tournamentRepository, times(1)).save(entity);
    }

    @Test
    void finishTournament_NoInProgress_LanzaException() {
        TournamentEntity entity = buildEntity("TOURN-1", TournamentsStatus.DRAFT);
        when(tournamentRepository.findByTournId("TOURN-1")).thenReturn(Optional.of(entity));

        assertThrows(TournamentException.class,
                () -> tournamentService.finishTournament("TOURN-1"));
    }
}