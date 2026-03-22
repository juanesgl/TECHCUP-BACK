package edu.dosw.proyect.models;

import edu.dosw.proyect.models.enums.EstadoInvitacion;
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
