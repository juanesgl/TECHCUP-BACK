package edu.dosw.proyect.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Professor extends AbstractUser {
    private SportProfile sportProfile;

    public Professor(String name, String email, String password, SportProfile sportProfile) {
        super(name, email, password, "PROFESSOR");
        this.sportProfile = sportProfile;
    }
}
