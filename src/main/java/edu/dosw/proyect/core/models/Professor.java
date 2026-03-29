package edu.dosw.proyect.core.models;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import jakarta.persistence.Entity;

/**
 * @deprecated Use User entity directly with role="PLAYER"
 * Esta clase se mantiene por compatibilidad con BD pero no debe usarse en nuevas funcionalidades
 */
@Getter
@Setter
@Entity
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
