package edu.dosw.proyect.utils;

import edu.dosw.proyect.models.User;
import edu.dosw.proyect.models.Student;
import edu.dosw.proyect.models.SportProfile;
import edu.dosw.proyect.dtos.RegisterRequestDTO;

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
