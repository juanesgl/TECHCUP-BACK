package edu.dosw.proyect.core.models;

import edu.dosw.proyect.core.models.enums.FormacionTecnica;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Alineacion {

    private Long id;
    private Long partidoId;
    private Long equipoId;
    private String nombreEquipo;
    private List<String> titulares;
    private List<String> reservas;
    private FormacionTecnica formacion;
}
