package edu.dosw.proyect.controllers;

import edu.dosw.proyect.controllers.dtos.request.PlayerFilterRequest;
import edu.dosw.proyect.controllers.dtos.response.PlayerResponse;
import edu.dosw.proyect.core.exceptions.BusinessException;
import edu.dosw.proyect.core.services.PlayerFilterService;
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
class PlayerFilterControllerTest {

    @Mock
    private PlayerFilterService playerFilterService;

    @InjectMocks
    private PlayerFilterController playerFilterController;

    @Test
    void filterPlayers_HappyPath_RetornaOk() {
        PlayerFilterRequest request = new PlayerFilterRequest();
        request.setPosition("Delantero");

        List<PlayerResponse> response = List.of(
                new PlayerResponse(1L, "Juan", "Delantero", 22));

        when(playerFilterService.filterPlayers(request)).thenReturn(response);

        ResponseEntity<List<PlayerResponse>> result =
                playerFilterController.filterPlayers(request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(1, result.getBody().size());
        verify(playerFilterService, times(1)).filterPlayers(request);
    }

    @Test
    void filterPlayers_SinFiltros_LanzaBusinessException() {
        PlayerFilterRequest request = new PlayerFilterRequest();

        when(playerFilterService.filterPlayers(request))
                .thenThrow(new BusinessException("Debe proporcionar al menos un filtro"));

        assertThrows(BusinessException.class,
                () -> playerFilterController.filterPlayers(request));
    }
}