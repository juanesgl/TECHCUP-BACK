package edu.dosw.proyect.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ALINEACION_JUGADOR")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlineacionJugadorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alineacion_id", nullable = false)
    private AlineacionEntity alineacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jugador_id", nullable = false)
    private JugadorEntity jugador;

    @Column(name = "rol")
    private String rol;

    @Column(name = "posicion_en_cancha")
    private String posicionEnCancha;

    @Column(name = "numero_camiseta")
    private int numeroCamiseta;
}