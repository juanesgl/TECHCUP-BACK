package edu.dosw.proyect.core.models;

import edu.dosw.proyect.core.models.enums.MatchStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Partido {
    private Long id;
    private Tournament torneo;
    private Equipo equipoLocal;
    private Equipo equipoVisitante;
    private LocalDateTime fechaHora;
    private String cancha;
    private MatchStatus estado;

    private int golesLocal;
    private int golesVisitante;
}
