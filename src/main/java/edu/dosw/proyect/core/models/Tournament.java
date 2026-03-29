package edu.dosw.proyect.core.models;

import edu.dosw.proyect.core.models.enums.TournamentsStatus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tournament {

    private String tournId;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private int maxTeams;
    private double costPerTeam;
    private TournamentsStatus status;
    private String regulation;
    private Long organizerId;
    private LocalDate registrationCloseDate;
    private String importantDates;
    private String matchSchedules;
    private String sanctions;
    private List<Cancha> canchas = new ArrayList<>();
}
