package edu.dosw.proyect.models;

public class Student extends AbstractUser {
    private SportProfile sportProfile;

    public Student(String name, String email, String password, SportProfile sportProfile) {
        super(name, email, password, "STUDENT");
        this.sportProfile = sportProfile;
    }

    public SportProfile getSportProfile() { return sportProfile; }
    public void setSportProfile(SportProfile sportProfile) { this.sportProfile = sportProfile; }
}
