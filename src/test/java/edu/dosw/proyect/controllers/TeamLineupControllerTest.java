package edu.dosw.proyect.controllers;

import edu.dosw.proyect.controllers.dtos.request.SaveLineupRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.TeamLineupResponseDTO;
import edu.dosw.proyect.core.services.TeamLineupService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeamLineupControllerTest {

    @Mock
    private TeamLineupService teamLineupService;

    @InjectMocks
    private TeamLineupController teamLineupController;

    @Test
    void saveLineup_HappyPath_RetornaCreated() {
        SaveLineupRequestDTO request = new SaveLineupRequestDTO();
        request.setTeamId(1L);
        request.setMatchId(1L);

        TeamLineupResponseDTO dto = TeamLineupResponseDTO.builder()
                .lineupId(1L).teamName("Alpha FC").build();

        when(teamLineupService.saveLineup(1L, request)).thenReturn(dto);

        ResponseEntity<TeamLineupResponseDTO> result =
                teamLineupController.saveLineup(1L, request);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertNotNull(result.getBody());
        verify(teamLineupService, times(1)).saveLineup(1L, request);
    }

    @Test
    void updateLineup_HappyPath_RetornaOk() {
        SaveLineupRequestDTO request = new SaveLineupRequestDTO();
        TeamLineupResponseDTO dto = TeamLineupResponseDTO.builder()
                .lineupId(1L).build();

        when(teamLineupService.updateLineup(1L, 1L, request)).thenReturn(dto);

        ResponseEntity<TeamLineupResponseDTO> result =
                teamLineupController.updateLineup(1L, 1L, request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void getLineup_HappyPath_RetornaOk() {
        TeamLineupResponseDTO dto = TeamLineupResponseDTO.builder()
                .lineupId(1L).build();

        when(teamLineupService.getLineup(1L, 1L, 1L)).thenReturn(dto);

        ResponseEntity<TeamLineupResponseDTO> result =
                teamLineupController.getLineup(1L, 1L, 1L);

        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void getTeamLineups_HappyPath_RetornaOk() {
        when(teamLineupService.getTeamLineups(1L, 1L)).thenReturn(List.of());

        ResponseEntity<List<TeamLineupResponseDTO>> result =
                teamLineupController.getTeamLineups(1L, 1L);

        assertEquals(HttpStatus.OK, result.getStatusCode());
    }
}