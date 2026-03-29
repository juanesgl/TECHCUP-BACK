package edu.dosw.proyect.core.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ALINEACION_JUGADOR")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlineacionJugador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alineacion_id", nullable = false)
    private Alineacion alineacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jugador_id", nullable = false)
    private Jugador jugador;

    @Column(name = "rol")
    private String rol;

    @Column(name = "posicion_en_cancha")
    private String posicionEnCancha;

    @Column(name = "numero_camiseta")
    private int numeroCamiseta;
}
