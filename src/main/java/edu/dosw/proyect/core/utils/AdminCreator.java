package edu.dosw.proyect.core.utils;

import edu.dosw.proyect.core.models.User;
import edu.dosw.proyect.core.models.Admin;
import edu.dosw.proyect.core.models.SportProfile;
import edu.dosw.proyect.controllers.dtos.RegisterRequestDTO;

public class AdminCreator extends UserCreator {
    public AdminCreator() {
        super(new InstitutionalMailStrategy());
    }

    @Override
    public User createUser(RegisterRequestDTO request) {
        SportProfile profile = new SportProfile(request.getPreferredPosition(), request.getSkillLevel());
        return new Admin(request.getName(), request.getEmail(), request.getPassword(), profile);
    }
}

