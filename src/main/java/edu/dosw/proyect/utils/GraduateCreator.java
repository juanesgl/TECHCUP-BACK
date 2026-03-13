package edu.dosw.proyect.utils;

import edu.dosw.proyect.models.User;
import edu.dosw.proyect.models.Graduate;
import edu.dosw.proyect.models.SportProfile;
import edu.dosw.proyect.dtos.RegisterRequestDTO;

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
