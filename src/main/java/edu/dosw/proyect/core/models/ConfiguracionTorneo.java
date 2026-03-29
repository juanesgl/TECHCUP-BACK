package edu.dosw.proyect.core.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "CONFIGURACION_TORNEO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfiguracionTorneo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "torneo_id", unique = true, nullable = false)
    private Tournament torneo;

    @Column(name = "reglamento", columnDefinition = "text")
    private String reglamento;

    @Column(name = "cierre_inscripciones")
    private LocalDate cierreInscripciones;

    @Column(name = "sanciones", columnDefinition = "text")
    private String sanciones;

    @Column(name = "fechas_importantes")
    private String fechasImportantes;
}
