package edu.dosw.proyect.models;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public abstract class User {
    private Long id;
    private String name;
    private String email;
    private String password;
    private String role;

    private String programaAcademico;

    public User(String name, String email, String password, String role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public SportProfile getSportProfile() {
        return null; // Sobreescrito por subclases relevantes
    }
}
