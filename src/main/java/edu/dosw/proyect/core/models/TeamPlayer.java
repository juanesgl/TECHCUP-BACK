package edu.dosw.proyect.core.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamPlayer {

    private Long id;
    private Team team;
    private Player jugador;
    private LocalDateTime fechaUnion;
    private boolean activo = true;

    public TeamPlayer(Team team, Player jugador) {
        this.team = team;
        this.jugador = jugador;
        this.fechaUnion = LocalDateTime.now();
        this.activo = true;
    }
}