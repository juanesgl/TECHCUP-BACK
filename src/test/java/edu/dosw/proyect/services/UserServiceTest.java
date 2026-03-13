package edu.dosw.proyect.services;

import edu.dosw.proyect.dtos.RegisterRequestDTO;
import edu.dosw.proyect.dtos.RegisterResponseDTO;
import edu.dosw.proyect.models.User;
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
        // Arrange
        RegisterRequestDTO request = new RegisterRequestDTO();
        request.setName("John Doe");
        request.setEmail("john@mail.escuelaing.edu.co"); // Valid institutional email
        request.setPassword("password123");
        request.setRole("STUDENT");
        request.setPreferredPosition("Forward");
        request.setSkillLevel(8);

        // Act
        RegisterResponseDTO response = userService.registerUser(request);

        // Assert
        assertNotNull(response);
        assertEquals("User created successfully.", response.getMessage());
        assertNotNull(response.getUserId());

        Map<Long, User> repo = userService.getUserRepository();
        assertEquals(1, repo.size());
        User savedUser = repo.get(response.getUserId());
        assertEquals("John Doe", savedUser.getName());
        assertEquals("STUDENT", savedUser.getRole());
    }

    @Test
    void registerUser_ErrorHandling_InvalidEmailForRole() {
        // Arrange
        RegisterRequestDTO request = new RegisterRequestDTO();
        request.setName("Jane Doe");
        request.setEmail("jane@gmail.com");
        request.setPassword("password123");
        request.setRole("STUDENT");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.registerUser(request);
        });

        assertEquals("Invalid email domain for Student. Must use institutional mail.", exception.getMessage());

        Map<Long, User> repo = userService.getUserRepository();
        assertTrue(repo.isEmpty(), "Repository should be empty after failed registration");
    }

    @Test
    void registerUser_ErrorHandling_UnsupportedRole() {
        // Arrange
        RegisterRequestDTO request = new RegisterRequestDTO();
        request.setName("Bob");
        request.setEmail("bob@gmail.com");
        request.setPassword("pass");
        request.setRole("INVALID_ROLE");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.registerUser(request);
        });

        assertEquals("Unsupported role: INVALID_ROLE", exception.getMessage());
    }
}
