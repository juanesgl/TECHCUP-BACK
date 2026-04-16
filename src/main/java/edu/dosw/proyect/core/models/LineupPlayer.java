package edu.dosw.proyect.core.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LineupPlayer {

    private Long id;
    private Lineup lineup;
    private Player jugador;
    private String rol;
    private String posicionEnCancha;
    private int numeroCamiseta;
}