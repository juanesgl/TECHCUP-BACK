package edu.dosw.proyect.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DisponibilidadResponseDTO {
    private String mensaje;
    private Boolean estadoFinal;
}
