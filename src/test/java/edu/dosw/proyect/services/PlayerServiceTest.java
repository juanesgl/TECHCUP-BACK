package edu.dosw.proyect.services;

import edu.dosw.proyect.controllers.dtos.PlayerFilterRequest;
import edu.dosw.proyect.controllers.dtos.PlayerResponse;
import edu.dosw.proyect.core.exceptions.BusinessException;
import edu.dosw.proyect.core.models.Jugador;
import edu.dosw.proyect.core.repositories.JugadorRepository;
import edu.dosw.proyect.core.services.PlayerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.eq;

class PlayerServiceTest {

    private PlayerService playerService;
    private JugadorRepository jugadorRepository;

    @BeforeEach
    void setUp() {
        jugadorRepository = mock(JugadorRepository.class);
        playerService = new PlayerService(jugadorRepository);
        lenient().when(jugadorRepository.filterPlayers(any(), any(), any(), any(), any()))
                .thenReturn(java.util.Collections.emptyList());
    }

    @Test
    void filterPlayers_PorNombre_Success() {
        PlayerFilterRequest request = new PlayerFilterRequest();
        request.setName("Carlos");

        Jugador mockJugador = mock(Jugador.class);
        edu.dosw.proyect.core.models.User mockUser = mock(edu.dosw.proyect.core.models.User.class);
        lenient().when(mockJugador.getUsuario()).thenReturn(mockUser);
        lenient().when(mockUser.getName()).thenReturn("Carlos");
        lenient().when(mockJugador.getPosiciones()).thenReturn("Portero");
        lenient().when(mockJugador.getEdad()).thenReturn(20);

        when(jugadorRepository.filterPlayers(eq("Carlos"), any(), any(), any(), any()))
                .thenReturn(java.util.List.of(mockJugador));

        java.util.List<PlayerResponse> result = playerService.filterPlayers(request);

        assertFalse(result.isEmpty());
        assertTrue(result.get(0).getName().contains("Carlos"));
    }

    @Test
    void filterPlayers_PorPosicion_Success() {
        PlayerFilterRequest request = new PlayerFilterRequest();
        request.setPosition("Delantero");

        Jugador mockJugador = mock(Jugador.class);
        edu.dosw.proyect.core.models.User mockUser = mock(edu.dosw.proyect.core.models.User.class);
        lenient().when(mockJugador.getUsuario()).thenReturn(mockUser);
        lenient().when(mockUser.getName()).thenReturn("Carlos");
        lenient().when(mockJugador.getPosiciones()).thenReturn("Delantero");
        lenient().when(mockJugador.getEdad()).thenReturn(22);

        when(jugadorRepository.filterPlayers(any(), eq("Delantero"), any(), any(), any()))
                .thenReturn(java.util.List.of(mockJugador));

        java.util.List<PlayerResponse> result = playerService.filterPlayers(request);

        assertFalse(result.isEmpty());
        result.forEach(p -> assertEquals("Delantero", p.getPosition()));
    }

    @Test
    void filterPlayers_PorEdad_Success() {
        PlayerFilterRequest request = new PlayerFilterRequest();
        request.setAge(22);

        Jugador mockJugador = mock(Jugador.class);
        edu.dosw.proyect.core.models.User mockUser = mock(edu.dosw.proyect.core.models.User.class);
        lenient().when(mockJugador.getUsuario()).thenReturn(mockUser);
        lenient().when(mockUser.getName()).thenReturn("Carlos");
        lenient().when(mockJugador.getPosiciones()).thenReturn("Delantero");
        lenient().when(mockJugador.getEdad()).thenReturn(22);

        when(jugadorRepository.filterPlayers(any(), any(), any(), any(), eq(22)))
                .thenReturn(java.util.List.of(mockJugador));

        java.util.List<PlayerResponse> result = playerService.filterPlayers(request);

        assertFalse(result.isEmpty());
        result.forEach(p -> assertEquals(22, p.getAge()));
    }

    @Test
    void filterPlayers_PorNombreYPosicion_Success() {
        PlayerFilterRequest request = new PlayerFilterRequest();
        request.setName("Carlos");
        request.setPosition("Delantero");

        when(jugadorRepository.filterPlayers(eq("Carlos"), eq("Delantero"), any(), any(), any()))
                .thenReturn(java.util.List.of(new Jugador()));
        java.util.List<PlayerResponse> result = playerService.filterPlayers(request);

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

        when(jugadorRepository.filterPlayers(eq("XYZ123"), any(), any(), any(), any()))
                .thenReturn(java.util.Collections.emptyList());
        BusinessException ex = assertThrows(BusinessException.class,
                () -> playerService.filterPlayers(request));

        assertEquals("No se encontraron jugadores con los filtros indicados", ex.getMessage());
    }

    @Test
    void filterPlayers_NombreEnMayusculas_Success() {
        PlayerFilterRequest request = new PlayerFilterRequest();
        request.setName("CARLOS");

        when(jugadorRepository.filterPlayers(eq("CARLOS"), any(), any(), any(), any()))
                .thenReturn(java.util.List.of(new Jugador()));
        java.util.List<PlayerResponse> result = playerService.filterPlayers(request);

        assertFalse(result.isEmpty());
    }

    @Test
    void filterPlayers_NombreParcial_Success() {
        PlayerFilterRequest request = new PlayerFilterRequest();
        request.setName("car");

        when(jugadorRepository.filterPlayers(eq("car"), any(), any(), any(), any()))
                .thenReturn(java.util.List.of(new Jugador()));
        java.util.List<PlayerResponse> result = playerService.filterPlayers(request);

        assertFalse(result.isEmpty());
    }
}
