package edu.dosw.proyect.controllers.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlayerStatisticsDTO {
    private Long   jugadorId;
    private String nombreJugador;
    private String nombreEquipo;
    private int    goles;
    private int    tarjetasAmarillas;
    private int    tarjetasRojas;
}