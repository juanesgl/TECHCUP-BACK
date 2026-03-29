package edu.dosw.proyect.core.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "JUGADOR")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Jugador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private User usuario;

    @Column(name = "foto_url")
    private String fotoUrl;

    @Column(name = "posiciones")
    private String posiciones;

    @Column(name = "dorsal")
    private Integer dorsal;

    @Column(name = "disponible")
    private boolean disponible;

    @Column(name = "semestre")
    private String semestre;

    @Column(name = "genero")
    private String genero;

    @Column(name = "identificacion")
    private String identificacion;

    @Column(name = "edad")
    private Integer edad;

    // Legacy support
    @Transient
    private String nombre;
    @Column(name = "perfil_completo")
    private boolean perfilCompleto;

    @Column(name = "tiene_equipo")
    private boolean tieneEquipo;

    // Constructor to minimally maintain compatibility if needed elsewhere
    public Jugador(Long id, String nombre, boolean perfilCompleto, boolean tieneEquipo, boolean disponible) {
        this.id = id;
        this.nombre = nombre;
        this.perfilCompleto = perfilCompleto;
        this.tieneEquipo = tieneEquipo;
        this.disponible = disponible;
    }
}
