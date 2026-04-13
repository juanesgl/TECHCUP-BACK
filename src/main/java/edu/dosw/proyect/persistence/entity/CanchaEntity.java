package edu.dosw.proyect.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "CANCHA")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CanchaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "torneo_id")
    private TournamentEntity torneo;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "dirección")
    private String direccion;

    @Column(name = "descripcion")
    private String descripcion;
}