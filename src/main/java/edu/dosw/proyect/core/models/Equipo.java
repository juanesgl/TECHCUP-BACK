package edu.dosw.proyect.core.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "EQUIPO", uniqueConstraints = @UniqueConstraint(columnNames = { "nombre", "torneo_id" }))
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Equipo {

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
    private Jugador capitan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "torneo_id")
    private Tournament torneo;

    @Column(name = "estado_inscripcion")
    private String estadoInscripcion;

    
    @OneToMany(mappedBy = "equipo", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<EquipoJugador> equipoJugadores = new ArrayList<>();

    
    @Transient
    private String escudo;

    @Transient
    private String coloresUniforme;

    @Transient
    private User capitanLegacy;

    @Transient
    @Builder.Default
    private List<User> jugadores = new ArrayList<>();

}
