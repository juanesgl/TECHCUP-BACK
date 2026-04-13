package edu.dosw.proyect.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "JUGADOR")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JugadorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private UserEntity usuario;

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

    @Column(name = "identificación")
    private String identificacion;

    @Column(name = "edad")
    private Integer edad;

    @Column(name = "perfil_completo")
    private boolean perfilCompleto;

    @Column(name = "tiene_equipo")
    private boolean tieneEquipo;
}