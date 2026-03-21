package edu.dosw.proyect.models;

public class Referee extends User {
    public Referee(String name, String email, String password) {
        super(name, email, password, "REFEREE");
    }
}
