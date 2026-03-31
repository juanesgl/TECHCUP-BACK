package edu.dosw.proyect.utils;

import edu.dosw.proyect.controllers.dtos.request.DisponibilidadRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.DisponibilidadResponseDTO;
import edu.dosw.proyect.core.models.Jugador;
import edu.dosw.proyect.core.utils.DisponibilidadMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DisponibilidadMapperTest {

    @Test
    void mapRequestToStatus_Disponible_RetornaTrue() {
        DisponibilidadRequestDTO dto = new DisponibilidadRequestDTO();
        dto.setEstadoDisponibilidad(true);

        assertTrue(DisponibilidadMapper.mapRequestToStatus(dto));
    }

    @Test
    void mapRequestToStatus_NoDisponible_RetornaFalse() {
        DisponibilidadRequestDTO dto = new DisponibilidadRequestDTO();
        dto.setEstadoDisponibilidad(false);

        assertFalse(DisponibilidadMapper.mapRequestToStatus(dto));
    }

    @Test
    void mapRequestToStatus_DtoNulo_RetornaFalse() {
        assertFalse(DisponibilidadMapper.mapRequestToStatus(null));
    }

    @Test
    void mapRequestToStatus_EstadoNulo_RetornaFalse() {
        DisponibilidadRequestDTO dto = new DisponibilidadRequestDTO();
        dto.setEstadoDisponibilidad(null);

        assertFalse(DisponibilidadMapper.mapRequestToStatus(dto));
    }

    @Test
    void mapToResponse_JugadorDisponible_RetornaTrue() {
        Jugador jugador = new Jugador();
        jugador.setDisponible(true);

        DisponibilidadResponseDTO response =
                DisponibilidadMapper.mapToResponse(jugador, "Disponible");

        assertEquals("Disponible", response.getMensaje());
        assertTrue(response.getEstadoFinal());
    }

    @Test
    void mapToResponse_JugadorNoDisponible_RetornaFalse() {
        Jugador jugador = new Jugador();
        jugador.setDisponible(false);

        DisponibilidadResponseDTO response =
                DisponibilidadMapper.mapToResponse(jugador, "No disponible");

        assertEquals("No disponible", response.getMensaje());
        assertFalse(response.getEstadoFinal());
    }

    @Test
    void mapToResponse_JugadorNulo_RetornaFalseConMensaje() {
        DisponibilidadResponseDTO response =
                DisponibilidadMapper.mapToResponse(null, "Error");

        assertEquals("Error", response.getMensaje());
        assertFalse(response.getEstadoFinal());
    }
}
