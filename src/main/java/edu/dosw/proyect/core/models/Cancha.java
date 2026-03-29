package edu.dosw.proyect.core.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cancha {
    private Long id;
    private String nombre;
    private String ubicacion;
    private Tournament torneo;
}
