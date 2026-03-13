package edu.dosw.proyect.models;

public class Admin extends AbstractUser {
    private SportProfile sportProfile;

    public Admin(String name, String email, String password, SportProfile sportProfile) {
        super(name, email, password, "ADMIN");
        this.sportProfile = sportProfile;
    }

    public SportProfile getSportProfile() { return sportProfile; }
    public void setSportProfile(SportProfile sportProfile) { this.sportProfile = sportProfile; }
}
