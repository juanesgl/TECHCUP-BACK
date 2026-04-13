package edu.dosw.proyect.services;

import edu.dosw.proyect.controllers.dtos.request.LoginRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.LoginResponseDTO;
import edu.dosw.proyect.core.security.JwtProvider;
import edu.dosw.proyect.core.services.impl.AuthServiceImpl;
import edu.dosw.proyect.persistence.entity.UserEntity;
import edu.dosw.proyect.persistence.mapper.UserPersistenceMapper;
import edu.dosw.proyect.persistence.repository.UserRepository;
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

    @Mock private UserRepository userRepository;
    @Mock private JwtProvider jwtProvider;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private UserPersistenceMapper userMapper;

    @InjectMocks
    private AuthServiceImpl authService;

    private UserEntity buildUserEntity() {
        UserEntity e = new UserEntity();
        e.setId(1L);
        e.setEmail("admin@mail.escuelaing.edu.co");
        e.setPassword("hashedPass");
        e.setRole("ADMIN");
        e.setName("Admin");
        return e;
    }

    @Test
    void loginUser_HappyPath_RetornaToken() {
        LoginRequestDTO request = new LoginRequestDTO(
                "admin@mail.escuelaing.edu.co", "admin123");
        UserEntity entity = buildUserEntity();

        when(userRepository.findByEmail(request.getEmail()))
                .thenReturn(Optional.of(entity));
        when(passwordEncoder.matches("admin123", "hashedPass")).thenReturn(true);
        when(jwtProvider.generateToken(any(), any(), any())).thenReturn("jwt-token");

        LoginResponseDTO result = authService.loginUser(request);

        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals("Bienvenido Admin", result.getMessage());
        assertEquals("jwt-token", result.getToken());
    }

    @Test
    void loginUser_CredencialesInvalidas_RetornaFalse() {
        LoginRequestDTO request = new LoginRequestDTO(
                "admin@mail.escuelaing.edu.co", "wrongpass");
        UserEntity entity = buildUserEntity();

        when(userRepository.findByEmail(request.getEmail()))
                .thenReturn(Optional.of(entity));
        when(passwordEncoder.matches("wrongpass", "hashedPass")).thenReturn(false);

        LoginResponseDTO result = authService.loginUser(request);

        assertFalse(result.isSuccess());
        assertEquals("Correo o contraseña incorrectos", result.getMessage());
        assertNull(result.getToken());
    }

    @Test
    void loginUser_UsuarioNoExiste_RetornaFalse() {
        LoginRequestDTO request = new LoginRequestDTO(
                "noexiste@mail.escuelaing.edu.co", "password1");

        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());

        LoginResponseDTO result = authService.loginUser(request);

        assertFalse(result.isSuccess());
        assertEquals("Correo o contraseña incorrectos", result.getMessage());
    }
}