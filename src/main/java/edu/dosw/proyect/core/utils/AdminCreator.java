package edu.dosw.proyect.core.utils;

import edu.dosw.proyect.core.models.User;
import edu.dosw.proyect.core.models.Admin;
import edu.dosw.proyect.controllers.dtos.request.RegisterRequestDTO;

public class AdminCreator extends UserCreator {
    public AdminCreator() {
        super(new InstitutionalMailStrategy());
    }

    @Override
    public User createUser(RegisterRequestDTO request) {
        Admin admin = new Admin();
        admin.setName(request.getName());
        admin.setEmail(request.getEmail());
        admin.setPassword(request.getPassword());
        admin.setRole("ADMINISTRATOR");
        return admin;
    }
}

