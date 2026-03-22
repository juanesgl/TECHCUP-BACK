package edu.dosw.proyect.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Graduate extends AbstractUser {
    private SportProfile sportProfile;

    public Graduate(String name, String email, String password, SportProfile sportProfile) {
        super(name, email, password, "GRADUATE");
        this.sportProfile = sportProfile;
    }
    @Override
    public String getProgramaAcademico() { return null; }
    @Override
    public void setProgramaAcademico(String programaAcademico) {}

}
