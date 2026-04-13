package edu.dosw.proyect.controllers;

import edu.dosw.proyect.controllers.dtos.request.RegisterRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.RegisterResponseDTO;
import edu.dosw.proyect.core.services.UserService;
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
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private RegisterRequestDTO buildRequest(String role) {
        RegisterRequestDTO req = new RegisterRequestDTO();
        req.setName("Juan Test");
        req.setEmail("juan@mail.escuelaing.edu.co");
        req.setPassword("pass123");
        req.setRole(role);
        req.setPreferredPosition("Delantero");
        req.setSkillLevel(8);
        return req;
    }


    @Test
    void registerUser_HappyPath_RetornaCreated() {
        RegisterRequestDTO request = buildRequest("STUDENT");
        RegisterResponseDTO response = new RegisterResponseDTO(
                "Usuario registrado exitosamente", 1L);

        when(userService.registerUser(request)).thenReturn(response);

        ResponseEntity<?> result = userController.registerUser(request);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertNotNull(result.getBody());
        verify(userService, times(1)).registerUser(request);
    }


    @Test
    void registerUser_RolInvalido_RetornaBadRequest() {
        RegisterRequestDTO request = buildRequest("ROL_INVALIDO");

        when(userService.registerUser(request))
                .thenThrow(new IllegalArgumentException("Rol no soportado"));

        ResponseEntity<?> result = userController.registerUser(request);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("Rol no soportado", result.getBody());
    }

    @Test
    void registerUser_EmailDuplicado_RetornaConflict() {
        RegisterRequestDTO request = buildRequest("STUDENT");

        when(userService.registerUser(request))
                .thenThrow(new IllegalStateException("Correo ya registrado"));

        ResponseEntity<?> result = userController.registerUser(request);

        assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
        assertEquals("Correo ya registrado", result.getBody());
    }

    @Test
    void registerUser_ExcepcionGenerica_RetornaInternalServerError() {
        RegisterRequestDTO request = buildRequest("STUDENT");

        when(userService.registerUser(request))
                .thenThrow(new RuntimeException("Error inesperado"));

        ResponseEntity<?> result = userController.registerUser(request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        assertEquals("Error interno en el servidor", result.getBody());
    }
}