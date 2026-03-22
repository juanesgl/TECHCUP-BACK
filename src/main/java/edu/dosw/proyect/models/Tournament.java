package edu.dosw.proyect.models;

import edu.dosw.proyect.models.enums.TournamentsStatus;

import java.time.LocalDate;

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
}
