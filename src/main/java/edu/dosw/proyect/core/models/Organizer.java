package edu.dosw.proyect.core.models;

import lombok.NoArgsConstructor;
import jakarta.persistence.Entity;

/**
 * @deprecated Use User entity directly with role="ORGANIZER"
 * Esta clase se mantiene por compatibilidad con BD pero no debe usarse en nuevas funcionalidades
 */
@Entity
@NoArgsConstructor
public class Organizer extends User {
    
    @Deprecated
    public Organizer(String name, String email, String password) {
        this.setName(name);
        this.setEmail(email);
        this.setPassword(password);
        this.setRole("ORGANIZER");
    }
}
