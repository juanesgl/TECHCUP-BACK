package edu.dosw.proyect.core.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Equipo {

    private Long id;
    private String nombre;
    private String escudoUrl;
    private String colorUniformeLocal;
    private String colorUniformeVisita;
    private Jugador capitan;
    private Tournament torneo;
    private String estadoInscripcion;

    @Builder.Default
    private List<EquipoJugador> equipoJugadores = new ArrayList<>();

    private String escudo;
    private String coloresUniforme;
    private User capitanLegacy;

    @Builder.Default
    private List<User> jugadores = new ArrayList<>();
}