package edu.dosw.proyect.controllers;

import edu.dosw.proyect.controllers.dtos.RegisterRequestDTO;
import edu.dosw.proyect.controllers.dtos.RegisterResponseDTO;
import edu.dosw.proyect.core.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Usuarios", description = "Gestión de registro de usuarios base de la plataforma")
public class UserController {
    
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Registrar un usuario", description = "Permite registrar de manera abstracta administradores, organizadores, referís y jugadores según el Type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario registrado existosamente e incrustado en memoria"),
            @ApiResponse(responseCode = "400", description = "Error de validación o tipo de usuario inválido"),
            @ApiResponse(responseCode = "500", description = "Fallo en motor de persistencia interno")
    })
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequestDTO request) {
        try {
            RegisterResponseDTO response = userService.registerUser(request);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Error interno en el servidor", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
