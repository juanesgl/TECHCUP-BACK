package edu.dosw.proyect.controllers.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartidoResponseDTO {

    private Long id;
    private String equipoLocal;
    private String equipoVisitante;
    private LocalDate fecha;
    private LocalTime hora;
    private String cancha;
    private String arbitro;
    private String estado;
    private String tournamentId;
}

