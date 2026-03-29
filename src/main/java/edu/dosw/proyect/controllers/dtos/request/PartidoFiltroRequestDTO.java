package edu.dosw.proyect.controllers.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PartidoFiltroRequestDTO {

    private LocalDate fecha;
    private String cancha;
    private String nombreEquipo;
    private String tournamentId;
}

