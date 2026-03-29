package edu.dosw.proyect.controllers.dtos.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CanchaDTO {
    @NotBlank(message = "El nombre de la cancha es obligatorio")
    private String nombre;

    @NotBlank(message = "La ubicaciÃ³n de la cancha es obligatoria")
    private String ubicacion;
}

