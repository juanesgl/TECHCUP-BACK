package edu.dosw.proyect.core.models;

import java.time.LocalDate;

public record TournamentRequest(

    String name,
    LocalDate startDate,
    LocalDate endDate,
    int maxTeams,
    double costPerTeam,
    String regulation

) {}
