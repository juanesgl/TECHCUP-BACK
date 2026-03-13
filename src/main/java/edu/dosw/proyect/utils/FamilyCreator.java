package edu.dosw.proyect.utils;

import edu.dosw.proyect.models.User;
import edu.dosw.proyect.models.FamilyMember;
import edu.dosw.proyect.models.SportProfile;
import edu.dosw.proyect.dtos.RegisterRequestDTO;

public class FamilyCreator extends UserCreator {
    public FamilyCreator() {
        super(new GmailStrategy());
    }

    @Override
    public User createUser(RegisterRequestDTO request) {
        SportProfile profile = new SportProfile(request.getPreferredPosition(), request.getSkillLevel());
        return new FamilyMember(request.getName(), request.getEmail(), request.getPassword(), profile);
    }
}
