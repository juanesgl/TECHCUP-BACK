package edu.dosw.proyect.core.utils;

import edu.dosw.proyect.core.models.User;
import edu.dosw.proyect.core.models.Student;
import edu.dosw.proyect.core.models.SportProfile;
import edu.dosw.proyect.controllers.dtos.request.RegisterRequestDTO;

public class StudentCreator extends UserCreator {
    public StudentCreator() {
        super(new InstitutionalMailStrategy());
    }

    @Override
    public User createUser(RegisterRequestDTO request) {
        SportProfile profile = new SportProfile(request.getPreferredPosition(), request.getSkillLevel());
        return new Student(request.getName(), request.getEmail(), request.getPassword(), profile);
    }
}

