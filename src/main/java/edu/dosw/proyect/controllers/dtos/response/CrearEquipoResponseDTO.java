package edu.dosw.proyect.controllers.dtos.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CrearEquipoResponseDTO {
    private String mensajeConfirmacion;
    private List<String> notificacionesEnviadas;
}

