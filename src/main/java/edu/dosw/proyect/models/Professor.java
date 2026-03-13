package edu.dosw.proyect.models;

public class Professor extends AbstractUser {
    private SportProfile sportProfile;

    public Professor(String name, String email, String password, SportProfile sportProfile) {
        super(name, email, password, "PROFESSOR");
        this.sportProfile = sportProfile;
    }

    public SportProfile getSportProfile() { return sportProfile; }
    public void setSportProfile(SportProfile sportProfile) { this.sportProfile = sportProfile; }
}
