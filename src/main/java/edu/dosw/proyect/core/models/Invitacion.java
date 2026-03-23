package edu.dosw.proyect.core.models;

import edu.dosw.proyect.core.models.enums.EstadoInvitacion;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Invitacion {
    private Long id;
    private User jugadorInvitado;
    private Equipo equipoInvita;
    private User capitan;
    private EstadoInvitacion estado;
}
