package edu.dosw.proyect.core.utils;

import edu.dosw.proyect.core.models.User;
import edu.dosw.proyect.core.models.FamilyMember;
import edu.dosw.proyect.controllers.dtos.request.RegisterRequestDTO;

public class FamilyCreator extends UserCreator {
    public FamilyCreator() {
        super(new GmailStrategy());
    }

    @Override
    public User createUser(RegisterRequestDTO request) {
        FamilyMember familyMember = new FamilyMember();
        familyMember.setName(request.getName());
        familyMember.setEmail(request.getEmail());
        familyMember.setPassword(request.getPassword());
        familyMember.setRole("FAMILY_MEMBER");
        return familyMember;
    }
}
