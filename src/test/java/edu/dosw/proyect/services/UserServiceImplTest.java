package edu.dosw.proyect.services;

import edu.dosw.proyect.controllers.dtos.RegisterRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.RegisterResponseDTO;
import edu.dosw.proyect.core.models.User;
import edu.dosw.proyect.persistence.repository.UserRepository;
import edu.dosw.proyect.core.services.impl.UserServiceImpl;
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

    @InjectMocks
    private UserServiceImpl userService;

    private RegisterRequestDTO buildRequest(String role) {
        return new RegisterRequestDTO(
                "Juan Test",
                "juan@mail.escuelaing.edu.co",
                "pass123",
                role,
                "Delantero",
                8
        );
    }

    private User buildUser(Long id) {
        return User.builder()
                .id(id)
                .name("Juan Test")
                .email("juan@mail.escuelaing.edu.co")
                .password("hashedPass")
                .role("PLAYER")
                .active(true)
                .build();
    }

    @Test
    void registerUser_HappyPath_Student_RetornaCreated() {
        RegisterRequestDTO request = buildRequest("STUDENT");
        User saved = buildUser(1L);

        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(any())).thenReturn("hashedPass");
        when(userRepository.save(any())).thenReturn(saved);

        RegisterResponseDTO result = userService.registerUser(request);

        assertNotNull(result);
        assertEquals(1L, result.getUserId());
        assertEquals("Usuario registrado exitosamente", result.getMessage());
    }

    @Test
    void registerUser_RolInvalido_LanzaIllegalArgument() {
        RegisterRequestDTO request = buildRequest("ROL_INVALIDO");

        assertThrows(IllegalArgumentException.class,
                () -> userService.registerUser(request));
        verify(userRepository, never()).save(any());
    }

    @Test
    void registerUser_RolNulo_LanzaIllegalArgument() {
        RegisterRequestDTO request = buildRequest(null);

        assertThrows(IllegalArgumentException.class,
                () -> userService.registerUser(request));
    }

    @Test
    void registerUser_EmailDuplicado_LanzaIllegalState() {
        RegisterRequestDTO request = buildRequest("STUDENT");
        User existing = buildUser(1L);

        when(userRepository.findByEmail(any())).thenReturn(Optional.of(existing));

        assertThrows(IllegalStateException.class,
                () -> userService.registerUser(request));
        verify(userRepository, never()).save(any());
    }

    @Test
    void registerUser_HappyPath_Admin_RetornaCreated() {
        RegisterRequestDTO request = buildRequest("ADMIN");
        User saved = buildUser(2L);

        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(any())).thenReturn("hashedPass");
        when(userRepository.save(any())).thenReturn(saved);

        RegisterResponseDTO result = userService.registerUser(request);

        assertNotNull(result);
        assertEquals(2L, result.getUserId());
    }

    @Test
    void registerUser_HappyPath_Organizer_RetornaCreated() {
        RegisterRequestDTO request = buildRequest("ORGANIZER");
        User saved = buildUser(3L);

        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(any())).thenReturn("hashedPass");
        when(userRepository.save(any())).thenReturn(saved);

        RegisterResponseDTO result = userService.registerUser(request);

        assertNotNull(result);
        assertEquals(3L, result.getUserId());
    }
}