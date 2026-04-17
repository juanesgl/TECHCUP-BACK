package edu.dosw.proyect.core.utils;

import edu.dosw.proyect.core.models.User;
import edu.dosw.proyect.core.models.Professor;
import edu.dosw.proyect.controllers.dtos.request.RegisterRequestDTO;

public class ProfessorCreator extends UserCreator {
    public ProfessorCreator() {
        super(new InstitutionalMailStrategy());
    }

    @Override
    public User createUser(RegisterRequestDTO request) {
        Professor professor = new Professor();
        professor.setName(request.getName());
        professor.setEmail(request.getEmail());
        professor.setPassword(request.getPassword());
        professor.setRole("PROFESSOR");
        return professor;
    }
}
