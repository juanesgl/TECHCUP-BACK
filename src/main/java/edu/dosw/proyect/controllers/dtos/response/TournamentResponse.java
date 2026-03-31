package edu.dosw.proyect.controllers.dtos.response;

import edu.dosw.proyect.core.models.enums.TournamentsStatus;

public record TournamentResponse(
        String turnId,
        String name,
        TournamentsStatus status,
        String message
) {}
