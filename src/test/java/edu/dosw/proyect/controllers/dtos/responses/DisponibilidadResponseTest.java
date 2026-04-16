package edu.dosw.proyect.controllers.dtos.responses;

import edu.dosw.proyect.controllers.dtos.response.AvailabilityResponseDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DisponibilidadResponseTest {

    @Test
    void disponibilidadResponseDTO_AllArgsConstructor_ConstruyeCorrectamente() {
        AvailabilityResponseDTO dto = new AvailabilityResponseDTO(
                "Disponibilidad actualizada", true);

        assertEquals("Disponibilidad actualizada", dto.getMensaje());
        assertTrue(dto.getEstadoFinal());
    }

    @Test
    void disponibilidadResponseDTO_NoArgsConstructor_CreaVacio() {
        AvailabilityResponseDTO dto = new AvailabilityResponseDTO();
        assertNull(dto.getMensaje());
        assertNull(dto.getEstadoFinal());
    }

    @Test
    void disponibilidadResponseDTO_Setters_FuncionanCorrectamente() {
        AvailabilityResponseDTO dto = new AvailabilityResponseDTO();
        dto.setMensaje("No disponible");
        dto.setEstadoFinal(false);

        assertEquals("No disponible", dto.getMensaje());
        assertFalse(dto.getEstadoFinal());
    }
}
