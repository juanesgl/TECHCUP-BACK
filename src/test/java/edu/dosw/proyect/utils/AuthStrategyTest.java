package edu.dosw.proyect.utils;

import edu.dosw.proyect.core.utils.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthStrategyTest {

    //InstitutionalMailStrategy

    @Test
    void institutionalMail_EmailValido_RetornaTrue() {
        InstitutionalMailStrategy strategy = new InstitutionalMailStrategy();
        assertTrue(strategy.validate("juan@mail.escuelaing.edu.co"));
    }

    @Test
    void institutionalMail_EmailGmail_RetornaTrue() {
        InstitutionalMailStrategy strategy = new InstitutionalMailStrategy();
        assertTrue(strategy.validate("juan@gmail.com"));
    }

    @Test
    void institutionalMail_EmailNulo_RetornaFalse() {
        InstitutionalMailStrategy strategy = new InstitutionalMailStrategy();
        assertFalse(strategy.validate(null));
    }

    // GmailStrategy

    @Test
    void gmailStrategy_EmailGmail_RetornaTrue() {
        GmailStrategy strategy = new GmailStrategy();
        assertTrue(strategy.validate("juan@gmail.com"));
    }

    @Test
    void gmailStrategy_EmailNoGmail_RetornaFalse() {
        GmailStrategy strategy = new GmailStrategy();
        assertFalse(strategy.validate("juan@hotmail.com"));
    }

    @Test
    void gmailStrategy_EmailNulo_RetornaFalse() {
        GmailStrategy strategy = new GmailStrategy();
        assertFalse(strategy.validate(null));
    }

    // AnyMailStrategy

    @Test
    void anyMailStrategy_CualquierEmailValido_RetornaTrue() {
        AnyMailStrategy strategy = new AnyMailStrategy();
        assertTrue(strategy.validate("juan@gmail.com"));
        assertTrue(strategy.validate("juan@hotmail.com"));
        assertTrue(strategy.validate("juan@mail.escuelaing.edu.co"));
    }

    @Test
    void anyMailStrategy_EmailSinArroba_RetornaFalse() {
        AnyMailStrategy strategy = new AnyMailStrategy();
        assertFalse(strategy.validate("juangmail.com"));
    }

    @Test
    void anyMailStrategy_EmailNulo_RetornaFalse() {
        AnyMailStrategy strategy = new AnyMailStrategy();
        assertFalse(strategy.validate(null));
    }

    @Test
    void anyMailStrategy_EmailSinPunto_RetornaFalse() {
        AnyMailStrategy strategy = new AnyMailStrategy();
        assertFalse(strategy.validate("juan@gmailcom"));
    }
}