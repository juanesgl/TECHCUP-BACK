package edu.dosw.proyect.core.utils;

import edu.dosw.proyect.core.models.User;
import edu.dosw.proyect.core.models.Graduate;
import edu.dosw.proyect.core.models.SportProfile;
import edu.dosw.proyect.controllers.dtos.RegisterRequestDTO;

public class GraduateCreator extends UserCreator {
    public GraduateCreator() {
        super(new GmailStrategy());
    }

    @Override
    public User createUser(RegisterRequestDTO request) {
        SportProfile profile = new SportProfile(request.getPreferredPosition(), request.getSkillLevel());
        return new Graduate(request.getName(), request.getEmail(), request.getPassword(), profile);
    }
}

