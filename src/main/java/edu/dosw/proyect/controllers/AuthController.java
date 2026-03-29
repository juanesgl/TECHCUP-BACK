package edu.dosw.proyect.controllers;

import edu.dosw.proyect.controllers.dtos.LoginRequestDTO;
import edu.dosw.proyect.controllers.dtos.LoginResponseDTO;
import edu.dosw.proyect.core.services.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/users")
@Tag(name = "AutenticaciÃ³n", description = "Endpoints de control de acceso y login")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Iniciar SesiÃ³n", description = "Autentica con email y contraseÃ±a. Retorna la validez del intento.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login exitoso."),
            @ApiResponse(responseCode = "401", description = "Credenciales incorrectas o inexistentes."),
            @ApiResponse(responseCode = "400", description = "Solicitud malformada por parte del cliente.")
    })
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequestDTO request) {
        try {
            LoginResponseDTO response = authService.loginUser(request);
            if (response.isSuccess()) {
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(response.getMessage(), HttpStatus.UNAUTHORIZED);
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Error interno en el servidor", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

