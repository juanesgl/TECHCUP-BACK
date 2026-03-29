package edu.dosw.proyect.services;

import edu.dosw.proyect.controllers.dtos.RegisterRequestDTO;
import edu.dosw.proyect.controllers.dtos.RegisterResponseDTO;
import edu.dosw.proyect.core.models.User;
import edu.dosw.proyect.core.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService();
    }

    @Test
    void registerUser_HappyPath_Student() {
        RegisterRequestDTO request = new RegisterRequestDTO();
        request.setName("John Doe");
        request.setEmail("john@mail.escuelaing.edu.co");
        request.setPassword("password123");
        request.setRole("STUDENT");
        request.setPreferredPosition("Forward");
        request.setSkillLevel(8);

        RegisterResponseDTO response = userService.registerUser(request);

        assertNotNull(response);
        assertEquals("Usuario registrado exitosamente", response.getMessage());
        assertNotNull(response.getUserId());

        Map<Long, User> repo = userService.getUserRepository();
        assertEquals(1, repo.size());
        User savedUser = repo.get(response.getUserId());
        assertEquals("John Doe", savedUser.getName());
        assertEquals("STUDENT", savedUser.getRole());
    }

    @Test
    void registerUser_ErrorHandling_InvalidEmailForRole() {
        RegisterRequestDTO request = new RegisterRequestDTO();
        request.setName("Jane Doe");
        request.setEmail("jane@gmail.com");
        request.setPassword("password123");
        request.setRole("STUDENT");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.registerUser(request);
        });

        assertEquals("Dominio de correo invalido para el rol: STUDENT", exception.getMessage());

        Map<Long, User> repo = userService.getUserRepository();
        assertTrue(repo.isEmpty(), "Repository should be empty after failed registration");
    }

    @Test
    void registerUser_ErrorHandling_UnsupportedRole() {
        RegisterRequestDTO request = new RegisterRequestDTO();
        request.setName("Bob");
        request.setEmail("bob@gmail.com");
        request.setPassword("pass");
        request.setRole("INVALID_ROLE");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.registerUser(request);
        });

        assertEquals("Rol no soportado: INVALID_ROLE", exception.getMessage());
    }
}
