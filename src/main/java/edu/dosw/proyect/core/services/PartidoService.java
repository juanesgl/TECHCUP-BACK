package edu.dosw.proyect.core.services;

import edu.dosw.proyect.controllers.dtos.request.PartidoFiltroRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.PartidoResponseDTO;

import java.util.List;

public interface PartidoService {

    List<PartidoResponseDTO> consultarPartidos(PartidoFiltroRequestDTO filtro);

    PartidoResponseDTO consultarPartidoPorId(Long partidoId);
}