package edu.dosw.proyect.models;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public abstract class AbstractUser implements User {
    private Long id;
    private String name;
    private String email;
    private String password;
    private String role;

    public AbstractUser(String name, String email, String password, String role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }
}
