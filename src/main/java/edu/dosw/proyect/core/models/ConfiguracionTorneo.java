package edu.dosw.proyect.core.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfiguracionTorneo {

    private Long id;
    private Tournament torneo;
    private String reglamento;
    private LocalDate cierreInscripciones;
    private String sanciones;
    private String fechasImportantes;
}

