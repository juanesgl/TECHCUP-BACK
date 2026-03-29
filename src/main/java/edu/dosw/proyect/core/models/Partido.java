package edu.dosw.proyect.core.models;

import edu.dosw.proyect.core.models.enums.MatchStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Partido {

    private Long id;
    private Tournament torneo;
    private Equipo equipoLocal;
    private Equipo equipoVisitante;

    private String nombreEquipoLocal;
    private String nombreEquipoVisitante;
    private LocalDate fecha;
    private LocalTime hora;
    private LocalDateTime fechaHora;

    private String cancha;
    private String arbitro;

    private MatchStatus estado;

    private int golesLocal;
    private int golesVisitante;
}