package edu.dosw.proyect.models;

import edu.dosw.proyect.core.models.Student;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StudentTest {

    @Test
    void student_Constructor_ConstruyeCorrectamente() {
        Student s = new Student();
        s.setName("Juan");
        s.setEmail("juan@mail.com");
        s.setPassword("pass123");
        s.setRole("PLAYER");

        assertEquals("Juan", s.getName());
        assertEquals("juan@mail.com", s.getEmail());
        assertEquals("PLAYER", s.getRole());
    }

    @Test
    void student_NoArgsConstructor_CreaVacio() {
        Student s = new Student();
        assertNull(s.getName());
        assertNull(s.getEmail());
    }
}