package edu.dosw.proyect.core.utils;

import edu.dosw.proyect.core.models.User;
import edu.dosw.proyect.core.models.Referee;
import edu.dosw.proyect.controllers.dtos.request.RegisterRequestDTO;

public class RefereeCreator extends UserCreator {
    public RefereeCreator() {
        super(new GmailStrategy());
    }

    @Override
    public User createUser(RegisterRequestDTO request) {
        Referee referee = new Referee();
        referee.setName(request.getName());
        referee.setEmail(request.getEmail());
        referee.setPassword(request.getPassword());
        referee.setRole("REFEREE");
        return referee;
    }
}
