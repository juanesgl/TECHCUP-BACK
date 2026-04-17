package edu.dosw.proyect.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "LLAVE_ELIMINATORIA")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KnockoutBracketEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "torneo_id", nullable = false)
    private TournamentEntity torneo;

    @Column(name = "fase")
    private String fase;

    @Column(name = "numero_llave")
    private int numeroLlave;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipo1_id")
    private TeamEntity equipo1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipo2_id")
    private TeamEntity equipo2;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partido_id")
    private MatchEntity partido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ganador_id")
    private TeamEntity ganador;
}