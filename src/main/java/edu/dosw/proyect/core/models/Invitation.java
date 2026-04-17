package edu.dosw.proyect.core.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Invitation {

    private Long id;
    private Team team;
    private Player jugador;
    private String estado;
    private LocalDateTime fechaEnvio;
    private LocalDateTime fechaRespuesta;

    public Player getJugadorInvitado() {
        return jugador;
    }

    public Team getEquipoInvita() {
        return team;
    }
}

