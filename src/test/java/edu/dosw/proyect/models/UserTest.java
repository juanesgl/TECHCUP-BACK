package edu.dosw.proyect.models;

import edu.dosw.proyect.core.models.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void user_Builder_ConstruyeCorrectamente() {
        User user = User.builder()
                .id(1L)
                .name("Juan")
                .lastName("Perez")
                .email("juan@mail.escuelaing.edu.co")
                .password("pass123")
                .role("PLAYER")
                .registrationDate(LocalDateTime.now())
                .active(true)
                .build();

        assertEquals(1L, user.getId());
        assertEquals("Juan", user.getName());
        assertEquals("Perez", user.getLastName());
        assertEquals("juan@mail.escuelaing.edu.co", user.getEmail());
        assertEquals("PLAYER", user.getRole());
        assertTrue(user.isActive());
    }

    @Test
    void user_OnCreate_AsignaFechaRegistro() {
        User user = User.builder()
                .name("Test")
                .email("test@mail.com")
                .password("pass")
                .role("PLAYER")
                .registrationDate(LocalDateTime.now())
                .active(true)
                .build();

        assertNotNull(user.getRegistrationDate());
        assertTrue(user.isActive());
    }

    @Test
    void user_NoArgsConstructor_CreaVacio() {
        User user = new User();
        assertNull(user.getId());
        assertNull(user.getName());
        assertTrue(user.isActive());
    }

    @Test
    void user_Setters_FuncionanCorrectamente() {
        User user = new User();
        user.setName("Maria");
        user.setEmail("maria@mail.com");
        user.setRole("ADMIN");
        user.setActive(true);

        assertEquals("Maria", user.getName());
        assertEquals("maria@mail.com", user.getEmail());
        assertEquals("ADMIN", user.getRole());
        assertTrue(user.isActive());
    }

    @Test
    void user_ConFechaRegistro_NoEsNull() {
        User user = User.builder()
                .name("Test")
                .email("test@mail.com")
                .password("pass")
                .role("PLAYER")
                .registrationDate(LocalDateTime.now())
                .build();

        assertNotNull(user.getRegistrationDate());
    }

    @Test
    void user_ActivePorDefecto_EsTrue() {
        User user = User.builder()
                .name("Test")
                .email("test@mail.com")
                .password("pass")
                .role("PLAYER")
                .registrationDate(LocalDateTime.now())
                .active(true)
                .build();

        assertTrue(user.isActive());
    }

    @Test
    void user_Builder_TodosLosCampos() {
        LocalDateTime now = LocalDateTime.now();
        User user = User.builder()
                .id(1L)
                .name("Juan")
                .lastName("Perez")
                .email("juan@mail.com")
                .password("pass123")
                .role("PLAYER")
                .registrationDate(now)
                .active(true)
                .academicProgram("sistemas")
                .build();

        assertEquals(1L, user.getId());
        assertEquals("Juan", user.getName());
        assertEquals("Perez", user.getLastName());
        assertEquals("juan@mail.com", user.getEmail());
        assertEquals("PLAYER", user.getRole());
        assertEquals("sistemas", user.getAcademicProgram());
        assertTrue(user.isActive());
        assertNotNull(user.getRegistrationDate());
    }

    @Test
    void user_NoArgsConstructor_ActiveEsTrue() {
        User user = new User();
        assertNull(user.getId());
        assertTrue(user.isActive());
    }

    @Test
    void user_Setters_TodosLosCampos() {
        User user = new User();
        user.setName("Maria");
        user.setLastName("Lopez");
        user.setEmail("maria@mail.com");
        user.setPassword("pass");
        user.setRole("ADMIN");
        user.setAcademicProgram("ia");
        user.setActive(true);
        user.setRegistrationDate(LocalDateTime.now());

        assertEquals("Maria", user.getName());
        assertEquals("Lopez", user.getLastName());
        assertEquals("ADMIN", user.getRole());
        assertEquals("ia", user.getAcademicProgram());
        assertTrue(user.isActive());
    }
}