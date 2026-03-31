package edu.dosw.proyect.controllers;

import edu.dosw.proyect.controllers.dtos.PlayerFilterRequest;
import edu.dosw.proyect.controllers.dtos.PlayerResponse;
import edu.dosw.proyect.core.exceptions.BusinessException;
import edu.dosw.proyect.core.services.PlayerService;
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
class PlayerControllerTest {

    @Mock
    private PlayerService playerService;

    @InjectMocks
    private PlayerController playerController;

    @Test
    void filterPlayers_HappyPath_RetornaOk() {
        PlayerFilterRequest request = new PlayerFilterRequest();
        request.setPosition("Delantero");

        List<PlayerResponse> response = List.of(
                new PlayerResponse(1L, "Juan", "Delantero", 22));

        when(playerService.filterPlayers(request)).thenReturn(response);

        ResponseEntity<List<PlayerResponse>> result =
                playerController.filterPlayers(request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(1, result.getBody().size());
        verify(playerService, times(1)).filterPlayers(request);
    }

    @Test
    void filterPlayers_SinFiltros_LanzaBusinessException() {
        PlayerFilterRequest request = new PlayerFilterRequest();

        when(playerService.filterPlayers(request))
                .thenThrow(new BusinessException("Debe proporcionar al menos un filtro"));

        assertThrows(BusinessException.class,
                () -> playerController.filterPlayers(request));
    }
}