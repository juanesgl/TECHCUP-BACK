package edu.dosw.proyect.core.models;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import jakarta.persistence.Entity;

/**
 * @deprecated Use User entity directly with role="ADMINISTRATOR"
 * Esta clase se mantiene por compatibilidad con BD pero no debe usarse en nuevas funcionalidades
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
public class Admin extends User {

    @Deprecated
    public Admin(String name, String email, String password, SportProfile sportProfile) {
        this.setName(name);
        this.setEmail(email);
        this.setPassword(password);
        this.setRole("ADMINISTRATOR");
    }
}
