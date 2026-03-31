package edu.dosw.proyect.core.models;

import edu.dosw.proyect.core.models.enums.TacticalFormation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Alineacion {

    private Long id;
    private Partido partido;
    private Equipo equipo;
    private TacticalFormation formacion;
    private LocalDateTime fechaRegistro;

    @Builder.Default
    private List<AlineacionJugador> jugadores = new ArrayList<>();

    public String getNombreEquipo() {
        return equipo != null ? equipo.getNombre() : null;
    }

    public List<String> getTitulares() {
        return jugadores.stream()
                .filter(j -> "TITULAR".equalsIgnoreCase(j.getRol()))
                .map(j -> j.getJugador().getNombre())
                .toList();
    }

    public List<String> getReservas() {
        return jugadores.stream()
                .filter(j -> "RESERVA".equalsIgnoreCase(j.getRol()))
                .map(j -> j.getJugador().getNombre())
                .toList();
    }
}