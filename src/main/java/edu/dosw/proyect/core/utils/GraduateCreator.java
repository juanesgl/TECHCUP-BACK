package edu.dosw.proyect.core.utils;

import edu.dosw.proyect.core.models.User;
import edu.dosw.proyect.core.models.Graduate;
import edu.dosw.proyect.controllers.dtos.request.RegisterRequestDTO;

public class GraduateCreator extends UserCreator {
    public GraduateCreator() {
        super(new GmailStrategy());
    }

    @Override
    public User createUser(RegisterRequestDTO request) {
        Graduate graduate = new Graduate();
        graduate.setName(request.getName());
        graduate.setEmail(request.getEmail());
        graduate.setPassword(request.getPassword());
        graduate.setRole("GRADUATE");
        return graduate;
    }
}
