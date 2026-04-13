package edu.dosw.proyect.persistence.entity;

import edu.dosw.proyect.core.models.enums.MatchStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "PARTIDO")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartidoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "torneo_id")
    private TournamentEntity torneo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipo_local_id")
    private EquipoEntity equipoLocal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipo_visitante_id")
    private EquipoEntity equipoVisitante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cancha_id")
    private CanchaEntity cancha;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "arbitro_id")
    private UserEntity arbitro;

    @Column(name = "fecha_hora")
    private LocalDateTime fechaHora;

    @Column(name = "goles_local")
    private int golesLocal;

    @Column(name = "goles_visitante")
    private int golesVisitante;

    @Column(name = "fase")
    private String fase;

    @Column(name = "estado")
    @Enumerated(EnumType.STRING)
    private MatchStatus estado;
}