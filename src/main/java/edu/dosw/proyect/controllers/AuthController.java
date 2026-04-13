package edu.dosw.proyect.controllers;

import edu.dosw.proyect.controllers.dtos.LoginRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.LoginResponseDTO;
import edu.dosw.proyect.core.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Autenticacion", description = "Endpoints de control de acceso y login")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Iniciar Sesion",
            description = "Correo institucional y contraseña (mín. 8 caracteres). "
                    + "Éxito: mensaje de bienvenida y JWT; fallo: mensaje de error sin token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login exitoso (mensaje + token)."),
            @ApiResponse(responseCode = "401", description = "Correo o contraseña incorrectos."),
            @ApiResponse(responseCode = "400", description = "Validación de entrada fallida.")
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> loginUser(@Valid @RequestBody LoginRequestDTO request) {
        LoginResponseDTO response = authService.loginUser(request);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
}

