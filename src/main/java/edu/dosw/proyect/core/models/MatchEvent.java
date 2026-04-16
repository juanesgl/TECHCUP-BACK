package edu.dosw.proyect.core.models;

import edu.dosw.proyect.core.models.enums.EventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchEvent {

    private Long id;
    private Partido partido;
    private Player jugador;
    private EventType eventType;
    private int minuto;
    private String descripcion;
    private User jugadorLegacy;
    private Team team;

    public Player getJugador() {
        return jugador;
    }

    public void setJugador(Player jugador) {
        this.jugador = jugador;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }
}

