package edu.dosw.proyect.core.models;

import edu.dosw.proyect.core.models.enums.TournamentsStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Tournament {

    private Long id;
    private String tournId;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private int maxTeams;
    private double costPerTeam;
    private TournamentsStatus status;
    private User organizador;

    private String regulation;
    private Long organizerId;
    private LocalDate registrationCloseDate;
    private String importantDates;
    private String matchSchedules;
    private String sanctions;

    @Builder.Default
    private List<Cancha> canchas = new ArrayList<>();

    public Tournament(String tournId, String name, LocalDate startDate,
                      LocalDate endDate, int maxTeams, double costPerTeam,
                      TournamentsStatus status, String regulation) {
        this.tournId = tournId;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.maxTeams = maxTeams;
        this.costPerTeam = costPerTeam;
        this.status = status;
        this.regulation = regulation;
    }
}