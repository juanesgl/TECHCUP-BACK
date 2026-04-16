package edu.dosw.proyect.controllers.dtos.request;

import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTeamRequestDTO {

    @Size(max = 60, message = "El nombre no puede tener más de 60 caracteres")
    private String nombreEquipo;

    private String escudo;

    private String coloresUniforme;
}