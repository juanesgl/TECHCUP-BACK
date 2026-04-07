package edu.dosw.proyect.core.utils;

import edu.dosw.proyect.core.models.User;
import edu.dosw.proyect.core.models.Organizer;
import edu.dosw.proyect.controllers.dtos.RegisterRequestDTO;

public class OrganizerCreator extends UserCreator {
    public OrganizerCreator() {
        super(new InstitutionalMailStrategy());
    }

    @Override
    public User createUser(RegisterRequestDTO request) {
        return new Organizer(request.getName(), request.getEmail(), request.getPassword());
    }
}

