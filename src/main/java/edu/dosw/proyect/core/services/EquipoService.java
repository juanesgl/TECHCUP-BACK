package edu.dosw.proyect.core.services;

import edu.dosw.proyect.controllers.dtos.request.CrearEquipoRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.CrearEquipoResponseDTO;
import org.springframework.stereotype.Service;

@Service
public interface EquipoService {
    CrearEquipoResponseDTO crearEquipo(Long capitanId, CrearEquipoRequestDTO request);
}
