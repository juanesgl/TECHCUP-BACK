package edu.dosw.proyect.services;

import edu.dosw.proyect.controllers.dtos.RegisterRequestDTO;
import edu.dosw.proyect.controllers.dtos.RegisterResponseDTO;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

        @Mock
        private PasswordEncoder passwordEncoder;

    @InjectMocks
        private UserServiceImpl userService;

    private RegisterRequestDTO buildRequest(String name, String email, String role) {
        RegisterRequestDTO req = new RegisterRequestDTO();
        req.setName(name);
        req.setEmail(email);
        req.setPassword("password123");
        req.setRole(role);
        req.setPreferredPosition("Delantero");
        req.setSkillLevel(8);
        return req;
    }

    @Test
    void registerUser_HappyPath_Student() {
        RegisterRequestDTO request = buildRequest(
                "John Doe",
                "john@mail.escuelaing.edu.co",
                "STUDENT"
        );

        User studentGuardado = User.builder()
                .id(1L)
                .name("John Doe")
                .email("john@mail.escuelaing.edu.co")
                .password("password123")
                .role("PLAYER")
                .academicProgram("IngenierÃ­a")
                .build();

        when(userRepository.save(any(User.class))).thenReturn(studentGuardado);
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(any(String.class))).thenReturn("hashed-password");

        RegisterResponseDTO response = userService.registerUser(request);

        assertNotNull(response);
        assertEquals("Usuario registrado exitosamente", response.getMessage());
        assertEquals(1L, response.getUserId());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void registerUser_HappyPath_Graduate() {
        RegisterRequestDTO request = buildRequest(
                "Jane Graduate",
                "jane@gmail.com",
                "GRADUATE"
        );

        User graduateGuardado = User.builder()
                .id(2L)
                .name("Jane Graduate")
                .email("jane@gmail.com")
                .password("password123")
                .role("PLAYER")
                .academicProgram("IngenierÃ­a")
                .build();

        when(userRepository.save(any(User.class))).thenReturn(graduateGuardado);
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(any(String.class))).thenReturn("hashed-password");

        RegisterResponseDTO response = userService.registerUser(request);

        assertNotNull(response);
        assertEquals("Usuario registrado exitosamente", response.getMessage());
        assertEquals(2L, response.getUserId());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void registerUser_HappyPath_FamilyMember() {
        RegisterRequestDTO request = buildRequest(
                "Carlos Familiar",
                "carlos@gmail.com",
                "FAMILY_MEMBER"
        );

        User saved = User.builder()
                .id(3L)
                .name("Carlos Familiar")
                .email("carlos@gmail.com")
                .password("password123")
                .role("PLAYER")
                .academicProgram("IngenierÃ­a")
                .build();

        when(userRepository.save(any(User.class))).thenReturn(saved);
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(any(String.class))).thenReturn("hashed-password");

        RegisterResponseDTO response = userService.registerUser(request);

        assertNotNull(response);
        assertEquals("Usuario registrado exitosamente", response.getMessage());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void registerUser_Error_InvalidEmailForStudentRole() {
        RegisterRequestDTO request = buildRequest(
                "Jane Doe", "jane@gmail.com", "STUDENT");

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> userService.registerUser(request)
        );

        assertEquals("Dominio de correo invalido para el rol: STUDENT", ex.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void registerUser_Error_InvalidEmailForProfessorRole() {
        RegisterRequestDTO request = buildRequest(
                "Prof Torres", "torres@gmail.com", "PROFESSOR");

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> userService.registerUser(request)
        );

        assertEquals("Dominio de correo invalido para el rol: PROFESSOR", ex.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void registerUser_Error_UnsupportedRole() {
        RegisterRequestDTO request = buildRequest(
                "Bob", "bob@gmail.com", "INVALID_ROLE");

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> userService.registerUser(request)
        );

        assertEquals("Rol no soportado: INVALID_ROLE", ex.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void registerUser_Error_DuplicateEmail() {
        RegisterRequestDTO request = buildRequest(
                "Jane Duplicate", "jane@gmail.com", "PLAYER");

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(User.builder().id(9L).build()));

        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> userService.registerUser(request)
        );

        assertEquals("El correo ya estÃ¡ registrado: jane@gmail.com", ex.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void registerUser_Error_NullRole() {
        RegisterRequestDTO request = buildRequest("Bob", "bob@gmail.com", null);
        request.setRole(null);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> userService.registerUser(request)
        );

        assertEquals("Rol no soportado: null", ex.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }
}
