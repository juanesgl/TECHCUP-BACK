package edu.dosw.proyect.utils;

import edu.dosw.proyect.models.User;
import edu.dosw.proyect.models.Referee;
import edu.dosw.proyect.dtos.RegisterRequestDTO;

public class RefereeCreator extends UserCreator {
    public RefereeCreator() {
        super(new GmailStrategy());
    }

    @Override
    public User createUser(RegisterRequestDTO request) {
        return new Referee(request.getName(), request.getEmail(), request.getPassword());
    }
}
