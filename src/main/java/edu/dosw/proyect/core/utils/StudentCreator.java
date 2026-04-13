package edu.dosw.proyect.core.utils;

import edu.dosw.proyect.core.models.User;
import edu.dosw.proyect.core.models.Student;
import edu.dosw.proyect.controllers.dtos.request.RegisterRequestDTO;

public class StudentCreator extends UserCreator {
    public StudentCreator() {
        super(new InstitutionalMailStrategy());
    }

    @Override
    public User createUser(RegisterRequestDTO request) {
        Student student = new Student();
        student.setName(request.getName());
        student.setEmail(request.getEmail());
        student.setPassword(request.getPassword());
        student.setRole("STUDENT");
        return student;
    }
}
