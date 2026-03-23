package edu.dosw.proyect.core.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FamilyMember extends User {
    private SportProfile sportProfile;

    public FamilyMember(String name, String email, String password, SportProfile sportProfile) {
        super(name, email, password, "FAMILY_MEMBER");
        this.sportProfile = sportProfile;
    }
    @Override
    public String getProgramaAcademico() { return null; }
    @Override
    public void setProgramaAcademico(String programaAcademico) {}

}
