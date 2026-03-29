package edu.dosw.proyect.controllers.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EstadisticasTorneoDTO {
    private String torneoId;
    private int totalPartidosJugados;
    private int totalGolesAnotados;
    private List<EstadisticasEquipoDTO> tablaPosiciones;
    private List<EstadisticasJugadorDTO> tablaGoleadores;
    private List<EstadisticasJugadorDTO> tablaTarjetas;
}
