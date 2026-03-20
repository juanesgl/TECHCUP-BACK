package edu.dosw.proyect.services;

import edu.dosw.proyect.dtos.DisponibilidadRequestDTO;
import edu.dosw.proyect.dtos.DisponibilidadResponseDTO;
import edu.dosw.proyect.exceptions.DisponibilidadException;
import edu.dosw.proyect.repositories.JugadorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JugadorServiceTest {

    private JugadorService jugadorService;

    @BeforeEach
    void setUp() {
        JugadorRepository repository = new JugadorRepository();
        jugadorService = new JugadorService(repository);
    }

    @Test
    void testActivarDisponibilidadExito() {
        DisponibilidadRequestDTO request = new DisponibilidadRequestDTO(true);
        DisponibilidadResponseDTO response = jugadorService.actualizarDisponibilidad(1L, request);

        assertTrue(response.getEstadoFinal());
        assertEquals("Ahora estas visible para los capitanes.", response.getMensaje());
    }

    @Test
    void testActivarDisponibilidadRN01FallaPerfilIncompleto() {
        DisponibilidadRequestDTO request = new DisponibilidadRequestDTO(true);

        DisponibilidadException exception = assertThrows(DisponibilidadException.class, () -> {
            jugadorService.actualizarDisponibilidad(2L, request);
        });

        assertEquals("Para marcarte como disponible, el perfil deportivo debe estar completo.", exception.getMessage());
    }

    @Test
    void testActivarDisponibilidadRN02FallaPorqueTieneEquipo() {
        DisponibilidadRequestDTO request = new DisponibilidadRequestDTO(true);

        DisponibilidadException exception = assertThrows(DisponibilidadException.class, () -> {
            jugadorService.actualizarDisponibilidad(3L, request);
        });

        assertEquals("NO puedes marcarte como disponible porque ya perteneces a un equipo", exception.getMessage());
    }

    @Test
    void testUnirseAEquipoAplicaRN03() {
        jugadorService.unirseAEquipo(1L, 99L);
        
        DisponibilidadRequestDTO request = new DisponibilidadRequestDTO(true);

        assertThrows(DisponibilidadException.class, () -> {
            jugadorService.actualizarDisponibilidad(1L, request);
        });
    }
}
