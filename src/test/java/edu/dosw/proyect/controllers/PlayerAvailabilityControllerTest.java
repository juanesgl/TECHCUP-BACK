package edu.dosw.proyect.controllers;

import edu.dosw.proyect.controllers.dtos.request.AvailabilityRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.AvailabilityResponseDTO;
import edu.dosw.proyect.core.services.PlayerAvailabilityService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlayerAvailabilityControllerTest {

    @Mock
    private PlayerAvailabilityService playerAvailabilityService;

    @InjectMocks
    private PlayerAvailabilityController playerAvailabilityController;

    @Test
    void actualizarDisponibilidad_HappyPath_RetornaOk() {
        AvailabilityRequestDTO request = new AvailabilityRequestDTO(true);
        AvailabilityResponseDTO response =
                new AvailabilityResponseDTO("Disponibilidad actualizada", true);

        when(playerAvailabilityService.actualizarDisponibilidad(1L, request))
                .thenReturn(response);

        ResponseEntity<AvailabilityResponseDTO> result =
                playerAvailabilityController.actualizarDisponibilidad(1L, request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertTrue(result.getBody().getEstadoFinal());
        verify(playerAvailabilityService, times(1))
                .actualizarDisponibilidad(1L, request);
    }
}