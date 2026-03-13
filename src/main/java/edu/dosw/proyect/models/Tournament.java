package edu.dosw.proyect.models;

import edu.dosw.proyect.models.enums.TournamentsStatus;

import java.time.LocalDate;

public class Tournament {

    private String tournId;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private int maxTeams;
    private double costPerTeam;
    private TournamentsStatus status;
    private String regulation;

    public Tournament(String turnId, String name, LocalDate startDate, LocalDate endDate, int maxTeams, double costPerTeam, TournamentsStatus status, String regulation) {
        this.tournId = turnId;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.maxTeams = maxTeams;
        this.costPerTeam = costPerTeam;
        this.status = status;
        this.regulation = regulation;
    }

    public String getTournId() {
        return tournId;
    }

    public String getName() {
        return name;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public int getMaxTeams() {
        return maxTeams;
    }

    public double getCostPerTeam() {
        return costPerTeam;
    }

    public TournamentsStatus getStatus() {
        return status;
    }

    public String getRegulation() {
        return regulation;
    }

    public void setTourId(String tournId) { this.tournId = tournId;}
    public void setStatus(TournamentsStatus status) { this.status = status;}

}
