package edu.dosw.proyect.core.services;

import edu.dosw.proyect.controllers.dtos.request.MatchFilterRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.MatchResponseDTO;

import java.util.List;

public interface PartidoService {

    List<MatchResponseDTO> consultarPartidos(MatchFilterRequestDTO filtro);

    MatchResponseDTO consultarPartidoPorId(Long partidoId);
}
