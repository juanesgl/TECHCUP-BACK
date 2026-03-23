package edu.dosw.proyect.core.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Professor extends User {
    private SportProfile sportProfile;

    public Professor(String name, String email, String password, SportProfile sportProfile) {
        super(name, email, password, "PROFESSOR");
        this.sportProfile = sportProfile;
    }
}
