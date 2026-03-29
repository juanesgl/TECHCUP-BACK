package edu.dosw.proyect.core.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "EQUIPO_JUGADOR")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EquipoJugador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipo_id", nullable = false)
    private Equipo equipo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jugador_id", nullable = false)
    private Jugador jugador;

    @Column(name = "fecha_union")
    private LocalDateTime fechaUnion;

    @Column(name = "activo")
    private boolean activo = true;

    
    public EquipoJugador(Equipo equipo, Jugador jugador) {
        this.equipo = equipo;
        this.jugador = jugador;
        this.fechaUnion = LocalDateTime.now();
        this.activo = true;
    }
}

