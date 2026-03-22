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
    private User jugadorInvitado; // Se usa User que tiene SportProfile, evitando Jugador duplicado
    private Equipo equipoInvita;
    private User capitan; // El usuario que envía la invitación
    private EstadoInvitacion estado;
}
