package edu.dosw.proyect.utils;

import edu.dosw.proyect.models.User;
import edu.dosw.proyect.dtos.RegisterRequestDTO;

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
