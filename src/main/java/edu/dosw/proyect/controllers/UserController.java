package edu.dosw.proyect.controllers;

import edu.dosw.proyect.controllers.dtos.request.RegisterRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.RegisterResponseDTO;
import edu.dosw.proyect.controllers.dtos.request.UpdateUserRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.UserResponseDTO;
import edu.dosw.proyect.core.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "02 Registro de Usuarios", description = "Registro y gestión de usuarios")
public class UserController {

    private final UserService userService;


    @Operation(summary = "Registrar un usuario",
            description = "Permite registrar administradores, organizadores, árbitros y jugadores")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario registrado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Error de validación o tipo de usuario inválido"),
            @ApiResponse(responseCode = "409", description = "Correo ya registrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequestDTO request) {
        try {
            RegisterResponseDTO response = userService.registerUser(request);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>("Error interno en el servidor", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Operation(summary = "Listar todos los usuarios",
            description = "Solo el administrador puede ver todos los usuarios del sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de usuarios retornada exitosamente"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para realizar esta acción")
    })
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers(
            @RequestHeader("X-User-ID") Long requesterId) {
        return ResponseEntity.ok(userService.getAllUsers(requesterId));
    }


    @Operation(summary = "Obtener usuario por ID",
            description = "El administrador puede ver cualquier usuario. " +
                    "Un usuario solo puede ver su propio perfil.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario retornado exitosamente"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para ver este usuario"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(
            @PathVariable Long id,
            @RequestHeader("X-User-ID") Long requesterId) {
        return ResponseEntity.ok(userService.getUserById(id, requesterId));
    }


    @Operation(summary = "Actualizar usuario",
            description = "El administrador puede actualizar cualquier usuario. " +
                    "Un usuario solo puede actualizar su propio perfil.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario actualizado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para actualizar este usuario"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable Long id,
            @RequestHeader("X-User-ID") Long requesterId,
            @RequestBody UpdateUserRequestDTO request) {
        return ResponseEntity.ok(userService.updateUser(id, requesterId, request));
    }


    @Operation(summary = "Eliminar usuario",
            description = "Solo el administrador puede eliminar usuarios del sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuario eliminado exitosamente"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para eliminar usuarios"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable Long id,
            @RequestHeader("X-User-ID") Long requesterId) {
        userService.deleteUser(id, requesterId);
        return ResponseEntity.noContent().build();
    }
}