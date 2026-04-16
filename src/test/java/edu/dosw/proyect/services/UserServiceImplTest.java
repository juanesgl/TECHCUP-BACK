package edu.dosw.proyect.services;

import edu.dosw.proyect.controllers.dtos.request.RegisterRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.RegisterResponseDTO;
import edu.dosw.proyect.core.services.impl.UserServiceImpl;
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
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserPersistenceMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private RegisterRequestDTO buildRequest(String role) {
        return new RegisterRequestDTO(
                "Test User",
                "test@mail.escuelaing.edu.co",
                "pass123", role, "Delantero", 4);
    }

    @Test
    void registerUser_HappyPath_RetornaResponse() {
        RegisterRequestDTO request = buildRequest("STUDENT");
        UserEntity saved = new UserEntity();
        saved.setId(1L);
        saved.setEmail("test@mail.escuelaing.edu.co");
        saved.setRole("STUDENT");

        when(passwordEncoder.encode(any())).thenReturn("hashedPass");
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
        when(userMapper.toEntity(any())).thenReturn(saved);
        when(userRepository.save(any())).thenReturn(saved);

        RegisterResponseDTO response = userService.registerUser(request);

        assertNotNull(response);
        assertEquals(1L, response.getUserId());
        verify(userRepository, times(1)).save(any());
    }

    @Test
    void registerUser_RolInvalido_LanzaException() {
        RegisterRequestDTO request = buildRequest("ROL_INVALIDO");
        assertThrows(IllegalArgumentException.class,
                () -> userService.registerUser(request));
    }

    @Test
    void registerUser_CorreoYaRegistrado_LanzaException() {
        RegisterRequestDTO request = buildRequest("STUDENT");
        UserEntity existing = new UserEntity();
        existing.setEmail("test@mail.escuelaing.edu.co");

        when(userRepository.findByEmail(any())).thenReturn(Optional.of(existing));

        assertThrows(IllegalStateException.class,
                () -> userService.registerUser(request));
    }

    @Test
    void registerUser_CorreoInvalidoParaRol_LanzaException() {
        RegisterRequestDTO request = new RegisterRequestDTO(
                "Test", "test@gmail.com", "pass", "STUDENT", null, 1);
        assertThrows(IllegalArgumentException.class,
                () -> userService.registerUser(request));
    }
}