package edu.dosw.proyect.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "EQUIPO", uniqueConstraints = @UniqueConstraint(columnNames = {"nombre", "torneo_id"}))
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EquipoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "escudo_url")
    private String escudoUrl;

    @Column(name = "color_uniforme_local")
    private String colorUniformeLocal;

    @Column(name = "color_uniforme_visita")
    private String colorUniformeVisita;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "capitan_id")
    private JugadorEntity capitan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "torneo_id")
    private TournamentEntity torneo;

    @Column(name = "estado_inscripcion")
    private String estadoInscripcion;

    @OneToMany(mappedBy = "equipo", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<EquipoJugadorEntity> equipoJugadores = new ArrayList<>();
}