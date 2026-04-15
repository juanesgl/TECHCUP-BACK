package edu.dosw.proyect.core.services;

import edu.dosw.proyect.controllers.dtos.request.UpdateTeamRequestDTO;
import edu.dosw.proyect.controllers.dtos.request.CreateTeamRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.CreateTeamResponseDTO;
import edu.dosw.proyect.controllers.dtos.response.TeamResponseDTO;

import java.util.List;

public interface TeamService {

    CreateTeamResponseDTO crearEquipo(Long capitanId, CreateTeamRequestDTO request);

    TeamResponseDTO consultarEquipo(Long equipoId);

    List<TeamResponseDTO> consultarEquiposPorTorneo(String tournamentId);

    TeamResponseDTO actualizarEquipo(Long equipoId, Long capitanId,
                                     UpdateTeamRequestDTO request);

    void eliminarEquipo(Long equipoId, Long capitanId);

    List<String> consultarJugadoresEquipo(Long equipoId);
}