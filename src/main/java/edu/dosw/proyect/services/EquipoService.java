package edu.dosw.proyect.services;

import edu.dosw.proyect.dtos.request.CrearEquipoRequestDTO;
import edu.dosw.proyect.dtos.response.CrearEquipoResponseDTO;

public interface EquipoService {
    CrearEquipoResponseDTO crearEquipo(Long capitanId, CrearEquipoRequestDTO request);
}
