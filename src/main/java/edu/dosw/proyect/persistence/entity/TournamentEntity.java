package edu.dosw.proyect.persistence.entity;

import edu.dosw.proyect.core.models.enums.TournamentsStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "TORNEO")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TournamentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo_legacy", unique = true)
    private String tournId;

    @Column(name = "nombre")
    private String name;

    @Column(name = "fecha_inicio")
    private LocalDate startDate;

    @Column(name = "fecha_fin")
    private LocalDate endDate;

    @Column(name = "cantidad_equipos")
    private int maxTeams;

    @Column(name = "costo_por_equipo")
    private double costPerTeam;

    @Column(name = "estado")
    @Enumerated(EnumType.STRING)
    private TournamentsStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organizador_id")
    private UserEntity organizador;

    /**
     * Fecha límite de pago de inscripción.
     * Según las reglas del torneo: Jueves 26 de Marzo.
     * Se configura al crear/configurar el torneo.
     */
    @Column(name = "fecha_cierre_inscripciones")
    private LocalDate registrationCloseDate;
}