package edu.dosw.proyect.persistence.entity;

import edu.dosw.proyect.core.models.enums.TacticalFormation;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ALINEACION")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LineupEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partido_id", nullable = false)
    private MatchEntity partido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipo_id", nullable = false)
    private TeamEntity equipo;

    @Enumerated(EnumType.STRING)
    @Column(name = "formacion")
    private TacticalFormation formacion;

    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro;

    @OneToMany(mappedBy = "alineacion", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<LineupPlayerEntity> jugadores = new ArrayList<>();
}