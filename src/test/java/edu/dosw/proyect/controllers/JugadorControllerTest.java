package edu.dosw.proyect.controllers;

import edu.dosw.proyect.controllers.dtos.request.DisponibilidadRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.DisponibilidadResponseDTO;
import edu.dosw.proyect.core.services.JugadorService;
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
class JugadorControllerTest {

    @Mock
    private JugadorService jugadorService;

    @InjectMocks
    private JugadorController jugadorController;

    @Test
    void actualizarDisponibilidad_HappyPath_RetornaOk() {
        DisponibilidadRequestDTO request = new DisponibilidadRequestDTO(true);
        DisponibilidadResponseDTO response =
                new DisponibilidadResponseDTO("Disponibilidad actualizada", true);

        when(jugadorService.actualizarDisponibilidad(1L, request))
                .thenReturn(response);

        ResponseEntity<DisponibilidadResponseDTO> result =
                jugadorController.actualizarDisponibilidad(1L, request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertTrue(result.getBody().getEstadoFinal());
        verify(jugadorService, times(1))
                .actualizarDisponibilidad(1L, request);
    }
}