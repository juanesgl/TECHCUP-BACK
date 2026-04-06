package edu.dosw.proyect.persistence.entitys;

import edu.dosw.proyect.persistence.entity.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserEntityTest {

    @Test
    void userEntity_Builder_ConstruyeCorrectamente() {
        UserEntity user = UserEntity.builder()
                .id(1L).name("Juan").lastName("Perez")
                .email("juan@mail.com").password("pass")
                .role("PLAYER").active(true)
                .academicProgram("sistemas")
                .build();

        assertEquals(1L, user.getId());
        assertEquals("Juan", user.getName());
        assertEquals("Perez", user.getLastName());
        assertEquals("juan@mail.com", user.getEmail());
        assertEquals("PLAYER", user.getRole());
        assertTrue(user.isActive());
        assertEquals("sistemas", user.getAcademicProgram());
    }

    @Test
    void userEntity_Setters_FuncionanCorrectamente() {
        UserEntity user = new UserEntity();
        user.setName("Maria");
        user.setEmail("maria@mail.com");
        user.setRole("CAPTAIN");
        user.setActive(true);
        user.setRegistrationDate(LocalDateTime.now());

        assertEquals("Maria", user.getName());
        assertEquals("CAPTAIN", user.getRole());
        assertTrue(user.isActive());
    }
}
