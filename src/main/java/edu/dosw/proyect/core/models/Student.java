package edu.dosw.proyect.core.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Student extends User {
    private SportProfile sportProfile;

    public Student(String name, String email, String password, SportProfile sportProfile) {
        super(name, email, password, "STUDENT");
        this.sportProfile = sportProfile;
    }

    @Override
    public SportProfile getSportProfile() {
        return sportProfile;
    }
}