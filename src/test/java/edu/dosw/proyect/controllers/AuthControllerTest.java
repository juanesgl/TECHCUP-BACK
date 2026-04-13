package edu.dosw.proyect.controllers;

import edu.dosw.proyect.controllers.dtos.request.LoginRequestDTO;
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
                "user@mail.com", "pass123");
        LoginResponseDTO response = new LoginResponseDTO(
                "Login exitoso", true, "token123");

        when(authService.loginUser(request)).thenReturn(response);

        ResponseEntity<?> result = authController.loginUser(request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(authService, times(1)).loginUser(request);
    }

    @Test
    void loginUser_CredencialesInvalidas_RetornaUnauthorized() {
        LoginRequestDTO request = new LoginRequestDTO(
                "user@mail.com", "wrongpass");
        LoginResponseDTO response = new LoginResponseDTO(
                "Credenciales invalidas", false, null);

        when(authService.loginUser(request)).thenReturn(response);

        ResponseEntity<?> result = authController.loginUser(request);

        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        assertEquals("Credenciales invalidas", result.getBody());
    }

    @Test
    void loginUser_IllegalArgument_RetornaBadRequest() {
        LoginRequestDTO request = new LoginRequestDTO(null, null);

        when(authService.loginUser(request))
                .thenThrow(new IllegalArgumentException("Email requerido"));

        ResponseEntity<?> result = authController.loginUser(request);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("Email requerido", result.getBody());
    }

    @Test
    void loginUser_ExcepcionGenerica_RetornaInternalServerError() {
        LoginRequestDTO request = new LoginRequestDTO(
                "user@mail.com", "pass123");

        when(authService.loginUser(request))
                .thenThrow(new RuntimeException("Error inesperado"));

        ResponseEntity<?> result = authController.loginUser(request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        assertEquals("Error interno en el servidor", result.getBody());
    }
}