package edu.dosw.proyect.utils;

import edu.dosw.proyect.controllers.dtos.request.RegisterRequestDTO;
import edu.dosw.proyect.core.models.User;
import edu.dosw.proyect.core.utils.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CreatorsTest {

    private RegisterRequestDTO buildRequest(String email) {
        return new RegisterRequestDTO(
                "Juan Test", email, "pass123",
                "PLAYER", "Delantero", 8);
    }

    // StudentCreator

    @Test
    void studentCreator_EmailValido_CreaUsuario() {
        StudentCreator creator = new StudentCreator();
        RegisterRequestDTO request = buildRequest(
                "juan@mail.escuelaing.edu.co");

        User user = creator.createUser(request);

        assertEquals("Juan Test", user.getName());
        assertEquals("juan@mail.escuelaing.edu.co", user.getEmail());
        assertEquals("STUDENT", user.getRole());
    }

    @Test
    void studentCreator_ValidateEmail_EmailInstitucional_RetornaTrue() {
        StudentCreator creator = new StudentCreator();
        assertTrue(creator.validateEmail("juan@mail.escuelaing.edu.co"));
    }

    @Test
    void studentCreator_ValidateEmail_EmailGmail_RetornaTrue() {
        StudentCreator creator = new StudentCreator();
        assertTrue(creator.validateEmail("juan@gmail.com"));
    }

    // AdminCreator

    @Test
    void adminCreator_EmailValido_CreaUsuario() {
        AdminCreator creator = new AdminCreator();
        RegisterRequestDTO request = buildRequest(
                "admin@mail.escuelaing.edu.co");

        User user = creator.createUser(request);

        assertEquals("Juan Test", user.getName());
        assertEquals("ADMINISTRATOR", user.getRole());
    }

    @Test
    void adminCreator_ValidateEmail_EmailInstitucional_RetornaTrue() {
        AdminCreator creator = new AdminCreator();
        assertTrue(creator.validateEmail("admin@mail.escuelaing.edu.co"));
    }

    @Test
    void adminCreator_ValidateEmail_EmailGmail_RetornaTrue() {
        AdminCreator creator = new AdminCreator();
        assertTrue(creator.validateEmail("admin@gmail.com"));
    }

    // RefereeCreator

    @Test
    void refereeCreator_EmailValido_CreaUsuario() {
        RefereeCreator creator = new RefereeCreator();
        RegisterRequestDTO request = buildRequest("arbitro@gmail.com");

        User user = creator.createUser(request);

        assertEquals("Juan Test", user.getName());
        assertEquals("REFEREE", user.getRole());
    }

    @Test
    void refereeCreator_ValidateEmail_Gmail_RetornaTrue() {
        RefereeCreator creator = new RefereeCreator();
        assertTrue(creator.validateEmail("arbitro@gmail.com"));
    }

    @Test
    void refereeCreator_ValidateEmail_NoGmail_RetornaFalse() {
        RefereeCreator creator = new RefereeCreator();
        assertFalse(creator.validateEmail("arbitro@hotmail.com"));
    }

    // CaptainCreator

    @Test
    void captainCreator_CualquierEmail_CreaUsuario() {
        CaptainCreator creator = new CaptainCreator();
        RegisterRequestDTO request = buildRequest("capitan@cualquier.com");

        User user = creator.createUser(request);

        assertEquals("Juan Test", user.getName());
        assertEquals("CAPTAIN", user.getRole());
        assertTrue(user.isActive());
    }

    @Test
    void captainCreator_ValidateEmail_CualquierEmail_RetornaTrue() {
        CaptainCreator creator = new CaptainCreator();
        assertTrue(creator.validateEmail("capitan@gmail.com"));
        assertTrue(creator.validateEmail("capitan@hotmail.com"));
        assertTrue(creator.validateEmail("capitan@mail.escuelaing.edu.co"));
    }

    // PlayerCreator

    @Test
    void playerCreator_CualquierEmail_CreaUsuario() {
        PlayerCreator creator = new PlayerCreator();
        RegisterRequestDTO request = buildRequest("player@cualquier.com");

        User user = creator.createUser(request);

        assertEquals("Juan Test", user.getName());
        assertEquals("PLAYER", user.getRole());
        assertTrue(user.isActive());
    }

    @Test
    void playerCreator_ValidateEmail_CualquierEmail_RetornaTrue() {
        PlayerCreator creator = new PlayerCreator();
        assertTrue(creator.validateEmail("player@gmail.com"));
        assertTrue(creator.validateEmail("player@hotmail.com"));
    }

    // FamilyCreator

    @Test
    void familyCreator_EmailGmail_CreaUsuario() {
        FamilyCreator creator = new FamilyCreator();
        RegisterRequestDTO request = buildRequest("familia@gmail.com");

        User user = creator.createUser(request);

        assertEquals("Juan Test", user.getName());
        assertEquals("FAMILY_MEMBER", user.getRole());
    }

    @Test
    void familyCreator_ValidateEmail_Gmail_RetornaTrue() {
        FamilyCreator creator = new FamilyCreator();
        assertTrue(creator.validateEmail("familia@gmail.com"));
    }

    @Test
    void familyCreator_ValidateEmail_NoGmail_RetornaFalse() {
        FamilyCreator creator = new FamilyCreator();
        assertFalse(creator.validateEmail("familia@hotmail.com"));
    }

    // GraduateCreator

    @Test
    void graduateCreator_EmailGmail_CreaUsuario() {
        GraduateCreator creator = new GraduateCreator();
        RegisterRequestDTO request = buildRequest("graduado@gmail.com");

        User user = creator.createUser(request);

        assertEquals("Juan Test", user.getName());
        assertEquals("GRADUATE", user.getRole());
    }

    @Test
    void graduateCreator_ValidateEmail_Gmail_RetornaTrue() {
        GraduateCreator creator = new GraduateCreator();
        assertTrue(creator.validateEmail("graduado@gmail.com"));
    }

    @Test
    void graduateCreator_ValidateEmail_NoGmail_RetornaFalse() {
        GraduateCreator creator = new GraduateCreator();
        assertFalse(creator.validateEmail("graduado@hotmail.com"));
    }

    // OrganizerCreator

    @Test
    void organizerCreator_EmailInstitucional_CreaUsuario() {
        OrganizerCreator creator = new OrganizerCreator();
        RegisterRequestDTO request = buildRequest("orga@mail.escuelaing.edu.co");

        User user = creator.createUser(request);

        assertEquals("Juan Test", user.getName());
        assertEquals("ORGANIZER", user.getRole());
    }

    @Test
    void organizerCreator_ValidateEmail_Gmail_TambienEsValido() {
        OrganizerCreator creator = new OrganizerCreator();
        assertTrue(creator.validateEmail("orga@gmail.com"));
    }

    // ProfessorCreator

    @Test
    void professorCreator_EmailInstitucional_CreaUsuario() {
        ProfessorCreator creator = new ProfessorCreator();
        RegisterRequestDTO request = buildRequest("profe@mail.escuelaing.edu.co");

        User user = creator.createUser(request);

        assertEquals("Juan Test", user.getName());
        assertEquals("PROFESSOR", user.getRole());
    }

    @Test
    void professorCreator_ValidateEmail_Gmail_TambienEsValido() {
        ProfessorCreator creator = new ProfessorCreator();
        assertTrue(creator.validateEmail("profe@gmail.com"));
    }
}