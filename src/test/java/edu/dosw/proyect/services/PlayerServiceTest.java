package edu.dosw.proyect.services;

import edu.dosw.proyect.controllers.dtos.PlayerFilterRequest;
import edu.dosw.proyect.controllers.dtos.PlayerResponse;
import edu.dosw.proyect.core.exceptions.BusinessException;
import edu.dosw.proyect.core.services.PlayerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayerServiceTest {

    private PlayerService playerService;

    @BeforeEach
    void setUp() {
        playerService = new PlayerService();
    }

    @Test
    void filterPlayers_PorNombre_Success() {
        PlayerFilterRequest request = new PlayerFilterRequest();
        request.setName("Carlos");

        List<PlayerResponse> result = playerService.filterPlayers(request);

        assertFalse(result.isEmpty());
        assertTrue(result.get(0).getName().contains("Carlos"));
    }

    @Test
    void filterPlayers_PorPosicion_Success() {
        PlayerFilterRequest request = new PlayerFilterRequest();
        request.setPosition("Delantero");

        List<PlayerResponse> result = playerService.filterPlayers(request);

        assertFalse(result.isEmpty());
        result.forEach(p -> assertEquals("Delantero", p.getPosition()));
    }

    @Test
    void filterPlayers_PorEdad_Success() {
        PlayerFilterRequest request = new PlayerFilterRequest();
        request.setAge(22);

        List<PlayerResponse> result = playerService.filterPlayers(request);

        assertFalse(result.isEmpty());
        result.forEach(p -> assertEquals(22, p.getAge()));
    }

    @Test
    void filterPlayers_PorNombreYPosicion_Success() {
        PlayerFilterRequest request = new PlayerFilterRequest();
        request.setName("Carlos");
        request.setPosition("Delantero");

        List<PlayerResponse> result = playerService.filterPlayers(request);

        assertFalse(result.isEmpty());
    }

    @Test
    void filterPlayers_SinFiltros_LanzaExcepcion() {
        PlayerFilterRequest request = new PlayerFilterRequest();

        BusinessException ex = assertThrows(BusinessException.class,
                () -> playerService.filterPlayers(request));

        assertEquals("Debe proporcionar al menos un filtro de busqueda", ex.getMessage());
    }

    @Test
    void filterPlayers_SinResultados_LanzaExcepcion() {
        PlayerFilterRequest request = new PlayerFilterRequest();
        request.setName("XYZ123");

        BusinessException ex = assertThrows(BusinessException.class,
                () -> playerService.filterPlayers(request));

        assertEquals("No se encontraron jugadores con los filtros indicados", ex.getMessage());
    }

    @Test
    void filterPlayers_NombreEnMayusculas_Success() {
        PlayerFilterRequest request = new PlayerFilterRequest();
        request.setName("CARLOS");

        List<PlayerResponse> result = playerService.filterPlayers(request);

        assertFalse(result.isEmpty());
    }

    @Test
    void filterPlayers_NombreParcial_Success() {
        PlayerFilterRequest request = new PlayerFilterRequest();
        request.setName("car");

        List<PlayerResponse> result = playerService.filterPlayers(request);

        assertFalse(result.isEmpty());
    }
}