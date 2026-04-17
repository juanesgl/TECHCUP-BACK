package edu.dosw.proyect.controllers;

import edu.dosw.proyect.controllers.dtos.request.SoccerFieldDTO;
import edu.dosw.proyect.controllers.dtos.request.TournamentConfigurationRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.TournamentConfigurationResponseDTO;
import edu.dosw.proyect.core.services.TournamentConfigurationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TournamentConfigurationtControllerTest {

    @Mock
    private TournamentConfigurationService configuracionService;

    @InjectMocks
    private TournamentConfigurationtController tournamentConfigurationtController;

    @Test
    void configurarTorneo_HappyPath_RetornaOk() {
        SoccerFieldDTO cancha = new SoccerFieldDTO("Cancha Principal", "Calle 100");
        TournamentConfigurationRequestDTO request = TournamentConfigurationRequestDTO.builder()
                .registrationCloseDate(LocalDate.now().plusMonths(1))
                .canchas(List.of(cancha))
                .organizerId(1L)
                .build();

        TournamentConfigurationResponseDTO response = TournamentConfigurationResponseDTO.builder()
                .message("Configuracion exitosa")
                .tournId("TOURN-1")
                .build();

        when(configuracionService.configurarTorneo("TOURN-1", request))
                .thenReturn(response);

        ResponseEntity<TournamentConfigurationResponseDTO> result =
                tournamentConfigurationtController.configurarTorneo("TOURN-1", request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("TOURN-1", result.getBody().getTournId());
        verify(configuracionService, times(1))
                .configurarTorneo("TOURN-1", request);
    }
}