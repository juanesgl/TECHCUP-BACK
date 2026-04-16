package edu.dosw.proyect.controllers.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;

public record TournamentRequest(

    @NotBlank(message = "El nombre del torneo es obligatorio")
    String name,

    @NotNull(message = "La fecha de inicio es obligatoria")
    LocalDate startDate,

    @NotNull(message = "La fecha de fin es obligatoria")
    LocalDate endDate,

    @NotNull(message = "El máximo de equipos es obligatorio")
    @Positive(message = "El máximo de equipos debe ser mayor que 0")
    Integer maxTeams,

    @NotNull(message = "El costo por equipo es obligatorio")
    @Positive(message = "El costo por equipo debe ser mayor que 0")
    Double costPerTeam,

    @NotBlank(message = "El reglamento es obligatorio")
    String regulation

) {}
