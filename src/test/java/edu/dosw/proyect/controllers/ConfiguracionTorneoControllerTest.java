package edu.dosw.proyect.controllers;

import edu.dosw.proyect.controllers.dtos.request.CanchaDTO;
import edu.dosw.proyect.controllers.dtos.request.ConfiguracionTorneoRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.ConfiguracionTorneoResponseDTO;
import edu.dosw.proyect.core.services.ConfiguracionTorneoService;
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
class ConfiguracionTorneoControllerTest {

    @Mock
    private ConfiguracionTorneoService configuracionService;

    @InjectMocks
    private ConfiguracionTorneoController configuracionTorneoController;

    @Test
    void configurarTorneo_HappyPath_RetornaOk() {
        CanchaDTO cancha = new CanchaDTO("Cancha Principal", "Calle 100");
        ConfiguracionTorneoRequestDTO request = ConfiguracionTorneoRequestDTO.builder()
                .registrationCloseDate(LocalDate.now().plusMonths(1))
                .canchas(List.of(cancha))
                .organizerId(1L)
                .build();

        ConfiguracionTorneoResponseDTO response = ConfiguracionTorneoResponseDTO.builder()
                .message("Configuracion exitosa")
                .tournId("TOURN-1")
                .build();

        when(configuracionService.configurarTorneo("TOURN-1", request))
                .thenReturn(response);

        ResponseEntity<ConfiguracionTorneoResponseDTO> result =
                configuracionTorneoController.configurarTorneo("TOURN-1", request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("TOURN-1", result.getBody().getTournId());
        verify(configuracionService, times(1))
                .configurarTorneo("TOURN-1", request);
    }
}