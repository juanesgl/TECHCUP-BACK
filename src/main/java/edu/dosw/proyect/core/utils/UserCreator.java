package edu.dosw.proyect.core.utils;

import edu.dosw.proyect.core.models.User;
import edu.dosw.proyect.controllers.dtos.RegisterRequestDTO;

public abstract class UserCreator {
    protected AuthStrategy authStrategy;

    public UserCreator(AuthStrategy authStrategy) {
        this.authStrategy = authStrategy;
    }

    public abstract User createUser(RegisterRequestDTO request);

    public boolean validateEmail(String email) {
        return authStrategy.validate(email);
    }
}
