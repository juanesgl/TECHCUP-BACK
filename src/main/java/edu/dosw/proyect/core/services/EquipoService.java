package edu.dosw.proyect.core.services;

import edu.dosw.proyect.controllers.dtos.request.ActualizarEquipoRequestDTO;
import edu.dosw.proyect.controllers.dtos.request.CrearEquipoRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.CrearEquipoResponseDTO;
import edu.dosw.proyect.controllers.dtos.response.EquipoResponseDTO;

import java.util.List;

public interface EquipoService {

    CrearEquipoResponseDTO crearEquipo(Long capitanId, CrearEquipoRequestDTO request);

    EquipoResponseDTO consultarEquipo(Long equipoId);

    List<EquipoResponseDTO> consultarEquiposPorTorneo(String tournamentId);

    EquipoResponseDTO actualizarEquipo(Long equipoId, Long capitanId,
                                       ActualizarEquipoRequestDTO request);

    void eliminarEquipo(Long equipoId, Long capitanId);

    List<String> consultarJugadoresEquipo(Long equipoId);
}