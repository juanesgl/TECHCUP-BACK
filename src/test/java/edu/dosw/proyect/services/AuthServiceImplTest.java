package edu.dosw.proyect.services;

import edu.dosw.proyect.controllers.dtos.LoginRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.LoginResponseDTO;
import edu.dosw.proyect.core.models.User;
import edu.dosw.proyect.persistence.repository.UserRepository;
import edu.dosw.proyect.core.services.impl.AuthServiceImpl;
import edu.dosw.proyect.core.security.JwtProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthServiceImpl authService;

    private User buildUser() {
        return User.builder()
                .id(1L)
                .name("Juan")
                .email("juan@mail.com")
                .password("hashedPass")
                .role("PLAYER")
                .active(true)
                .build();
    }

    @Test
    void loginUser_HappyPath_RetornaTokenYExito() {
        LoginRequestDTO request = new LoginRequestDTO("juan@mail.com", "pass123");
        User user = buildUser();

        when(userRepository.findByEmail("juan@mail.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("pass123", "hashedPass")).thenReturn(true);
        when(jwtProvider.generateToken("juan@mail.com", "PLAYER", 1L)).thenReturn("token123");

        LoginResponseDTO result = authService.loginUser(request);

        assertTrue(result.isSuccess());
        assertEquals("token123", result.getToken());
        verify(jwtProvider, times(1)).generateToken("juan@mail.com", "PLAYER", 1L);
    }

    @Test
    void loginUser_PasswordIncorrecta_RetornaFalse() {
        LoginRequestDTO request = new LoginRequestDTO("juan@mail.com", "wrongpass");
        User user = buildUser();

        when(userRepository.findByEmail("juan@mail.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongpass", "hashedPass")).thenReturn(false);

        LoginResponseDTO result = authService.loginUser(request);

        assertFalse(result.isSuccess());
        assertNull(result.getToken());
        verify(jwtProvider, never()).generateToken(any(), any(), any());
    }

    @Test
    void loginUser_UsuarioNoExiste_RetornaFalse() {
        LoginRequestDTO request = new LoginRequestDTO("noexiste@mail.com", "pass123");

        when(userRepository.findByEmail("noexiste@mail.com")).thenReturn(Optional.empty());

        LoginResponseDTO result = authService.loginUser(request);

        assertFalse(result.isSuccess());
        assertNull(result.getToken());
    }

    @Test
    void loginUser_EmailNulo_LanzaIllegalArgument() {
        LoginRequestDTO request = new LoginRequestDTO(null, "pass123");

        assertThrows(IllegalArgumentException.class,
                () -> authService.loginUser(request));
        verify(userRepository, never()).findByEmail(any());
    }

    @Test
    void loginUser_PasswordNula_LanzaIllegalArgument() {
        LoginRequestDTO request = new LoginRequestDTO("juan@mail.com", null);

        assertThrows(IllegalArgumentException.class,
                () -> authService.loginUser(request));
        verify(userRepository, never()).findByEmail(any());
    }
}