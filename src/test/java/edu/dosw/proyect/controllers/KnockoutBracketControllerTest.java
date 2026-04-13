package edu.dosw.proyect.controllers;

import edu.dosw.proyect.controllers.dtos.response.BracketMatchDTO;
import edu.dosw.proyect.controllers.dtos.response.TournamentBracketResponseDTO;
import edu.dosw.proyect.core.services.KnockoutBracketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KnockoutBracketControllerTest {

    @Mock
    private KnockoutBracketService bracketService;

    @InjectMocks
    private KnockoutBracketController knockoutBracketController;

    private TournamentBracketResponseDTO mockResponseDTO;

    @BeforeEach
    void setUp() {
        mockResponseDTO = new TournamentBracketResponseDTO();
    }

    @Test
    void generateBracket_Success() {
        when(bracketService.generateBracket("tourn1")).thenReturn(mockResponseDTO);

        ResponseEntity<TournamentBracketResponseDTO> response = knockoutBracketController.generateBracket("tourn1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockResponseDTO, response.getBody());
        verify(bracketService).generateBracket("tourn1");
    }

    @Test
    void getBracket_Success() {
        when(bracketService.getBracket("tourn1")).thenReturn(mockResponseDTO);

        ResponseEntity<TournamentBracketResponseDTO> response = knockoutBracketController.getBracket("tourn1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockResponseDTO, response.getBody());
        verify(bracketService).getBracket("tourn1");
    }

    @Test
    void getPhase_Success() {
        List<BracketMatchDTO> phaseMatches = List.of(new BracketMatchDTO());
        when(bracketService.getPhase("tourn1", "FINAL")).thenReturn(phaseMatches);

        ResponseEntity<List<BracketMatchDTO>> response = knockoutBracketController.getPhase("tourn1", "FINAL");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(phaseMatches, response.getBody());
        verify(bracketService).getPhase("tourn1", "FINAL");
    }

    @Test
    void advanceBracket_Success() {
        doNothing().when(bracketService).advanceBracket(100L);

        ResponseEntity<String> response = knockoutBracketController.advanceBracket("tourn1", 100L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Bracket actualizado exitosamente", response.getBody());
        verify(bracketService).advanceBracket(100L);
    }
}
