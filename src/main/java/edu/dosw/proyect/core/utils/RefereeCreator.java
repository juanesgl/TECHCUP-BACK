package edu.dosw.proyect.core.utils;

import edu.dosw.proyect.core.models.User;
import edu.dosw.proyect.core.models.Referee;
import edu.dosw.proyect.controllers.dtos.RegisterRequestDTO;

public class RefereeCreator extends UserCreator {
    public RefereeCreator() {
        super(new GmailStrategy());
    }

    @Override
    public User createUser(RegisterRequestDTO request) {
        return new Referee(request.getName(), request.getEmail(), request.getPassword());
    }
}

