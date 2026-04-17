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
    private Team teamLocal;
    private Team teamVisitante;
    private SoccerField soccerField;
    private User arbitro;
    private LocalDateTime fechaHora;
    private int golesLocal;
    private int golesVisitante;
    private String fase;
    private MatchStatus estado;

    private String nombreEquipoLocal;
    private String nombreEquipoVisitante;
    private LocalDate fecha;
    private LocalTime hora;
    private String canchaLegacy;
    private String arbitroLegacy;
}