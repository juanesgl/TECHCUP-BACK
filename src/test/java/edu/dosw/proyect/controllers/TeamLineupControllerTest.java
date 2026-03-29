package edu.dosw.proyect.controllers;

import edu.dosw.proyect.controllers.dtos.request.SaveLineupRequestDTO;
import edu.dosw.proyect.controllers.dtos.request.StarterEntryRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.StarterEntryResponseDTO;
import edu.dosw.proyect.controllers.dtos.response.TeamLineupResponseDTO;
import edu.dosw.proyect.core.exceptions.BusinessRuleException;
import edu.dosw.proyect.core.exceptions.ResourceNotFoundException;
import edu.dosw.proyect.core.models.enums.FieldPosition;
import edu.dosw.proyect.core.models.enums.LineupStatus;
import edu.dosw.proyect.core.models.enums.TacticalFormation;
import edu.dosw.proyect.core.services.TeamLineupService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class TeamLineupControllerTest {

    @Mock
    private TeamLineupService teamLineupService;

    @InjectMocks
    private TeamLineupController controller;

    private static final Long CAPTAIN_ID = 1L;
    private static final Long TEAM_ID    = 10L;
    private static final Long MATCH_ID   = 20L;
    private static final Long LINEUP_ID  = 100L;

    private TeamLineupResponseDTO mockResponse;

    @BeforeEach
    void setUp() {
        mockResponse = buildMockResponse("Lineup saved successfully. Your team is ready for the match!");
    }


    @Test
    void saveLineup_ValidRequest_Returns201() {
        when(teamLineupService.saveLineup(eq(CAPTAIN_ID), any())).thenReturn(mockResponse);

        ResponseEntity<TeamLineupResponseDTO> resp =
                controller.saveLineup(CAPTAIN_ID, validRequest());

        assertEquals(HttpStatus.CREATED, resp.getStatusCode());
        assertNotNull(resp.getBody());
        assertEquals(LINEUP_ID, resp.getBody().getLineupId());
        assertEquals(TacticalFormation.F_1_2_3_1, resp.getBody().getFormation());
        assertTrue(resp.getBody().getMessage().contains("saved successfully"));
        verify(teamLineupService, times(1)).saveLineup(eq(CAPTAIN_ID), any());
    }

    @Test
    void saveLineup_NotCaptain_PropagatesBusinessRuleException() {
        when(teamLineupService.saveLineup(eq(99L), any()))
                .thenThrow(new BusinessRuleException("Only the team captain can manage the lineup."));

        BusinessRuleException ex = assertThrows(BusinessRuleException.class,
                () -> controller.saveLineup(99L, validRequest()));

        assertTrue(ex.getMessage().contains("captain"));
    }

    @Test
    void saveLineup_WrongStarterCount_PropagatesBusinessRuleException() {
        when(teamLineupService.saveLineup(eq(CAPTAIN_ID), any()))
                .thenThrow(new BusinessRuleException("You must select exactly 7 starter players."));

        BusinessRuleException ex = assertThrows(BusinessRuleException.class,
                () -> controller.saveLineup(CAPTAIN_ID, validRequest()));

        assertTrue(ex.getMessage().contains("7 starter players"));
    }

    @Test
    void saveLineup_TeamNotFound_PropagatesResourceNotFoundException() {
        when(teamLineupService.saveLineup(eq(CAPTAIN_ID), any()))
                .thenThrow(new ResourceNotFoundException("Team not found with ID: 10"));

        assertThrows(ResourceNotFoundException.class,
                () -> controller.saveLineup(CAPTAIN_ID, validRequest()));
    }

    @Test
    void saveLineup_MatchNotScheduled_PropagatesBusinessRuleException() {
        when(teamLineupService.saveLineup(eq(CAPTAIN_ID), any()))
                .thenThrow(new BusinessRuleException(
                        "Lineup management is only allowed for scheduled matches."));

        assertThrows(BusinessRuleException.class,
                () -> controller.saveLineup(CAPTAIN_ID, validRequest()));
    }


    @Test
    void updateLineup_ValidRequest_Returns200() {
        TeamLineupResponseDTO updated =
                buildMockResponse("Lineup updated successfully. Changes saved correctly!");

        when(teamLineupService.updateLineup(eq(CAPTAIN_ID), eq(LINEUP_ID), any()))
                .thenReturn(updated);

        ResponseEntity<TeamLineupResponseDTO> resp =
                controller.updateLineup(CAPTAIN_ID, LINEUP_ID, validRequest());

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertNotNull(resp.getBody());
        assertTrue(resp.getBody().getMessage().contains("updated successfully"));
    }

    @Test
    void updateLineup_LineupNotFound_PropagatesResourceNotFoundException() {
        when(teamLineupService.updateLineup(eq(CAPTAIN_ID), eq(LINEUP_ID), any()))
                .thenThrow(new ResourceNotFoundException("Lineup not found with ID: 100"));

        assertThrows(ResourceNotFoundException.class,
                () -> controller.updateLineup(CAPTAIN_ID, LINEUP_ID, validRequest()));
    }

    @Test
    void updateLineup_LockedLineup_PropagatesBusinessRuleException() {
        when(teamLineupService.updateLineup(eq(CAPTAIN_ID), eq(LINEUP_ID), any()))
                .thenThrow(new BusinessRuleException(
                        "The lineup can no longer be modified because the match has already started."));

        BusinessRuleException ex = assertThrows(BusinessRuleException.class,
                () -> controller.updateLineup(CAPTAIN_ID, LINEUP_ID, validRequest()));

        assertTrue(ex.getMessage().contains("already started"));
    }


    @Test
    void getLineup_ExistingLineup_Returns200() {
        when(teamLineupService.getLineup(CAPTAIN_ID, TEAM_ID, MATCH_ID))
                .thenReturn(mockResponse);

        ResponseEntity<TeamLineupResponseDTO> resp =
                controller.getLineup(CAPTAIN_ID, TEAM_ID, MATCH_ID);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertNotNull(resp.getBody());
        assertEquals(LINEUP_ID, resp.getBody().getLineupId());
        verify(teamLineupService, times(1)).getLineup(CAPTAIN_ID, TEAM_ID, MATCH_ID);
    }

    @Test
    void getLineup_NoLineup_PropagatesResourceNotFoundException() {
        when(teamLineupService.getLineup(CAPTAIN_ID, TEAM_ID, MATCH_ID))
                .thenThrow(new ResourceNotFoundException(
                        "No lineup found. There are no scheduled matches at the moment."));

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> controller.getLineup(CAPTAIN_ID, TEAM_ID, MATCH_ID));

        assertTrue(ex.getMessage().contains("no scheduled matches"));
    }


    @Test
    void getTeamLineups_WithLineups_Returns200() {
        when(teamLineupService.getTeamLineups(CAPTAIN_ID, TEAM_ID))
                .thenReturn(List.of(mockResponse, mockResponse));

        ResponseEntity<List<TeamLineupResponseDTO>> resp =
                controller.getTeamLineups(CAPTAIN_ID, TEAM_ID);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertEquals(2, resp.getBody().size());
    }

    @Test
    void getTeamLineups_EmptyList_Returns200WithEmptyList() {
        when(teamLineupService.getTeamLineups(CAPTAIN_ID, TEAM_ID))
                .thenReturn(Collections.emptyList());

        ResponseEntity<List<TeamLineupResponseDTO>> resp =
                controller.getTeamLineups(CAPTAIN_ID, TEAM_ID);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertTrue(resp.getBody().isEmpty());
    }

    @Test
    void getTeamLineups_CallsServiceWithCorrectIds() {
        when(teamLineupService.getTeamLineups(CAPTAIN_ID, TEAM_ID))
                .thenReturn(Collections.emptyList());

        controller.getTeamLineups(CAPTAIN_ID, TEAM_ID);

        verify(teamLineupService, times(1)).getTeamLineups(CAPTAIN_ID, TEAM_ID);
    }


    private TeamLineupResponseDTO buildMockResponse(String message) {
        List<StarterEntryResponseDTO> starters = new ArrayList<>();
        starters.add(StarterEntryResponseDTO.builder()
                .playerId(1L).playerName("Player 1")
                .fieldPosition(FieldPosition.GOALKEEPER).build());

        return TeamLineupResponseDTO.builder()
                .lineupId(LINEUP_ID)
                .teamId(TEAM_ID)
                .teamName("Alpha FC")
                .matchId(MATCH_ID)
                .formation(TacticalFormation.F_1_2_3_1)
                .formationDisplay("1-2-3-1")
                .status(LineupStatus.SAVED)
                .starters(starters)
                .reserveIds(Collections.emptyList())
                .savedAt(LocalDateTime.now())
                .message(message)
                .build();
    }

    private SaveLineupRequestDTO validRequest() {
        List<StarterEntryRequestDTO> starters = new ArrayList<>();
        FieldPosition[] positions = {
                FieldPosition.GOALKEEPER,
                FieldPosition.DEFENDER, FieldPosition.DEFENDER,
                FieldPosition.MIDFIELDER, FieldPosition.MIDFIELDER, FieldPosition.MIDFIELDER,
                FieldPosition.FORWARD
        };
        for (int i = 0; i < 7; i++) {
            starters.add(new StarterEntryRequestDTO((long) (i + 1), positions[i]));
        }
        SaveLineupRequestDTO req = new SaveLineupRequestDTO();
        req.setTeamId(TEAM_ID);
        req.setMatchId(MATCH_ID);
        req.setFormation(TacticalFormation.F_1_2_3_1);
        req.setStarters(starters);
        req.setReserveIds(Collections.emptyList());
        return req;
    }
}