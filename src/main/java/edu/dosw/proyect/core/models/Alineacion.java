package edu.dosw.proyect.core.models;

import edu.dosw.proyect.core.models.enums.FormacionTecnica;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ALINEACION")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Alineacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partido_id", nullable = false)
    private Partido partido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipo_id", nullable = false)
    private Equipo equipo;

    @Enumerated(EnumType.STRING)
    @Column(name = "formacion")
    private FormacionTecnica formacion;

    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro;

    @OneToMany(mappedBy = "alineacion", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
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
