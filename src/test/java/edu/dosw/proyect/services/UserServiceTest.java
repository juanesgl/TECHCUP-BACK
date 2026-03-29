package edu.dosw.proyect.services;

import edu.dosw.proyect.controllers.dtos.RegisterRequestDTO;
import edu.dosw.proyect.controllers.dtos.RegisterResponseDTO;
import edu.dosw.proyect.core.models.Student;
import edu.dosw.proyect.core.models.User;
import edu.dosw.proyect.core.repositories.UserRepository;
import edu.dosw.proyect.core.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

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

        Student studentGuardado = new Student(
                "John Doe",
                "john@mail.escuelaing.edu.co",
                "password123",
                null
        );
        studentGuardado.setId(1L);
        when(userRepository.save(any(User.class))).thenReturn(studentGuardado);

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

        User graduateGuardado = new Student("Jane Graduate", "jane@gmail.com", "password123", null);
        graduateGuardado.setId(2L);
        when(userRepository.save(any(User.class))).thenReturn(graduateGuardado);

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

        User saved = new Student("Carlos Familiar", "carlos@gmail.com", "password123", null);
        saved.setId(3L);
        when(userRepository.save(any(User.class))).thenReturn(saved);

        RegisterResponseDTO response = userService.registerUser(request);

        assertNotNull(response);
        assertEquals("Usuario registrado exitosamente", response.getMessage());
        verify(userRepository, times(1)).save(any(User.class));
    }


    @Test
    void registerUser_Error_InvalidEmailForStudentRole() {
        RegisterRequestDTO request = buildRequest(
                "Jane Doe",
                "jane@gmail.com",
                "STUDENT"
        );

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
                "Prof Torres",
                "torres@gmail.com",
                "PROFESSOR"
        );

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
                "Bob",
                "bob@gmail.com",
                "INVALID_ROLE"
        );

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> userService.registerUser(request)
        );

        assertEquals("Rol no soportado: INVALID_ROLE", ex.getMessage());
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