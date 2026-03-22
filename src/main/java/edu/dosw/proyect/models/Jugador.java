package edu.dosw.proyect.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Jugador {
    private Long id;
    private String nombre;
    private boolean perfilCompleto; 
    private boolean tieneEquipo;    
    private boolean disponible;     
}
