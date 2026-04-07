package edu.dosw.proyect.core.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EquipoJugador {

    private Long id;
    private Equipo equipo;
    private Jugador jugador;
    private LocalDateTime fechaUnion;
    private boolean activo = true;

    public EquipoJugador(Equipo equipo, Jugador jugador) {
        this.equipo = equipo;
        this.jugador = jugador;
        this.fechaUnion = LocalDateTime.now();
        this.activo = true;
    }
}