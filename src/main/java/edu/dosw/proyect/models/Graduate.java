package edu.dosw.proyect.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Graduate extends User {
    private SportProfile sportProfile;

    public Graduate(String name, String email, String password, SportProfile sportProfile) {
        super(name, email, password, "GRADUATE");
        this.sportProfile = sportProfile;
    }
}
