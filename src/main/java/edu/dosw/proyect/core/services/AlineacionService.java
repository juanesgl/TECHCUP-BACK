package edu.dosw.proyect.core.services;

import edu.dosw.proyect.controllers.dtos.response.AlineacionRivalResponseDTO;

public interface AlineacionService {

    AlineacionRivalResponseDTO consultarAlineacionRival(Long partidoId, Long equipoSolicitanteId);
}
