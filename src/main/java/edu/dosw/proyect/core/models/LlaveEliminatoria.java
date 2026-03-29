package edu.dosw.proyect.core.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "LLAVE_ELIMINATORIA")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LlaveEliminatoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "torneo_id", nullable = false)
    private Tournament torneo;

    @Column(name = "fase")
    private String fase;

    @Column(name = "numero_llave")
    private int numeroLlave;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipo1_id")
    private Equipo equipo1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipo2_id")
    private Equipo equipo2;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partido_id")
    private Partido partido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ganador_id")
    private Equipo ganador;
}

