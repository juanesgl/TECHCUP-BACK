package edu.dosw.proyect.utils;

import edu.dosw.proyect.controllers.dtos.request.AvailabilityRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.AvailabilityResponseDTO;
import edu.dosw.proyect.core.models.Player;
import edu.dosw.proyect.core.utils.AvailabilityMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AvailabilityMapperTest {

    @Test
    void mapRequestToStatus_Disponible_RetornaTrue() {
        AvailabilityRequestDTO dto = new AvailabilityRequestDTO();
        dto.setEstadoDisponibilidad(true);

        assertTrue(AvailabilityMapper.mapRequestToStatus(dto));
    }

    @Test
    void mapRequestToStatus_NoDisponible_RetornaFalse() {
        AvailabilityRequestDTO dto = new AvailabilityRequestDTO();
        dto.setEstadoDisponibilidad(false);

        assertFalse(AvailabilityMapper.mapRequestToStatus(dto));
    }

    @Test
    void mapRequestToStatus_DtoNulo_RetornaFalse() {
        assertFalse(AvailabilityMapper.mapRequestToStatus(null));
    }

    @Test
    void mapRequestToStatus_EstadoNulo_RetornaFalse() {
        AvailabilityRequestDTO dto = new AvailabilityRequestDTO();
        dto.setEstadoDisponibilidad(null);

        assertFalse(AvailabilityMapper.mapRequestToStatus(dto));
    }

    @Test
    void mapToResponse_JugadorDisponible_RetornaTrue() {
        Player jugador = new Player();
        jugador.setDisponible(true);

        AvailabilityResponseDTO response =
                AvailabilityMapper.mapToResponse(jugador, "Disponible");

        assertEquals("Disponible", response.getMensaje());
        assertTrue(response.getEstadoFinal());
    }

    @Test
    void mapToResponse_JugadorNoDisponible_RetornaFalse() {
        Player jugador = new Player();
        jugador.setDisponible(false);

        AvailabilityResponseDTO response =
                AvailabilityMapper.mapToResponse(jugador, "No disponible");

        assertEquals("No disponible", response.getMensaje());
        assertFalse(response.getEstadoFinal());
    }

    @Test
    void mapToResponse_JugadorNulo_RetornaFalseConMensaje() {
        AvailabilityResponseDTO response =
                AvailabilityMapper.mapToResponse(null, "Error");

        assertEquals("Error", response.getMensaje());
        assertFalse(response.getEstadoFinal());
    }
}
