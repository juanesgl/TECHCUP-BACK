package edu.dosw.proyect.controllers.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Respuesta con la información completa de un equipo.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamResponseDTO {
    private Long   equipoId;
    private String nombre;
    private String escudoUrl;
    private String colorUniformeLocal;
    private String colorUniformeVisita;
    private String estadoInscripcion;
    private String torneoId;
    private Long   capitanId;
    private String capitanNombre;
}