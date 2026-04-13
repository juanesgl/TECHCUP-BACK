package edu.dosw.proyect.controllers;

import edu.dosw.proyect.controllers.dtos.LoginRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.LoginResponseDTO;
import edu.dosw.proyect.core.services.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @Test
    void loginUser_HappyPath_RetornaOk() {
        LoginRequestDTO request = new LoginRequestDTO(
                "user@mail.escuelaing.edu.co", "password1");
        LoginResponseDTO response = new LoginResponseDTO(
                "Bienvenido Juan", true, "token123");

        when(authService.loginUser(request)).thenReturn(response);

        ResponseEntity<LoginResponseDTO> result = authController.loginUser(request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertTrue(result.getBody().isSuccess());
        assertEquals("token123", result.getBody().getToken());
        verify(authService, times(1)).loginUser(request);
    }

    @Test
    void loginUser_CredencialesInvalidas_RetornaUnauthorized() {
        LoginRequestDTO request = new LoginRequestDTO(
                "user@mail.escuelaing.edu.co", "wrongpass1");
        LoginResponseDTO response = new LoginResponseDTO(
                "Correo o contraseña incorrectos", false, null);

        when(authService.loginUser(request)).thenReturn(response);

        ResponseEntity<LoginResponseDTO> result = authController.loginUser(request);

        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        LoginResponseDTO body = result.getBody();
        assertNotNull(body);
        assertEquals("Correo o contraseña incorrectos", body.getMessage());
        assertFalse(body.isSuccess());
        assertNull(body.getToken());
    }
}