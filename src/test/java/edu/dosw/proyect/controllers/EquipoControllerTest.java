package edu.dosw.proyect.controllers;

import edu.dosw.proyect.controllers.dtos.request.CrearEquipoRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.CrearEquipoResponseDTO;
import edu.dosw.proyect.core.services.EquipoService;
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
class EquipoControllerTest {

    @Mock
    private EquipoService equipoService;

    @InjectMocks
    private EquipoController equipoController;

    @Test
    void crearEquipo_HappyPath_RetornaCreated() {
        CrearEquipoRequestDTO request = new CrearEquipoRequestDTO();
        request.setNombreEquipo("Alpha FC");
        request.setJugadoresInvitadosIds(List.of(1L, 2L, 3L, 4L, 5L, 6L));

        CrearEquipoResponseDTO response = CrearEquipoResponseDTO.builder()
                .mensajeConfirmacion("Equipo creado exitosamente")
                .notificacionesEnviadas(List.of())
                .build();

        when(equipoService.crearEquipo(1L, request)).thenReturn(response);

        ResponseEntity<CrearEquipoResponseDTO> result =
                equipoController.crearEquipo(1L, request);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertNotNull(result.getBody());
        verify(equipoService, times(1)).crearEquipo(1L, request);
    }
}