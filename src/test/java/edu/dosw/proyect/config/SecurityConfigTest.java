package edu.dosw.proyect.config;

import edu.dosw.proyect.core.security.JwtAuthFilter;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

class SecurityConfigTest {

    private final SecurityConfig securityConfig = new SecurityConfig(
            Mockito.mock(ObjectProvider.class),
            Mockito.mock(ObjectProvider.class),
            Mockito.mock(JwtAuthFilter.class)
    );

    @Test
    void passwordEncoder_RetornaBCrypt() {
        PasswordEncoder encoder = securityConfig.passwordEncoder();
        assertNotNull(encoder);
        assertTrue(encoder.matches("pass123", encoder.encode("pass123")));
    }
}