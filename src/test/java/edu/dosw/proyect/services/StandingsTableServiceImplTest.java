package edu.dosw.proyect.services;

import edu.dosw.proyect.controllers.dtos.RegisterMatchResultRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.RegisterMatchResultResponseDTO;
import edu.dosw.proyect.controllers.dtos.response.StandingsTableResponseDTO;
import edu.dosw.proyect.controllers.dtos.response.TeamStandingDTO;
import edu.dosw.proyect.controllers.mappers.StandingsTableMapper;
import edu.dosw.proyect.core.exceptions.BusinessRuleException;
import edu.dosw.proyect.core.exceptions.ResourceNotFoundException;
import edu.dosw.proyect.core.models.Equipo;
import edu.dosw.proyect.core.models.Partido;
import edu.dosw.proyect.core.models.Tournament;
import edu.dosw.proyect.core.models.enums.MatchStatus;
import edu.dosw.proyect.core.repositories.EstadisticaEquipoRepository;
import edu.dosw.proyect.core.repositories.PartidoRepository;
import edu.dosw.proyect.core.repositories.TournamentRepository;
import edu.dosw.proyect.core.services.impl.StandingsTableServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StandingsTableServiceImplTest {

    @Mock
    private PartidoRepository matchRepository;

    @Mock
    private EstadisticaEquipoRepository statsRepository;

    @Mock
    private TournamentRepository tournamentRepository;

    @Spy
    private StandingsTableMapper standingsMapper;

    @InjectMocks
    private StandingsTableServiceImpl service;

    private Tournament tournament;
    private Equipo teamA;
    private Equipo teamB;
    private Equipo teamC;

    @BeforeEach
    void setUp() {
        tournament = new Tournament();
        tournament.setTournId("T-001");
        tournament.setName("TechCup League");

        teamA = Equipo.builder().id(1L).nombre("Alpha FC").build();
        teamB = Equipo.builder().id(2L).nombre("Beta FC").build();
        teamC = Equipo.builder().id(3L).nombre("Cyber Sec").build();
    }

    @Test
    void registerResult_HomeWin_SetsFinishedAndReturnsHome() {
        Partido match = match(10L, teamA, teamB, MatchStatus.PROGRAMADO);
        when(matchRepository.findById(10L)).thenReturn(Optional.of(match));
        when(matchRepository.save(any())).thenReturn(match);
        when(statsRepository.findByEquipoIdAndTorneoId(any(), any())).thenReturn(Optional.empty());

        RegisterMatchResultResponseDTO resp = service.registerResult(10L, new RegisterMatchResultRequestDTO(3, 1));

        assertEquals("HOME", resp.getOutcome());
        assertEquals(3, resp.getHomeGoals());
        assertEquals(1, resp.getAwayGoals());
        assertEquals("Alpha FC", resp.getHomeTeam());
        assertEquals("Beta FC", resp.getAwayTeam());
        assertEquals(MatchStatus.FINALIZADO, match.getEstado());
        verify(matchRepository).save(match);
        verify(statsRepository, times(2)).save(any());
    }

    @Test
    void registerResult_AwayWin_ReturnsAway() {
        Partido match = match(11L, teamA, teamB, MatchStatus.PROGRAMADO);
        when(matchRepository.findById(11L)).thenReturn(Optional.of(match));
        when(matchRepository.save(any())).thenReturn(match);
        when(statsRepository.findByEquipoIdAndTorneoId(any(), any())).thenReturn(Optional.empty());

        RegisterMatchResultResponseDTO resp = service.registerResult(11L, new RegisterMatchResultRequestDTO(0, 2));

        assertEquals("AWAY", resp.getOutcome());
    }

    @Test
    void registerResult_Draw_ReturnsDraw() {
        Partido match = match(12L, teamA, teamB, MatchStatus.PROGRAMADO);
        when(matchRepository.findById(12L)).thenReturn(Optional.of(match));
        when(matchRepository.save(any())).thenReturn(match);
        when(statsRepository.findByEquipoIdAndTorneoId(any(), any())).thenReturn(Optional.empty());

        RegisterMatchResultResponseDTO resp = service.registerResult(12L, new RegisterMatchResultRequestDTO(1, 1));

        assertEquals("DRAW", resp.getOutcome());
    }

    @Test
    void registerResult_MatchInProgress_AllowsUpdate() {
        Partido match = match(13L, teamA, teamB, MatchStatus.EN_JUEGO);
        when(matchRepository.findById(13L)).thenReturn(Optional.of(match));
        when(matchRepository.save(any())).thenReturn(match);
        when(statsRepository.findByEquipoIdAndTorneoId(any(), any())).thenReturn(Optional.empty());

        RegisterMatchResultResponseDTO resp = service.registerResult(13L, new RegisterMatchResultRequestDTO(2, 2));

        assertEquals("DRAW", resp.getOutcome());
        assertEquals(MatchStatus.FINALIZADO, match.getEstado());
    }

    @Test
    void registerResult_MatchNotFound_ThrowsResourceNotFoundException() {
        when(matchRepository.findById(99L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> service.registerResult(99L, new RegisterMatchResultRequestDTO(1, 0)));

        assertTrue(ex.getMessage().contains("99"));
    }

    @Test
    void registerResult_CancelledMatch_ThrowsBusinessRuleException() {
        Partido match = match(14L, teamA, teamB, MatchStatus.CANCELADO);
        when(matchRepository.findById(14L)).thenReturn(Optional.of(match));

        BusinessRuleException ex = assertThrows(BusinessRuleException.class,
                () -> service.registerResult(14L, new RegisterMatchResultRequestDTO(1, 0)));

        assertTrue(ex.getMessage().contains("CANCELLED"));
        verify(matchRepository, never()).save(any());
    }

    @Test
    void registerResult_AlreadyFinishedMatch_ThrowsBusinessRuleException() {
        Partido match = match(15L, teamA, teamB, MatchStatus.FINALIZADO);
        when(matchRepository.findById(15L)).thenReturn(Optional.of(match));

        BusinessRuleException ex = assertThrows(BusinessRuleException.class,
                () -> service.registerResult(15L, new RegisterMatchResultRequestDTO(2, 1)));

        assertTrue(ex.getMessage().contains("FINISHED"));
        verify(matchRepository, never()).save(any());
    }

    @Test
    void registerResult_ResponseContainsStandingsMessage() {
        Partido match = match(16L, teamA, teamB, MatchStatus.PROGRAMADO);
        when(matchRepository.findById(16L)).thenReturn(Optional.of(match));
        when(matchRepository.save(any())).thenReturn(match);
        when(statsRepository.findByEquipoIdAndTorneoId(any(), any())).thenReturn(Optional.empty());

        RegisterMatchResultResponseDTO resp = service.registerResult(16L, new RegisterMatchResultRequestDTO(2, 0));

        assertTrue(resp.getMessage().contains("standings table"));
        assertEquals(16L, resp.getMatchId());
    }

    @Test
    void getStandings_NoMatches_ReturnsEmptyTable() {
        when(tournamentRepository.findByTournId("T-001"))
                .thenReturn(Optional.of(tournament));
        when(statsRepository.findByTorneoIdOrderByPuntosDesc(any()))
                .thenReturn(Collections.emptyList());

        StandingsTableResponseDTO resp = service.getStandings("T-001");

        assertNotNull(resp);
        assertEquals("T-001", resp.getTournamentId());
        assertEquals(0,       resp.getTotalMatchesPlayed());
        assertTrue(resp.getStandings().isEmpty());
    }

    @Test
    void getStandings_TestFixed() {
        lenient().when(tournamentRepository.findByTournId(anyString())).thenReturn(Optional.of(tournament));
        lenient().when(statsRepository.findByTorneoIdOrderByPuntosDesc(anyLong())).thenReturn(Collections.emptyList());

        StandingsTableResponseDTO resp = service.getStandings("T-001");
        assertNotNull(resp);
    }

    private Partido match(Long id, Equipo home, Equipo away, MatchStatus status) {
        Partido p = new Partido();
        p.setId(id);
        p.setEquipoLocal(home);
        p.setEquipoVisitante(away);
        p.setEstado(status);
        p.setTorneo(tournament);
        return p;
    }

    private Partido matchWithGoals(Long id, Equipo home, Equipo away,
            int homeGoals, int awayGoals, MatchStatus status) {
        Partido p = match(id, home, away, status);
        p.setGolesLocal(homeGoals);
        p.setGolesVisitante(awayGoals);
        return p;
    }
}
