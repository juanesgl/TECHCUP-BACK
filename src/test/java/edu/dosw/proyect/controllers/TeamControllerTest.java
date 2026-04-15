package edu.dosw.proyect.controllers;

import edu.dosw.proyect.controllers.dtos.request.CreateTeamRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.CreateTeamResponseDTO;
import edu.dosw.proyect.core.services.TeamService;
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
class TeamControllerTest {

    @Mock
    private TeamService teamService;

    @InjectMocks
    private TeamController teamController;

    @Test
    void crearEquipo_HappyPath_RetornaCreated() {
        CreateTeamRequestDTO request = new CreateTeamRequestDTO();
        request.setNombreEquipo("Alpha FC");
        request.setJugadoresInvitadosIds(List.of(1L, 2L, 3L, 4L, 5L, 6L));

        CreateTeamResponseDTO response = CreateTeamResponseDTO.builder()
                .mensajeConfirmacion("Equipo creado exitosamente")
                .notificacionesEnviadas(List.of())
                .build();

        when(teamService.crearEquipo(1L, request)).thenReturn(response);

        ResponseEntity<CreateTeamResponseDTO> result =
                teamController.crearEquipo(1L, request);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertNotNull(result.getBody());
        verify(teamService, times(1)).crearEquipo(1L, request);
    }
}