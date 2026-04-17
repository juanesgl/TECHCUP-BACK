package edu.dosw.proyect.controllers;

import edu.dosw.proyect.controllers.dtos.request.RegisterMatchResultRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.RegisterMatchResultResponseDTO;
import edu.dosw.proyect.controllers.dtos.response.StandingsTableResponseDTO;
import edu.dosw.proyect.core.services.StandingsTableService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StandingsTableControllerTest {

    @Mock
    private StandingsTableService standingsTableService;

    @InjectMocks
    private StandingsTableController standingsTableController;

    @Test
    void registerResult_Success() {
        Long matchId = 1L;
        RegisterMatchResultRequestDTO request = new RegisterMatchResultRequestDTO();
        RegisterMatchResultResponseDTO responseDTO = new RegisterMatchResultResponseDTO();
        
        when(standingsTableService.registerResult(matchId, request)).thenReturn(responseDTO);

        ResponseEntity<RegisterMatchResultResponseDTO> response = standingsTableController.registerResult(matchId, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
        verify(standingsTableService).registerResult(matchId, request);
    }

    @Test
    void getStandings_Success() {
        String tournId = "tourn1";
        StandingsTableResponseDTO responseDTO = new StandingsTableResponseDTO();
        
        when(standingsTableService.getStandings(tournId)).thenReturn(responseDTO);

        ResponseEntity<StandingsTableResponseDTO> response = standingsTableController.getStandings(tournId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
        verify(standingsTableService).getStandings(tournId);
    }
}
