package edu.dosw.proyect.core.services;

import edu.dosw.proyect.controllers.dtos.request.RegisterMatchRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.RegisterMatchResponseDTO;

public interface MatchRegistrationService {

    /**
     * Registra el resultado completo de un partido:
     * marcador, goleadores, tarjetas amarillas y tarjetas rojas.
     *
     * @param matchId del partido a registrar
     * @param request   Datos del resultado y eventos
     * @return DTO con el resumen del partido registrado
     */
    RegisterMatchResponseDTO registerMatch(Long matchId, RegisterMatchRequestDTO request);
}