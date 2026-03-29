package edu.dosw.proyect.core.models;

import lombok.NoArgsConstructor;
import jakarta.persistence.Entity;

@Entity
@NoArgsConstructor
public class Referee extends User {
    
    @Deprecated
    public Referee(String name, String email, String password) {
        this.setName(name);
        this.setEmail(email);
        this.setPassword(password);
        this.setRole("REFEREE");
    }
}

