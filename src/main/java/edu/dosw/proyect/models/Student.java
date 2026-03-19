package edu.dosw.proyect.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Student extends AbstractUser {
    private SportProfile sportProfile;

    public Student(String name, String email, String password, SportProfile sportProfile) {
        super(name, email, password, "STUDENT");
        this.sportProfile = sportProfile;
    }
}
