package edu.dosw.proyect.core.services;

import edu.dosw.proyect.controllers.dtos.request.CrearEquipoRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.CrearEquipoResponseDTO;

public interface EquipoService {
    CrearEquipoResponseDTO crearEquipo(Long capitanId, CrearEquipoRequestDTO request);
}
