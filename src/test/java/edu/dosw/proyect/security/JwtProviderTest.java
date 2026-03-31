package edu.dosw.proyect.security;

import edu.dosw.proyect.core.security.JwtProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JwtProviderTest {

    private JwtProvider jwtProvider;

    @BeforeEach
    void setUp() {
        jwtProvider = new JwtProvider();
        ReflectionTestUtils.setField(jwtProvider, "jwtSecret",
                "techcup-super-secret-key-change-in-production-environment-2024-must-be-long-enough-for-hs512");
        ReflectionTestUtils.setField(jwtProvider, "jwtExpiration", 86400000L);
    }

    @Test
    void generateToken_HappyPath_RetornaToken() {
        String token = jwtProvider.generateToken("juan@mail.com", "PLAYER", 1L);
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void generateToken_ConExpiracion_RetornaToken() {
        String token = jwtProvider.generateToken(
                "juan@mail.com", "PLAYER", 1L, 3600000L);
        assertNotNull(token);
    }

    @Test
    void validateToken_TokenValido_RetornaTrue() {
        String token = jwtProvider.generateToken("juan@mail.com", "PLAYER", 1L);
        assertTrue(jwtProvider.validateToken(token));
    }

    @Test
    void validateToken_TokenInvalido_RetornaFalse() {
        assertFalse(jwtProvider.validateToken("token.invalido.aqui"));
    }

    @Test
    void validateToken_TokenVacio_RetornaFalse() {
        assertFalse(jwtProvider.validateToken(""));
    }

    @Test
    void getEmailFromToken_RetornaEmail() {
        String token = jwtProvider.generateToken("juan@mail.com", "PLAYER", 1L);
        assertEquals("juan@mail.com", jwtProvider.getEmailFromToken(token));
    }

    @Test
    void getRoleFromToken_RetornaRol() {
        String token = jwtProvider.generateToken("juan@mail.com", "ORGANIZER", 1L);
        assertEquals("ORGANIZER", jwtProvider.getRoleFromToken(token));
    }

    @Test
    void getUserIdFromToken_RetornaId() {
        String token = jwtProvider.generateToken("juan@mail.com", "PLAYER", 5L);
        assertEquals(5L, jwtProvider.getUserIdFromToken(token));
    }

    @Test
    void getExpirationDateFromToken_RetornaFecha() {
        String token = jwtProvider.generateToken("juan@mail.com", "PLAYER", 1L);
        assertNotNull(jwtProvider.getExpirationDateFromToken(token));
    }

    @Test
    void isTokenExpired_TokenValido_RetornaFalse() {
        String token = jwtProvider.generateToken("juan@mail.com", "PLAYER", 1L);
        assertFalse(jwtProvider.isTokenExpired(token));
    }

    @Test
    void isTokenExpired_TokenExpirado_RetornaTrue() {
        String token = jwtProvider.generateToken(
                "juan@mail.com", "PLAYER", 1L, -1000L);
        assertTrue(jwtProvider.isTokenExpired(token));
    }

    @Test
    void getTokenInfo_TokenValido_RetornaInfo() {
        String token = jwtProvider.generateToken("juan@mail.com", "PLAYER", 1L);
        String info = jwtProvider.getTokenInfo(token);
        assertTrue(info.contains("juan@mail.com"));
        assertTrue(info.contains("PLAYER"));
    }

    @Test
    void getTokenInfo_TokenInvalido_RetornaMensajeError() {
        String info = jwtProvider.getTokenInfo("token.invalido");
        assertTrue(info.contains("invalido") || info.contains("Token"));
    }
}