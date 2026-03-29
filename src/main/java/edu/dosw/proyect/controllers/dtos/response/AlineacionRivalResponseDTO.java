package edu.dosw.proyect.controllers.dtos.response;

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
public class AlineacionRivalResponseDTO {

    private Long partidoId;
    private String nombreEquipoRival;
    private FormacionTecnica formacion;
    private List<String> titulares;
    private List<String> reservas;
    private String mensaje;
}
