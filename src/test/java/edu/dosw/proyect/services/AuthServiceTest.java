package edu.dosw.proyect.services;

import edu.dosw.proyect.dtos.LoginRequestDTO;
import edu.dosw.proyect.dtos.LoginResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthServiceTest {

    private AuthService authService;

    @BeforeEach
    void setUp() {
        authService = new AuthService();
    }

    @Test
    void testSuccessfulLoginAdmin() {
        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail("admin@techcup.com");
        request.setPassword("admin123");

        LoginResponseDTO response = authService.loginUser(request);

        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals("Inicio de sesión exitoso", response.getMessage());
        assertNotNull(response.getToken());
    }

    @Test
    void testSuccessfulLoginUser() {
        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail("user@gmail.com");
        request.setPassword("user123");

        LoginResponseDTO response = authService.loginUser(request);

        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals("Inicio de sesión exitoso", response.getMessage());
        assertNotNull(response.getToken());
    }

    @Test
    void testInvalidCredentials() {
        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail("admin@techcup.com");
        request.setPassword("wrongpassword");

        LoginResponseDTO response = authService.loginUser(request);

        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertEquals("Credenciales invalidas", response.getMessage());
        assertNull(response.getToken());
    }

    @Test
    void testNullCredentials() {
        LoginRequestDTO request = new LoginRequestDTO();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            authService.loginUser(request);
        });

        assertTrue(exception.getMessage().contains("requeridos"));
    }
}
