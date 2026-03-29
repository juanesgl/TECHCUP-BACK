package edu.dosw.proyect.core.models;

import edu.dosw.proyect.core.models.enums.EstadoPartido;
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
public class Partido {

    private Long id;
    private String equipoLocalId;
    private String equipoVisitanteId;
    private String nombreEquipoLocal;
    private String nombreEquipoVisitante;
    private LocalDate fecha;
    private LocalTime hora;
    private String cancha;
    private String arbitro;
    private EstadoPartido estado;
    private String tournamentId;
}
