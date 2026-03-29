package edu.dosw.proyect.services;

import edu.dosw.proyect.controllers.dtos.LoginRequestDTO;
import edu.dosw.proyect.controllers.dtos.LoginResponseDTO;
import edu.dosw.proyect.core.services.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthServiceTest {

    private AuthService authService;
    private edu.dosw.proyect.core.repositories.UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository = org.mockito.Mockito.mock(edu.dosw.proyect.core.repositories.UserRepository.class);
        authService = new AuthService(userRepository);
    }

    @Test
    void testSuccessfulLoginAdmin() {
        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail("admin@techcup.com");
        request.setPassword("admin123");

        edu.dosw.proyect.core.models.Admin admin = new edu.dosw.proyect.core.models.Admin("Admin", "admin@techcup.com",
                "admin123", null);
        org.mockito.Mockito.when(userRepository.findByEmail("admin@techcup.com"))
                .thenReturn(java.util.Optional.of(admin));

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

        edu.dosw.proyect.core.models.Student user = new edu.dosw.proyect.core.models.Student("User", "user@gmail.com",
                "user123", null);
        org.mockito.Mockito.when(userRepository.findByEmail("user@gmail.com")).thenReturn(java.util.Optional.of(user));

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

        org.mockito.Mockito.when(userRepository.findByEmail("admin@techcup.com"))
                .thenReturn(java.util.Optional.empty());

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
