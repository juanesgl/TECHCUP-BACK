package edu.dosw.proyect.models;

public class Graduate extends AbstractUser {
    private SportProfile sportProfile;

    public Graduate(String name, String email, String password, SportProfile sportProfile) {
        super(name, email, password, "GRADUATE");
        this.sportProfile = sportProfile;
    }

    public SportProfile getSportProfile() { return sportProfile; }
    public void setSportProfile(SportProfile sportProfile) { this.sportProfile = sportProfile; }
}
