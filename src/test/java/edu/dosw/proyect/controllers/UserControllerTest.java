package edu.dosw.proyect.controllers;

import edu.dosw.proyect.controllers.dtos.request.RegisterRequestDTO;
import edu.dosw.proyect.controllers.dtos.request.UpdateUserRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.RegisterResponseDTO;
import edu.dosw.proyect.controllers.dtos.response.UserResponseDTO;
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
import java.util.List;

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

    @Test
    void getAllUsers_HappyPath_RetornaOk() {
        when(userService.getAllUsers(1L)).thenReturn(List.of(UserResponseDTO.builder().id(1L).build()));

        ResponseEntity<List<UserResponseDTO>> result = userController.getAllUsers(1L);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(1, result.getBody().size());
    }

    @Test
    void getUserById_HappyPath_RetornaOk() {
        when(userService.getUserById(2L, 1L)).thenReturn(UserResponseDTO.builder().id(2L).build());

        ResponseEntity<UserResponseDTO> result = userController.getUserById(2L, 1L);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(2L, result.getBody().getId());
    }

    @Test
    void updateUser_HappyPath_RetornaOk() {
        UpdateUserRequestDTO request = new UpdateUserRequestDTO();
        request.setName("Nuevo");
        when(userService.updateUser(2L, 1L, request)).thenReturn(UserResponseDTO.builder().id(2L).name("Nuevo").build());

        ResponseEntity<UserResponseDTO> result = userController.updateUser(2L, 1L, request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Nuevo", result.getBody().getName());
    }

    @Test
    void deleteUser_HappyPath_RetornaNoContent() {
        doNothing().when(userService).deleteUser(2L, 1L);

        ResponseEntity<Void> result = userController.deleteUser(2L, 1L);

        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        verify(userService).deleteUser(2L, 1L);
    }
}