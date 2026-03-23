package edu.dosw.proyect.core.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Admin extends User {
    private SportProfile sportProfile;

    public Admin(String name, String email, String password, SportProfile sportProfile) {
        super(name, email, password, "ADMIN");
        this.sportProfile = sportProfile;
    }
    @Override
    public String getProgramaAcademico() { return null; }
    @Override
    public void setProgramaAcademico(String programaAcademico) {}

}
