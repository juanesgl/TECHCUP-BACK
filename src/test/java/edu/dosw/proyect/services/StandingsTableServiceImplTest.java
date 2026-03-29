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
import edu.dosw.proyect.core.repositories.PartidoRepository;
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

    @Spy
    private StandingsTableMapper standingsMapper;

    @InjectMocks
    private StandingsTableServiceImpl service;

    private Tournament tournament;
    private Equipo     teamA;
    private Equipo     teamB;
    private Equipo     teamC;

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

        RegisterMatchResultResponseDTO resp =
                service.registerResult(10L, new RegisterMatchResultRequestDTO(3, 1));

        assertEquals("HOME",      resp.getOutcome());
        assertEquals(3,           resp.getHomeGoals());
        assertEquals(1,           resp.getAwayGoals());
        assertEquals("Alpha FC",  resp.getHomeTeam());
        assertEquals("Beta FC",   resp.getAwayTeam());
        assertEquals(MatchStatus.FINALIZADO, match.getEstado());
        verify(matchRepository).save(match);
    }

    @Test
    void registerResult_AwayWin_ReturnsAway() {
        Partido match = match(11L, teamA, teamB, MatchStatus.PROGRAMADO);
        when(matchRepository.findById(11L)).thenReturn(Optional.of(match));
        when(matchRepository.save(any())).thenReturn(match);

        RegisterMatchResultResponseDTO resp =
                service.registerResult(11L, new RegisterMatchResultRequestDTO(0, 2));

        assertEquals("AWAY", resp.getOutcome());
    }

    @Test
    void registerResult_Draw_ReturnsDraw() {
        Partido match = match(12L, teamA, teamB, MatchStatus.PROGRAMADO);
        when(matchRepository.findById(12L)).thenReturn(Optional.of(match));
        when(matchRepository.save(any())).thenReturn(match);

        RegisterMatchResultResponseDTO resp =
                service.registerResult(12L, new RegisterMatchResultRequestDTO(1, 1));

        assertEquals("DRAW", resp.getOutcome());
    }

    @Test
    void registerResult_MatchInProgress_AllowsUpdate() {
        Partido match = match(13L, teamA, teamB, MatchStatus.EN_JUEGO);
        when(matchRepository.findById(13L)).thenReturn(Optional.of(match));
        when(matchRepository.save(any())).thenReturn(match);

        RegisterMatchResultResponseDTO resp =
                service.registerResult(13L, new RegisterMatchResultRequestDTO(2, 2));

        assertEquals("DRAW",               resp.getOutcome());
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

        RegisterMatchResultResponseDTO resp =
                service.registerResult(16L, new RegisterMatchResultRequestDTO(2, 0));

        assertTrue(resp.getMessage().contains("standings table"));
        assertEquals(16L, resp.getMatchId());
    }

    @Test
    void getStandings_NoMatches_ReturnsEmptyTable() {
        when(matchRepository.findByTorneo_TournId("T-001"))
                .thenReturn(Collections.emptyList());

        StandingsTableResponseDTO resp = service.getStandings("T-001");

        assertNotNull(resp);
        assertEquals("T-001", resp.getTournamentId());
        assertEquals(0,       resp.getTotalMatchesPlayed());
        assertTrue(resp.getStandings().isEmpty());
    }

    @Test
    void getStandings_OnlyScheduledMatches_ReturnsEmptyTable() {
        Partido p = match(1L, teamA, teamB, MatchStatus.PROGRAMADO);
        p.setTorneo(tournament);
        when(matchRepository.findByTorneo_TournId("T-001")).thenReturn(List.of(p));

        StandingsTableResponseDTO resp = service.getStandings("T-001");

        assertEquals(0, resp.getTotalMatchesPlayed());
        assertTrue(resp.getStandings().isEmpty());
    }

    @Test
    void getStandings_CancelledMatch_NotCounted() {
        Partido p = matchWithGoals(1L, teamA, teamB, 3, 1, MatchStatus.CANCELADO);
        when(matchRepository.findByTorneo_TournId("T-001")).thenReturn(List.of(p));

        StandingsTableResponseDTO resp = service.getStandings("T-001");

        assertEquals(0, resp.getTotalMatchesPlayed());
        assertTrue(resp.getStandings().isEmpty());
    }

    @Test
    void getStandings_OneFinishedMatch_HomeWin_FullCalculation() {
        Partido p = matchWithGoals(1L, teamA, teamB, 3, 1, MatchStatus.FINALIZADO);
        when(matchRepository.findByTorneo_TournId("T-001")).thenReturn(List.of(p));

        StandingsTableResponseDTO resp = service.getStandings("T-001");

        assertEquals(1, resp.getTotalMatchesPlayed());
        assertEquals(2, resp.getTotalTeams());

        TeamStandingDTO first = resp.getStandings().get(0);
        assertEquals(1,          first.getPosition());
        assertEquals("Alpha FC", first.getTeamName());
        assertEquals(3,          first.getPoints());
        assertEquals(1,          first.getMatchesPlayed());
        assertEquals(1,          first.getWins());
        assertEquals(0,          first.getDraws());
        assertEquals(0,          first.getLosses());
        assertEquals(3,          first.getGoalsFor());
        assertEquals(1,          first.getGoalsAgainst());
        assertEquals(2,          first.getGoalDifference());

        TeamStandingDTO second = resp.getStandings().get(1);
        assertEquals(2,         second.getPosition());
        assertEquals("Beta FC", second.getTeamName());
        assertEquals(0,         second.getPoints());
        assertEquals(0,         second.getWins());
        assertEquals(1,         second.getLosses());
        assertEquals(1,         second.getGoalsFor());
        assertEquals(3,         second.getGoalsAgainst());
        assertEquals(-2,        second.getGoalDifference());
    }

    @Test
    void getStandings_Draw_BothTeamsGetOnePoint() {
        Partido p = matchWithGoals(2L, teamA, teamB, 2, 2, MatchStatus.FINALIZADO);
        when(matchRepository.findByTorneo_TournId("T-001")).thenReturn(List.of(p));

        StandingsTableResponseDTO resp = service.getStandings("T-001");

        TeamStandingDTO tA = resp.getStandings().stream()
                .filter(s -> s.getTeamName().equals("Alpha FC")).findFirst().orElseThrow();
        TeamStandingDTO tB = resp.getStandings().stream()
                .filter(s -> s.getTeamName().equals("Beta FC")).findFirst().orElseThrow();

        assertEquals(1, tA.getPoints());
        assertEquals(1, tA.getDraws());
        assertEquals(0, tA.getGoalDifference());
        assertEquals(1, tB.getPoints());
        assertEquals(1, tB.getDraws());
    }

    @Test
    void getStandings_InProgressMatch_IsCounted() {
        Partido p = matchWithGoals(3L, teamA, teamB, 1, 0, MatchStatus.EN_JUEGO);
        when(matchRepository.findByTorneo_TournId("T-001")).thenReturn(List.of(p));

        StandingsTableResponseDTO resp = service.getStandings("T-001");

        assertEquals(1,          resp.getTotalMatchesPlayed());
        assertEquals("Alpha FC", resp.getStandings().get(0).getTeamName());
        assertEquals(3,          resp.getStandings().get(0).getPoints());
    }

    @Test
    void getStandings_MultipleMatches_PointsAccumulateCorrectly() {
        Partido p1 = matchWithGoals(1L, teamA, teamB, 2, 0, MatchStatus.FINALIZADO);
        Partido p2 = matchWithGoals(2L, teamA, teamC, 1, 0, MatchStatus.FINALIZADO);
        Partido p3 = matchWithGoals(3L, teamB, teamC, 3, 1, MatchStatus.FINALIZADO);

        when(matchRepository.findByTorneo_TournId("T-001"))
                .thenReturn(Arrays.asList(p1, p2, p3));

        StandingsTableResponseDTO resp = service.getStandings("T-001");

        assertEquals(3, resp.getTotalMatchesPlayed());
        List<TeamStandingDTO> standings = resp.getStandings();

        assertEquals("Alpha FC", standings.get(0).getTeamName());
        assertEquals(6,          standings.get(0).getPoints());
        assertEquals("Beta FC",  standings.get(1).getTeamName());
        assertEquals(3,          standings.get(1).getPoints());
        assertEquals("Cyber Sec", standings.get(2).getTeamName());
        assertEquals(0,           standings.get(2).getPoints());
    }

    @Test
    void getStandings_SortedByPoints() {
        Partido p = matchWithGoals(1L, teamA, teamB, 0, 1, MatchStatus.FINALIZADO);
        when(matchRepository.findByTorneo_TournId("T-001")).thenReturn(List.of(p));

        StandingsTableResponseDTO resp = service.getStandings("T-001");

        assertEquals("Beta FC",  resp.getStandings().get(0).getTeamName());
        assertEquals("Alpha FC", resp.getStandings().get(1).getTeamName());
    }

    @Test
    void getStandings_TiebreakerByGoalDifference() {
        Partido p1 = matchWithGoals(1L, teamA, teamC, 3, 0, MatchStatus.FINALIZADO);
        Partido p2 = matchWithGoals(2L, teamB, teamC, 1, 0, MatchStatus.FINALIZADO);

        when(matchRepository.findByTorneo_TournId("T-001"))
                .thenReturn(Arrays.asList(p1, p2));

        StandingsTableResponseDTO resp = service.getStandings("T-001");

        assertEquals("Alpha FC", resp.getStandings().get(0).getTeamName());
        assertEquals(3,          resp.getStandings().get(0).getGoalDifference());
        assertEquals("Beta FC",  resp.getStandings().get(1).getTeamName());
    }

    @Test
    void getStandings_TiebreakerByGoalsScored() {
        Partido p1 = matchWithGoals(1L, teamA, teamC, 3, 2, MatchStatus.FINALIZADO);
        Partido p2 = matchWithGoals(2L, teamB, teamC, 2, 1, MatchStatus.FINALIZADO);

        when(matchRepository.findByTorneo_TournId("T-001"))
                .thenReturn(Arrays.asList(p1, p2));

        StandingsTableResponseDTO resp = service.getStandings("T-001");

        assertEquals("Alpha FC", resp.getStandings().get(0).getTeamName());
        assertEquals("Beta FC",  resp.getStandings().get(1).getTeamName());
    }

    @Test
    void getStandings_FinalTiebreakerByTeamName_Alphabetical() {
        Partido p = matchWithGoals(1L, teamA, teamB, 1, 1, MatchStatus.FINALIZADO);
        when(matchRepository.findByTorneo_TournId("T-001")).thenReturn(List.of(p));

        StandingsTableResponseDTO resp = service.getStandings("T-001");

        assertEquals("Alpha FC", resp.getStandings().get(0).getTeamName());
        assertEquals("Beta FC",  resp.getStandings().get(1).getTeamName());
    }

    @Test
    void getStandings_PositionNumbersAreCorrectlyAssigned() {
        Partido p1 = matchWithGoals(1L, teamA, teamB, 2, 0, MatchStatus.FINALIZADO);
        Partido p2 = matchWithGoals(2L, teamA, teamC, 1, 0, MatchStatus.FINALIZADO);

        when(matchRepository.findByTorneo_TournId("T-001"))
                .thenReturn(Arrays.asList(p1, p2));

        StandingsTableResponseDTO resp = service.getStandings("T-001");
        for (int i = 0; i < resp.getStandings().size(); i++) {
            assertEquals(i + 1, resp.getStandings().get(i).getPosition());
        }
    }

    @Test
    void getStandings_NullHomeTeam_MatchSkipped() {
        Partido p = new Partido();
        p.setId(20L);
        p.setTorneo(tournament);
        p.setEquipoLocal(null);
        p.setEquipoVisitante(teamB);
        p.setEstado(MatchStatus.FINALIZADO);
        p.setGolesLocal(1);
        p.setGolesVisitante(0);

        when(matchRepository.findByTorneo_TournId("T-001")).thenReturn(List.of(p));

        assertDoesNotThrow(() -> {
            StandingsTableResponseDTO resp = service.getStandings("T-001");
            assertTrue(resp.getStandings().isEmpty());
        });
    }

    @Test
    void getStandings_NullTeamId_MatchSkipped() {
        Equipo noId = Equipo.builder().id(null).nombre("No ID Team").build();
        Partido p   = matchWithGoals(21L, noId, teamB, 1, 0, MatchStatus.FINALIZADO);

        when(matchRepository.findByTorneo_TournId("T-001")).thenReturn(List.of(p));

        StandingsTableResponseDTO resp = service.getStandings("T-001");
        assertTrue(resp.getStandings().isEmpty());
    }

    @Test
    void getStandings_TournamentNameNull_UsesGenericName() {
        Tournament noName = new Tournament();
        noName.setTournId("T-002");
        noName.setName(null);

        Partido p = matchWithGoals(22L, teamA, teamB, 1, 0, MatchStatus.FINALIZADO);
        p.setTorneo(noName);

        when(matchRepository.findByTorneo_TournId("T-002")).thenReturn(List.of(p));

        StandingsTableResponseDTO resp = service.getStandings("T-002");
        assertEquals("Tournament", resp.getTournamentName());
    }

    @Test
    void getStandings_NullTorneo_UsesGenericName() {
        Partido p = matchWithGoals(23L, teamA, teamB, 1, 0, MatchStatus.FINALIZADO);
        p.setTorneo(null);

        when(matchRepository.findByTorneo_TournId("T-003")).thenReturn(List.of(p));

        StandingsTableResponseDTO resp = service.getStandings("T-003");
        assertEquals("Tournament", resp.getTournamentName());
    }

    @Test
    void getStandings_TournamentNameResolvedCorrectly() {
        Partido p = matchWithGoals(30L, teamA, teamB, 2, 1, MatchStatus.FINALIZADO);
        when(matchRepository.findByTorneo_TournId("T-001")).thenReturn(List.of(p));

        StandingsTableResponseDTO resp = service.getStandings("T-001");
        assertEquals("TechCup League", resp.getTournamentName());
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
