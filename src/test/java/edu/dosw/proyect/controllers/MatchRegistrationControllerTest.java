package edu.dosw.proyect.controllers;

import edu.dosw.proyect.controllers.dtos.request.RegisterMatchRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.RegisterMatchResponseDTO;
import edu.dosw.proyect.core.services.MatchRegistrationService;
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
class MatchRegistrationControllerTest {

    @Mock
    private MatchRegistrationService matchRegistrationService;

    @InjectMocks
    private MatchRegistrationController matchRegistrationController;

    @Test
    void registerMatch_Success() {
        Long matchId = 100L;
        RegisterMatchRequestDTO requestDTO = new RegisterMatchRequestDTO();
        RegisterMatchResponseDTO responseDTO = new RegisterMatchResponseDTO();
        
        when(matchRegistrationService.registerMatch(matchId, requestDTO)).thenReturn(responseDTO);

        ResponseEntity<RegisterMatchResponseDTO> response = matchRegistrationController.registerMatch(matchId, requestDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
        verify(matchRegistrationService).registerMatch(matchId, requestDTO);
    }
}
