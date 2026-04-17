package edu.dosw.proyect.core.utils;

import edu.dosw.proyect.controllers.dtos.request.AvailabilityRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.AvailabilityResponseDTO;
import edu.dosw.proyect.core.models.Player;

public class AvailabilityMapper {

    private AvailabilityMapper() {
        throw new IllegalStateException("Clase utilitaria");
    }

    public static boolean mapRequestToStatus(AvailabilityRequestDTO dto) {
        if (dto == null || dto.getEstadoDisponibilidad() == null) {
            return false;
        }
        return dto.getEstadoDisponibilidad();
    }

    public static AvailabilityResponseDTO mapToResponse(Player entidad, String mensaje) {
        if (entidad == null) {
            return new AvailabilityResponseDTO(mensaje, false);
        }
        return new AvailabilityResponseDTO(mensaje, entidad.isDisponible());
    }
}

