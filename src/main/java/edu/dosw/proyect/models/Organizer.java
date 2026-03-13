package edu.dosw.proyect.models;

public class Organizer extends AbstractUser {
    public Organizer(String name, String email, String password) {
        super(name, email, password, "ORGANIZER");
    }
}
