package edu.dosw.proyect.services;

import edu.dosw.proyect.controllers.dtos.DisponibilidadRequestDTO;
import edu.dosw.proyect.controllers.dtos.DisponibilidadResponseDTO;
import edu.dosw.proyect.core.exceptions.DisponibilidadException;
import edu.dosw.proyect.core.models.Jugador;
import edu.dosw.proyect.core.services.JugadorService;
import edu.dosw.proyect.core.repositories.JugadorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JugadorServiceTest {

    @Mock
    private JugadorRepository jugadorRepository;

    @InjectMocks
    private JugadorService jugadorService;

    private Jugador jugadorBasico;

    @BeforeEach
    void setUp() {
        jugadorBasico = new Jugador(1L, "Jugador Test", true, false, false);
    }

    @Test
    void testActivarDisponibilidadExito() {
        when(jugadorRepository.findById(1L)).thenReturn(Optional.of(jugadorBasico));
        when(jugadorRepository.save(any(Jugador.class))).thenReturn(jugadorBasico);

        DisponibilidadRequestDTO request = new DisponibilidadRequestDTO(true);
        DisponibilidadResponseDTO response = jugadorService.actualizarDisponibilidad(1L, request);

        assertTrue(response.getEstadoFinal());
        assertEquals("Ahora estas visible para los capitanes.", response.getMensaje());
        verify(jugadorRepository, times(1)).save(any(Jugador.class));
    }

    @Test
    void testActivarDisponibilidadRN01FallaPerfilIncompleto() {
        jugadorBasico.setPerfilCompleto(false); 
        when(jugadorRepository.findById(1L)).thenReturn(Optional.of(jugadorBasico));

        DisponibilidadRequestDTO request = new DisponibilidadRequestDTO(true);

        DisponibilidadException exception = assertThrows(DisponibilidadException.class, () -> {
            jugadorService.actualizarDisponibilidad(1L, request);
        });

        assertEquals("Para marcarte como disponible, el perfil deportivo debe estar completo.", exception.getMessage());
        verify(jugadorRepository, never()).save(any(Jugador.class));
    }

    @Test
    void testActivarDisponibilidadRN02FallaPorqueTieneEquipo() {
        jugadorBasico.setTieneEquipo(true); 
        when(jugadorRepository.findById(1L)).thenReturn(Optional.of(jugadorBasico));

        DisponibilidadRequestDTO request = new DisponibilidadRequestDTO(true);

        DisponibilidadException exception = assertThrows(DisponibilidadException.class, () -> {
            jugadorService.actualizarDisponibilidad(1L, request);
        });

        assertEquals("NO puedes marcarte como disponible porque ya perteneces a un equipo", exception.getMessage());
        verify(jugadorRepository, never()).save(any(Jugador.class));
    }

    @Test
    void testUnirseAEquipoAplicaRN03() {
        when(jugadorRepository.findById(1L)).thenReturn(Optional.of(jugadorBasico));

        jugadorService.unirseAEquipo(1L, 99L);
        
        assertTrue(jugadorBasico.isTieneEquipo());
        assertFalse(jugadorBasico.isDisponible());
        
        verify(jugadorRepository, times(1)).save(jugadorBasico);
    }

    @Test
    void testActualizarDisponibilidadFallaJugadorNoExistente() {
        when(jugadorRepository.findById(99L)).thenReturn(Optional.empty());

        DisponibilidadRequestDTO request = new DisponibilidadRequestDTO(true);

        DisponibilidadException exception = assertThrows(DisponibilidadException.class, () -> {
            jugadorService.actualizarDisponibilidad(99L, request);
        });

        assertEquals("El jugador especificado no existe", exception.getMessage());
        verify(jugadorRepository, never()).save(any(Jugador.class));
    }
}

