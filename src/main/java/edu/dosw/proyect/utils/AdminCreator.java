package edu.dosw.proyect.utils;

import edu.dosw.proyect.models.User;
import edu.dosw.proyect.models.Admin;
import edu.dosw.proyect.models.SportProfile;
import edu.dosw.proyect.dtos.RegisterRequestDTO;

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
