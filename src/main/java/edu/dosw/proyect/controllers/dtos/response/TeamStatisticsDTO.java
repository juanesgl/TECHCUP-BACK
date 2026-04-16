package edu.dosw.proyect.controllers.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamStatisticsDTO {
    private Long   equipoId;
    private String nombreEquipo;
    private int    partidosJugados;
    private int    victorias;
    private int    empates;
    private int    derrotas;
    private int    golesFavor;
    private int    golesContra;
    private int    diferenciaGol;
    private int    puntos;
}