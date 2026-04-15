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
                "user@mail.escuelaing.edu.co", "password1");
        LoginResponseDTO response = new LoginResponseDTO(
                "Bienvenido Juan", true, "token123", 1L, "Juan", "PLAYER");

        when(authService.loginUser(request)).thenReturn(response);

        ResponseEntity<?> result = authController.loginUser(request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        LoginResponseDTO body = (LoginResponseDTO) result.getBody();
        assertTrue(body.isSuccess());
        assertEquals("token123", body.getToken());
        verify(authService, times(1)).loginUser(request);
    }

    @Test
    void loginUser_CredencialesInvalidas_RetornaUnauthorized() {
        LoginRequestDTO request = new LoginRequestDTO(
                "user@mail.escuelaing.edu.co", "wrongpass1");
        LoginResponseDTO response = new LoginResponseDTO(
                "Correo o contraseña incorrectos", false, null, null, null, null);

        when(authService.loginUser(request)).thenReturn(response);

        ResponseEntity<?> result = authController.loginUser(request);

        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        assertEquals("Correo o contraseña incorrectos", result.getBody());
    }
}