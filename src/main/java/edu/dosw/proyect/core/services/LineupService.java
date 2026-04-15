package edu.dosw.proyect.core.services;

import edu.dosw.proyect.controllers.dtos.response.OpponentLineupResponseDTO;

public interface LineupService {

    OpponentLineupResponseDTO consultarAlineacionRival(Long partidoId, Long equipoSolicitanteId);
}
