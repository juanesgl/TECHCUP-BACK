package edu.dosw.proyect.core.models;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
public class Graduate extends User {

    @Deprecated
    public Graduate(String name, String email, String password, SportProfile sportProfile) {
        this.setName(name);
        this.setEmail(email);
        this.setPassword(password);
        this.setRole("PLAYER");
    }
}

