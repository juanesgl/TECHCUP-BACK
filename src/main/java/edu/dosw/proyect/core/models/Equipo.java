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
    private String escudo;
    private String coloresUniforme;
    
    private User capitan;
    
    @Builder.Default
    private List<User> jugadores = new ArrayList<>();
}
