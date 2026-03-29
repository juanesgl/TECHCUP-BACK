package edu.dosw.proyect.core.models;

import edu.dosw.proyect.core.models.enums.MatchStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "PARTIDO")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Partido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "torneo_id")
    private Tournament torneo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipo_local_id")
    private Equipo equipoLocal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipo_visitante_id")
    private Equipo equipoVisitante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cancha_id")
    private Cancha cancha;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "arbitro_id")
    private User arbitro; 

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

    
    @Transient
    private String nombreEquipoLocal;

    @Transient
    private String nombreEquipoVisitante;

    @Transient
    private LocalDate fecha;

    @Transient
    private LocalTime hora;

    @Transient
    private String canchaLegacy;

    @Transient
    private String arbitroLegacy;

}
