package edu.dosw.proyect.services;

import edu.dosw.proyect.controllers.dtos.request.PlayerFilterRequest;
import edu.dosw.proyect.controllers.dtos.response.PlayerResponse;
import edu.dosw.proyect.core.exceptions.BusinessException;
import edu.dosw.proyect.core.services.PlayerFilterService;
import edu.dosw.proyect.persistence.entity.PlayerEntity;
import edu.dosw.proyect.persistence.entity.UserEntity;
import edu.dosw.proyect.persistence.repository.PlayerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlayerFilterServiceTest {

    @Mock
    private PlayerRepository playerRepository;

    @InjectMocks
    private PlayerFilterService playerFilterService;

    private PlayerEntity buildJugadorEntity(Long id, String nombre) {
        UserEntity user = new UserEntity();
        user.setId(id);
        user.setName(nombre);

        PlayerEntity j = new PlayerEntity();
        j.setId(id);
        j.setUsuario(user);
        j.setPosiciones("Delantero");
        j.setEdad(22);
        j.setDisponible(true);
        return j;
    }

    @Test
    void filterPlayers_HappyPath_RetornaLista() {
        PlayerFilterRequest request = new PlayerFilterRequest();
        request.setPosition("Delantero");

        PlayerEntity entity = buildJugadorEntity(1L, "Juan");

        when(playerRepository.filterPlayers(any(), any(), any(), any(), any()))
                .thenReturn(List.of(entity));

        List<PlayerResponse> result = playerFilterService.filterPlayers(request);

        assertEquals(1, result.size());
        assertEquals("Juan", result.get(0).getName());
    }

    @Test
    void filterPlayers_SinFiltros_LanzaException() {
        PlayerFilterRequest request = new PlayerFilterRequest();
        assertThrows(BusinessException.class,
                () -> playerFilterService.filterPlayers(request));
    }

    @Test
    void filterPlayers_SinResultados_LanzaException() {
        PlayerFilterRequest request = new PlayerFilterRequest();
        request.setPosition("Portero");

        when(playerRepository.filterPlayers(any(), any(), any(), any(), any()))
                .thenReturn(List.of());

        assertThrows(BusinessException.class,
                () -> playerFilterService.filterPlayers(request));
    }
}