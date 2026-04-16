package edu.dosw.proyect.core.services;

import edu.dosw.proyect.controllers.dtos.request.RegisterMatchRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.RegisterMatchResponseDTO;

public interface MatchRegistrationService {

    RegisterMatchResponseDTO registerMatch(Long matchId, RegisterMatchRequestDTO request);
}