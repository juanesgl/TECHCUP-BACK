package edu.dosw.proyect.controllers;

import edu.dosw.proyect.controllers.dtos.request.RegisterMatchResultRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.RegisterMatchResultResponseDTO;
import edu.dosw.proyect.controllers.dtos.response.StandingsTableResponseDTO;
import edu.dosw.proyect.controllers.dtos.response.TeamStandingDTO;
import edu.dosw.proyect.core.exceptions.BusinessRuleException;
import edu.dosw.proyect.core.exceptions.ResourceNotFoundException;
import edu.dosw.proyect.core.services.StandingsTableService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StandingsTableControllerTest {

    @Mock
    private StandingsTableService standingsTableService;

    @InjectMocks
    private StandingsTableController controller;

    @Test
    void registerResult_HomeWin_Returns200() {
        RegisterMatchResultResponseDTO mockResp = RegisterMatchResultResponseDTO.builder()
                .matchId(1L).homeTeam("Alpha FC").awayTeam("Beta FC")
                .homeGoals(3).awayGoals(1).outcome("HOME")
                .message("Result registered successfully. The standings table has been updated.")
                .build();

        when(standingsTableService.registerResult(eq(1L), any())).thenReturn(mockResp);

        ResponseEntity<RegisterMatchResultResponseDTO> resp =
                controller.registerResult(1L, new RegisterMatchResultRequestDTO(3, 1));

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertNotNull(resp.getBody());
        assertEquals("HOME",     resp.getBody().getOutcome());
        assertEquals("Alpha FC", resp.getBody().getHomeTeam());
        assertEquals(3,          resp.getBody().getHomeGoals());
        verify(standingsTableService, times(1)).registerResult(eq(1L), any());
    }

    @Test
    void registerResult_AwayWin_Returns200() {
        RegisterMatchResultResponseDTO mockResp = RegisterMatchResultResponseDTO.builder()
                .outcome("AWAY").homeGoals(0).awayGoals(2).build();

        when(standingsTableService.registerResult(eq(2L), any())).thenReturn(mockResp);

        ResponseEntity<RegisterMatchResultResponseDTO> resp =
                controller.registerResult(2L, new RegisterMatchResultRequestDTO(0, 2));

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertEquals("AWAY",       resp.getBody().getOutcome());
    }

    @Test
    void registerResult_Draw_Returns200() {
        RegisterMatchResultResponseDTO mockResp = RegisterMatchResultResponseDTO.builder()
                .outcome("DRAW").homeGoals(1).awayGoals(1).build();

        when(standingsTableService.registerResult(eq(3L), any())).thenReturn(mockResp);

        ResponseEntity<RegisterMatchResultResponseDTO> resp =
                controller.registerResult(3L, new RegisterMatchResultRequestDTO(1, 1));

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertEquals("DRAW",       resp.getBody().getOutcome());
    }

    @Test
    void registerResult_MatchNotFound_PropagatesResourceNotFoundException() {
        when(standingsTableService.registerResult(eq(99L), any()))
                .thenThrow(new ResourceNotFoundException("Match not found with ID: 99"));

        assertThrows(ResourceNotFoundException.class,
                () -> controller.registerResult(99L, new RegisterMatchResultRequestDTO(1, 0)));
    }

    @Test
    void registerResult_CancelledMatch_PropagatesBusinessRuleException() {
        when(standingsTableService.registerResult(eq(5L), any()))
                .thenThrow(new BusinessRuleException("Cannot register a result for a CANCELLED match."));

        BusinessRuleException ex = assertThrows(BusinessRuleException.class,
                () -> controller.registerResult(5L, new RegisterMatchResultRequestDTO(1, 0)));

        assertTrue(ex.getMessage().contains("CANCELLED"));
    }

    @Test
    void registerResult_AlreadyFinished_PropagatesBusinessRuleException() {
        when(standingsTableService.registerResult(eq(6L), any()))
                .thenThrow(new BusinessRuleException(
                        "This match already has a registered result (status: FINISHED)."));

        BusinessRuleException ex = assertThrows(BusinessRuleException.class,
                () -> controller.registerResult(6L, new RegisterMatchResultRequestDTO(2, 1)));

        assertTrue(ex.getMessage().contains("FINISHED"));
    }

    @Test
    void getStandings_WithTeams_Returns200AndCorrectData() {
        TeamStandingDTO e1 = TeamStandingDTO.builder()
                .position(1).teamId(1L).teamName("Alpha FC")
                .matchesPlayed(2).wins(2).draws(0).losses(0)
                .goalsFor(5).goalsAgainst(1).goalDifference(4).points(6).build();

        TeamStandingDTO e2 = TeamStandingDTO.builder()
                .position(2).teamId(2L).teamName("Beta FC")
                .matchesPlayed(2).wins(0).draws(0).losses(2)
                .goalsFor(1).goalsAgainst(5).goalDifference(-4).points(0).build();

        StandingsTableResponseDTO mockResp = StandingsTableResponseDTO.builder()
                .tournamentId("T-001").tournamentName("TechCup League")
                .totalTeams(2).totalMatchesPlayed(2)
                .standings(List.of(e1, e2)).build();

        when(standingsTableService.getStandings("T-001")).thenReturn(mockResp);

        ResponseEntity<StandingsTableResponseDTO> resp = controller.getStandings("T-001");

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertNotNull(resp.getBody());
        assertEquals("T-001",    resp.getBody().getTournamentId());
        assertEquals(2,          resp.getBody().getTotalTeams());
        assertEquals(2,          resp.getBody().getTotalMatchesPlayed());
        assertEquals(2,          resp.getBody().getStandings().size());
        assertEquals(1,          resp.getBody().getStandings().get(0).getPosition());
        assertEquals(6,          resp.getBody().getStandings().get(0).getPoints());
        assertEquals("Alpha FC", resp.getBody().getStandings().get(0).getTeamName());
    }

    @Test
    void getStandings_EmptyTable_Returns200() {
        StandingsTableResponseDTO mockResp = StandingsTableResponseDTO.builder()
                .tournamentId("T-002").tournamentName("Tournament")
                .totalTeams(0).totalMatchesPlayed(0)
                .standings(Collections.emptyList()).build();

        when(standingsTableService.getStandings("T-002")).thenReturn(mockResp);

        ResponseEntity<StandingsTableResponseDTO> resp = controller.getStandings("T-002");

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertTrue(resp.getBody().getStandings().isEmpty());
        assertEquals(0, resp.getBody().getTotalMatchesPlayed());
    }

    @Test
    void getStandings_CallsServiceWithCorrectTournamentId() {
        StandingsTableResponseDTO mockResp = StandingsTableResponseDTO.builder()
                .tournamentId("T-XYZ").standings(Collections.emptyList()).build();

        when(standingsTableService.getStandings("T-XYZ")).thenReturn(mockResp);

        controller.getStandings("T-XYZ");

        verify(standingsTableService, times(1)).getStandings("T-XYZ");
    }
}
