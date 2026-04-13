package edu.dosw.proyect.core.models;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
public class Professor extends User {

    @Deprecated
    public Professor(String name, String email, String password, SportProfile sportProfile) {
        this.setName(name);
        this.setEmail(email);
        this.setPassword(password);
        this.setRole("PLAYER");
    }
}

