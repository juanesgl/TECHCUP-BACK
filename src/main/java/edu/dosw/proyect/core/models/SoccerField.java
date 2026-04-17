package edu.dosw.proyect.core.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SoccerField {

    private Long id;
    private Tournament torneo;
    private String nombre;
    private String direccion;
    private String descripcion;

    public void setUbicacion(String ubicacion) {
        this.direccion = ubicacion;
    }
}