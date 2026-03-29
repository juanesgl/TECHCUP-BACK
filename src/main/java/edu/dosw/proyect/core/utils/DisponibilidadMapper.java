package edu.dosw.proyect.core.utils;

import edu.dosw.proyect.controllers.dtos.DisponibilidadRequestDTO;
import edu.dosw.proyect.controllers.dtos.DisponibilidadResponseDTO;
import edu.dosw.proyect.core.models.Jugador;

public class DisponibilidadMapper {

    private DisponibilidadMapper() {
        throw new IllegalStateException("Clase utilitaria");
    }

    public static boolean mapRequestToStatus(DisponibilidadRequestDTO dto) {
        if (dto == null || dto.getEstadoDisponibilidad() == null) {
            return false;
        }
        return dto.getEstadoDisponibilidad();
    }

    public static DisponibilidadResponseDTO mapToResponse(Jugador entidad, String mensaje) {
        if (entidad == null) {
            return new DisponibilidadResponseDTO(mensaje, false);
        }
        return new DisponibilidadResponseDTO(mensaje, entidad.isDisponible());
    }
}

