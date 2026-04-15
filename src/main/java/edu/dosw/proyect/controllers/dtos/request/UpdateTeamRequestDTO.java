package edu.dosw.proyect.controllers.dtos.request;

import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Request para actualizar datos del equipo.
 * Solo el capitán puede actualizar su propio equipo.
 * Todos los campos son opcionales — solo se actualizan los que vienen.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTeamRequestDTO {

    @Size(max = 60, message = "El nombre no puede tener más de 60 caracteres")
    private String nombreEquipo;

    private String escudo;

    private String coloresUniforme;
}