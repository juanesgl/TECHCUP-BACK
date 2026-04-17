package edu.dosw.proyect.controllers;

import edu.dosw.proyect.controllers.dtos.request.CreateTeamRequestDTO;
import edu.dosw.proyect.controllers.dtos.request.UpdateTeamRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.CreateTeamResponseDTO;
import edu.dosw.proyect.controllers.dtos.response.TeamResponseDTO;
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

    @Mock private TeamService teamService;
    @InjectMocks private TeamController teamController;

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

        ResponseEntity<CreateTeamResponseDTO> result = teamController.crearEquipo(1L, request);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertNotNull(result.getBody());
        verify(teamService, times(1)).crearEquipo(1L, request);
    }

    @Test
    void consultarEquipo_HappyPath_RetornaOk() {
        TeamResponseDTO response = new TeamResponseDTO();
        when(teamService.consultarEquipo(1L)).thenReturn(response);

        ResponseEntity<TeamResponseDTO> result = teamController.consultarEquipo(1L);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        verify(teamService).consultarEquipo(1L);
    }

    @Test
    void consultarEquiposPorTorneo_HappyPath_RetornaLista() {
        List<TeamResponseDTO> lista = List.of(new TeamResponseDTO(), new TeamResponseDTO());
        when(teamService.consultarEquiposPorTorneo("TOURN-1")).thenReturn(lista);

        ResponseEntity<List<TeamResponseDTO>> result =
                teamController.consultarEquiposPorTorneo("TOURN-1");

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(2, result.getBody().size());
        verify(teamService).consultarEquiposPorTorneo("TOURN-1");
    }

    @Test
    void consultarJugadores_HappyPath_RetornaLista() {
        List<String> jugadores = List.of("Juan", "Pedro");
        when(teamService.consultarJugadoresEquipo(1L)).thenReturn(jugadores);

        ResponseEntity<List<String>> result = teamController.consultarJugadores(1L);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(2, result.getBody().size());
        verify(teamService).consultarJugadoresEquipo(1L);
    }

    @Test
    void actualizarEquipo_HappyPath_RetornaOk() {
        UpdateTeamRequestDTO request = new UpdateTeamRequestDTO();
        TeamResponseDTO response = new TeamResponseDTO();
        when(teamService.actualizarEquipo(1L, 2L, request)).thenReturn(response);

        ResponseEntity<TeamResponseDTO> result =
                teamController.actualizarEquipo(1L, 2L, request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        verify(teamService).actualizarEquipo(1L, 2L, request);
    }

    @Test
    void eliminarEquipo_HappyPath_RetornaNoContent() {
        doNothing().when(teamService).eliminarEquipo(1L, 2L);

        ResponseEntity<Void> result = teamController.eliminarEquipo(1L, 2L);

        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        verify(teamService).eliminarEquipo(1L, 2L);
    }
}