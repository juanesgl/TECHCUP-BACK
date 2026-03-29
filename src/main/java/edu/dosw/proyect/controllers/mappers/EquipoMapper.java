package edu.dosw.proyect.controllers.mappers;

import edu.dosw.proyect.controllers.dtos.response.CrearEquipoResponseDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EquipoMapper {

    public CrearEquipoResponseDTO toCrearEquipoResponseDTO(String mensaje, List<String> notificaciones) {
        return CrearEquipoResponseDTO.builder()
                .mensajeConfirmacion(mensaje)
                .notificacionesEnviadas(notificaciones)
                .build();
    }
}

