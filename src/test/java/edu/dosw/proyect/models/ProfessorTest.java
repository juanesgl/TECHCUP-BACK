package edu.dosw.proyect.models;

import edu.dosw.proyect.core.models.Professor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProfessorTest {

    @Test
    void professor_Constructor_ConstruyeCorrectamente() {
        Professor p = new Professor(
                "Prof Test", "prof@mail.com", "pass", null);

        assertEquals("Prof Test", p.getName());
        assertEquals("prof@mail.com", p.getEmail());
        assertEquals("PLAYER", p.getRole());
    }

    @Test
    void professor_NoArgsConstructor_CreaVacio() {
        Professor p = new Professor();
        assertNull(p.getName());
        assertNull(p.getEmail());
    }
}