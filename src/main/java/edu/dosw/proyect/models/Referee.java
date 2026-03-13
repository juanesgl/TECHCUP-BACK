package edu.dosw.proyect.models;

public class Referee extends AbstractUser {
    public Referee(String name, String email, String password) {
        super(name, email, password, "REFEREE");
    }
}
