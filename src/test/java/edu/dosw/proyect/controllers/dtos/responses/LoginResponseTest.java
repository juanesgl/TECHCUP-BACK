package edu.dosw.proyect.controllers.dtos.responses;

import edu.dosw.proyect.controllers.dtos.response.LoginResponseDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LoginResponseTest {

    @Test
    void loginResponseDTO_AllArgsConstructor_ConstruyeCorrectamente() {
        LoginResponseDTO dto = new LoginResponseDTO("Login exitoso", true, "token123");

        assertEquals("Login exitoso", dto.getMessage());
        assertTrue(dto.isSuccess());
        assertEquals("token123", dto.getToken());
    }

    @Test
    void loginResponseDTO_NoArgsConstructor_CreaVacio() {
        LoginResponseDTO dto = new LoginResponseDTO();
        assertNull(dto.getMessage());
        assertFalse(dto.isSuccess());
        assertNull(dto.getToken());
    }

    @Test
    void loginResponseDTO_Setters_FuncionanCorrectamente() {
        LoginResponseDTO dto = new LoginResponseDTO();
        dto.setMessage("Credenciales invalidas");
        dto.setSuccess(false);
        dto.setToken(null);

        assertEquals("Credenciales invalidas", dto.getMessage());
        assertFalse(dto.isSuccess());
    }

    @Test
    void loginResponseDTO_AllArgs_ConstruyeCorrectamente() {
        LoginResponseDTO dto = new LoginResponseDTO("Login exitoso", true, "token123");
        assertEquals("Login exitoso", dto.getMessage());
        assertTrue(dto.isSuccess());
        assertEquals("token123", dto.getToken());
    }

    @Test
    void loginResponseDTO_NoArgs_CreaVacio() {
        LoginResponseDTO dto = new LoginResponseDTO();
        assertNull(dto.getMessage());
        assertFalse(dto.isSuccess());
        assertNull(dto.getToken());
    }

}
