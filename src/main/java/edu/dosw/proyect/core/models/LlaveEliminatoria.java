package edu.dosw.proyect.core.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LlaveEliminatoria {

    private Long id;
    private Tournament torneo;
    private String fase;
    private int numeroLlave;
    private Equipo equipo1;
    private Equipo equipo2;
    private Partido partido;
    private Equipo ganador;
}

