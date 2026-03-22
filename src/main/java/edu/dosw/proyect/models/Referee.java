package edu.dosw.proyect.models;

public class Referee extends User {
    public Referee(String name, String email, String password) {
        super(name, email, password, "REFEREE");
    }
    @Override
    public String getProgramaAcademico() { return null; }
    @Override
    public void setProgramaAcademico(String programaAcademico) {}

}
