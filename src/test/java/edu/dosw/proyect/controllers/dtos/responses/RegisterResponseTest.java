package edu.dosw.proyect.controllers.dtos.responses;

import edu.dosw.proyect.controllers.dtos.response.RegisterResponseDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class RegisterResponseTest {

    @Test
    void registerResponseDTO_AllArgsConstructor_ConstruyeCorrectamente() {
        RegisterResponseDTO dto = new RegisterResponseDTO("Usuario registrado", 1L);

        assertEquals("Usuario registrado", dto.getMessage());
        assertEquals(1L, dto.getUserId());
    }

    @Test
    void registerResponseDTO_NoArgsConstructor_CreaVacio() {
        RegisterResponseDTO dto = new RegisterResponseDTO();
        assertNull(dto.getMessage());
        assertNull(dto.getUserId());
    }

    @Test
    void registerResponseDTO_Setters_FuncionanCorrectamente() {
        RegisterResponseDTO dto = new RegisterResponseDTO();
        dto.setMessage("Registrado");
        dto.setUserId(5L);

        assertEquals("Registrado", dto.getMessage());
        assertEquals(5L, dto.getUserId());
    }
}
