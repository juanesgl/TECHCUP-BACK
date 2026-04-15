package edu.dosw.proyect.core.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KnockoutBracket {

    private Long id;
    private Tournament torneo;
    private String fase;
    private int numeroLlave;
    private Team team1;
    private Team team2;
    private Partido partido;
    private Team ganador;
}

