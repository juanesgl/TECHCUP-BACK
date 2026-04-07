package edu.dosw.proyect.core.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlineacionJugador {

    private Long id;
    private Alineacion alineacion;
    private Jugador jugador;
    private String rol;
    private String posicionEnCancha;
    private int numeroCamiseta;
}