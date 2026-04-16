package edu.dosw.proyect.controllers.dtos.responses;

import edu.dosw.proyect.controllers.dtos.response.DisponibilidadResponseDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DisponibilidadResponseTest {

    @Test
    void disponibilidadResponseDTO_AllArgsConstructor_ConstruyeCorrectamente() {
        DisponibilidadResponseDTO dto = new DisponibilidadResponseDTO(
                "Disponibilidad actualizada", true);

        assertEquals("Disponibilidad actualizada", dto.getMensaje());
        assertTrue(dto.getEstadoFinal());
    }

    @Test
    void disponibilidadResponseDTO_NoArgsConstructor_CreaVacio() {
        DisponibilidadResponseDTO dto = new DisponibilidadResponseDTO();
        assertNull(dto.getMensaje());
        assertNull(dto.getEstadoFinal());
    }

    @Test
    void disponibilidadResponseDTO_Setters_FuncionanCorrectamente() {
        DisponibilidadResponseDTO dto = new DisponibilidadResponseDTO();
        dto.setMensaje("No disponible");
        dto.setEstadoFinal(false);

        assertEquals("No disponible", dto.getMensaje());
        assertFalse(dto.getEstadoFinal());
    }
}
