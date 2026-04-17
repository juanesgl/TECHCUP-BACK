package edu.dosw.proyect.controllers.dtos.responses;

import edu.dosw.proyect.controllers.dtos.response.LoginResponseDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoginResponseTest {

    @Test
    void loginResponseDTO_AllArgsConstructor_ConstruyeCorrectamente() {
        LoginResponseDTO dto = new LoginResponseDTO("Login exitoso", true, "token123", 1L, "Juan", "PLAYER");

        assertEquals("Login exitoso", dto.getMessage());
        assertTrue(dto.isSuccess());
        assertEquals("token123", dto.getToken());
        assertEquals(1L, dto.getUserId());
        assertEquals("Juan", dto.getName());
        assertEquals("PLAYER", dto.getRole());
    }

    @Test
    void loginResponseDTO_NoArgsConstructor_CreaVacio() {
        LoginResponseDTO dto = new LoginResponseDTO();
        assertNull(dto.getMessage());
        assertFalse(dto.isSuccess());
        assertNull(dto.getToken());
        assertNull(dto.getUserId());
        assertNull(dto.getName());
        assertNull(dto.getRole());
    }

    @Test
    void loginResponseDTO_Setters_FuncionanCorrectamente() {
        LoginResponseDTO dto = new LoginResponseDTO();
        dto.setMessage("Credenciales invalidas");
        dto.setSuccess(false);
        dto.setToken(null);
        dto.setUserId(2L);
        dto.setName("Pedro");
        dto.setRole("ADMIN");

        assertEquals("Credenciales invalidas", dto.getMessage());
        assertFalse(dto.isSuccess());
        assertNull(dto.getToken());
        assertEquals(2L, dto.getUserId());
        assertEquals("Pedro", dto.getName());
        assertEquals("ADMIN", dto.getRole());
    }

}
