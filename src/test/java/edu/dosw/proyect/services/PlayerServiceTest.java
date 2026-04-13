package edu.dosw.proyect.services;

import edu.dosw.proyect.controllers.dtos.request.PlayerFilterRequest;
import edu.dosw.proyect.controllers.dtos.response.PlayerResponse;
import edu.dosw.proyect.core.exceptions.BusinessException;
import edu.dosw.proyect.persistence.entity.JugadorEntity;
import edu.dosw.proyect.persistence.entity.UserEntity;
import edu.dosw.proyect.persistence.repository.JugadorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlayerServiceTest {

    @Mock
    private JugadorRepository jugadorRepository;

    @InjectMocks
    private edu.dosw.proyect.core.services.PlayerService playerService;

    private JugadorEntity buildJugadorEntity(Long id, String nombre) {
        UserEntity user = new UserEntity();
        user.setId(id);
        user.setName(nombre);

        JugadorEntity j = new JugadorEntity();
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

        JugadorEntity entity = buildJugadorEntity(1L, "Juan");

        when(jugadorRepository.filterPlayers(any(), any(), any(), any(), any()))
                .thenReturn(List.of(entity));

        List<PlayerResponse> result = playerService.filterPlayers(request);

        assertEquals(1, result.size());
        assertEquals("Juan", result.get(0).getName());
    }

    @Test
    void filterPlayers_SinFiltros_LanzaException() {
        PlayerFilterRequest request = new PlayerFilterRequest();
        assertThrows(BusinessException.class,
                () -> playerService.filterPlayers(request));
    }

    @Test
    void filterPlayers_SinResultados_LanzaException() {
        PlayerFilterRequest request = new PlayerFilterRequest();
        request.setPosition("Portero");

        when(jugadorRepository.filterPlayers(any(), any(), any(), any(), any()))
                .thenReturn(List.of());

        assertThrows(BusinessException.class,
                () -> playerService.filterPlayers(request));
    }
}