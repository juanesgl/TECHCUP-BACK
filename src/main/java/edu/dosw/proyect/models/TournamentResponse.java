package edu.dosw.proyect.models;

import edu.dosw.proyect.models.enums.TournamentsStatus;

public record TournamentResponse(
        String turnId,
        String name,
        TournamentsStatus status,
        String message
) {}