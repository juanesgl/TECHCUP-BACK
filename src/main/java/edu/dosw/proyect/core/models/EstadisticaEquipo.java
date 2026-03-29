package edu.dosw.proyect.core.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ESTADISTICA_EQUIPO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstadisticaEquipo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipo_id", nullable = false)
    private Equipo equipo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "torneo_id", nullable = false)
    private Tournament torneo;

    @Column(name = "partidos_jugados")
    private int partidosJugados;

    @Column(name = "partidos_ganados")
    private int partidosGanados;

    @Column(name = "partidos_empatados")
    private int partidosEmpatados;

    @Column(name = "partidos_perdidos")
    private int partidosPerdidos;

    @Column(name = "goles_favor")
    private int golesFavor;

    @Column(name = "goles_contra")
    private int golesContra;

    @Column(name = "diferencia_gol")
    private int diferenciaGol;

    @Column(name = "puntos")
    private int puntos;
}
