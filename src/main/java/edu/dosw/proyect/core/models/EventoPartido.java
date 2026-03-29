package edu.dosw.proyect.core.models;

import edu.dosw.proyect.core.models.enums.TipoEvento;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventoPartido {
    private Long id;
    private Partido partido;
    private User jugador;
    private Equipo equipo;
    private TipoEvento tipoEvento;
    private int minuto;
}
