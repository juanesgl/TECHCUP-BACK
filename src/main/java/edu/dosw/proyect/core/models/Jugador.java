package edu.dosw.proyect.core.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Jugador {

    private Long id;
    private User usuario;
    private String fotoUrl;
    private String posiciones;
    private Integer dorsal;
    private boolean disponible;
    private String semestre;
    private String genero;
    private String identificacion;
    private Integer edad;
    private String nombre;
    private boolean perfilCompleto;
    private boolean tieneEquipo;

    public Jugador(Long id, String nombre, boolean perfilCompleto,
                   boolean tieneEquipo, boolean disponible) {
        this.id = id;
        this.nombre = nombre;
        this.perfilCompleto = perfilCompleto;
        this.tieneEquipo = tieneEquipo;
        this.disponible = disponible;
    }
}