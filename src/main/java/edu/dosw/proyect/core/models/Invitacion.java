package edu.dosw.proyect.core.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Invitacion {

    private Long id;
    private Equipo equipo;
    private Jugador jugador;
    private String estado;
    private LocalDateTime fechaEnvio;
    private LocalDateTime fechaRespuesta;

    public Jugador getJugadorInvitado() {
        return jugador;
    }

    public Equipo getEquipoInvita() {
        return equipo;
    }
}

