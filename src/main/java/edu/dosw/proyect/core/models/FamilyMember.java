package edu.dosw.proyect.core.models;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import jakarta.persistence.Entity;

@Getter
@Setter
@NoArgsConstructor
public class FamilyMember extends User {

    @Deprecated
    public FamilyMember(String name, String email, String password, SportProfile sportProfile) {
        this.setName(name);
        this.setEmail(email);
        this.setPassword(password);
        this.setRole("PLAYER");
    }
}

