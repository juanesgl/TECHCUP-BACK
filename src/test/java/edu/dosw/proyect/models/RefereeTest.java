package edu.dosw.proyect.models;

import edu.dosw.proyect.core.models.Referee;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RefereeTest {

    @Test
    void referee_Constructor_ConstruyeCorrectamente() {
        Referee r = new Referee(
                "Arbitro Test", "arbitro@mail.com", "pass");

        assertEquals("Arbitro Test", r.getName());
        assertEquals("arbitro@mail.com", r.getEmail());
        assertEquals("REFEREE", r.getRole());
    }

    @Test
    void referee_NoArgsConstructor_CreaVacio() {
        Referee r = new Referee();
        assertNull(r.getName());
        assertNull(r.getEmail());
    }
}