package edu.dosw.proyect.persistence.entity;

import edu.dosw.proyect.core.models.enums.EventType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "EVENTO_PARTIDO")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchEventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partido_id", nullable = false)
    private MatchEntity partido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jugador_id", nullable = false)
    private PlayerEntity jugador;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_evento")
    private EventType eventType;

    @Column(name = "minuto")
    private int minuto;

    @Column(name = "descripcion")
    private String descripcion;
}