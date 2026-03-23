package edu.dosw.proyect.core.models;

public class Organizer extends User {
    public Organizer(String name, String email, String password) {
        super(name, email, password, "ORGANIZER");
    }
    @Override
    public String getProgramaAcademico() { return null; }
    @Override
    public void setProgramaAcademico(String programaAcademico) {}

}
