package edu.dosw.proyect.core.services;

import edu.dosw.proyect.controllers.dtos.RegisterMatchResultRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.RegisterMatchResultResponseDTO;
import edu.dosw.proyect.controllers.dtos.response.StandingsTableResponseDTO;

public interface StandingsTableService {

    
    RegisterMatchResultResponseDTO registerResult(Long matchId,
                                                  RegisterMatchResultRequestDTO request);

    
    StandingsTableResponseDTO getStandings(String tournamentId);
}
