package edu.dosw.proyect.services;

import edu.dosw.proyect.controllers.dtos.LoginRequestDTO;
import edu.dosw.proyect.controllers.dtos.LoginResponseDTO;
import edu.dosw.proyect.core.models.User;
import edu.dosw.proyect.core.repositories.UserRepository;
import edu.dosw.proyect.core.services.impl.AuthServiceImpl;
import edu.dosw.proyect.core.services.jwt.JwtProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private PasswordEncoder passwordEncoder;

    private AuthServiceImpl authService;

    @BeforeEach
    void setUp() {
        authService = new AuthServiceImpl(userRepository, jwtProvider, passwordEncoder);
    }

    @Test
    void testSuccessfulLoginAdmin() {
        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail("admin@techcup.com");
        request.setPassword("admin123");

        User admin = User.builder()
                .id(1L)
                .name("Admin")
                .email("admin@techcup.com")
                .password("admin123")
                .role("ADMINISTRATOR")
                .registrationDate(LocalDateTime.now())
                .active(true)
                .build();

        when(userRepository.findByEmail("admin@techcup.com")).thenReturn(Optional.of(admin));
        when(passwordEncoder.matches("admin123", "admin123")).thenReturn(true);
        when(jwtProvider.generateToken("admin@techcup.com", "ADMINISTRATOR", 1L)).thenReturn("mock-jwt-token");

        LoginResponseDTO response = authService.loginUser(request);

        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals("Inicio de sesiÃ³n exitoso", response.getMessage());
        assertNotNull(response.getToken());
    }

    @Test
    void testSuccessfulLoginUser() {
        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail("user@gmail.com");
        request.setPassword("user123");

        User user = User.builder()
                .id(2L)
                .name("User")
                .email("user@gmail.com")
                .password("user123")
                .role("PLAYER")
                .registrationDate(LocalDateTime.now())
                .active(true)
                .build();

        when(userRepository.findByEmail("user@gmail.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("user123", "user123")).thenReturn(true);
        when(jwtProvider.generateToken("user@gmail.com", "PLAYER", 2L)).thenReturn("mock-jwt-token");

        LoginResponseDTO response = authService.loginUser(request);

        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals("Inicio de sesiÃ³n exitoso", response.getMessage());
        assertNotNull(response.getToken());
    }

    @Test
    void testInvalidCredentials() {
        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail("admin@techcup.com");
        request.setPassword("wrongpassword");

        when(userRepository.findByEmail("admin@techcup.com")).thenReturn(Optional.empty());

        LoginResponseDTO response = authService.loginUser(request);

        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertEquals("Credenciales invÃ¡lidas", response.getMessage());
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

