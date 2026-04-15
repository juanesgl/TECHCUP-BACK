package edu.dosw.proyect.controllers.dtos.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DisponibilidadRequestDTO {
    @NotNull(message = "El estado de disponibilidad es obligatorio")
    private Boolean estadoDisponibilidad;
}

