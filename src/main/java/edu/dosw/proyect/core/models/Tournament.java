package edu.dosw.proyect.core.models;

import edu.dosw.proyect.core.models.enums.TournamentsStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "TORNEO")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Tournament {

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
    private User organizador;

    @OneToOne(mappedBy = "torneo", cascade = CascadeType.ALL)
    private ConfiguracionTorneo configuracion;

    
    @Transient
    private String regulation;
    @Transient
    private Long organizerId;
    @Transient
    private LocalDate registrationCloseDate;
    @Transient
    private String importantDates;
    @Transient
    private String matchSchedules;
    @Transient
    private String sanctions;

    @Transient
    @Builder.Default
    private List<Cancha> canchas = new ArrayList<>();

    public Tournament(String tournId, String name, LocalDate startDate, LocalDate endDate, int maxTeams,
            double costPerTeam, TournamentsStatus status, String regulation) {
        this.tournId = tournId;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.maxTeams = maxTeams;
        this.costPerTeam = costPerTeam;
        this.status = status;
        this.regulation = regulation;
    }
}

