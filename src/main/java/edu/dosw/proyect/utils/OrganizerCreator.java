package edu.dosw.proyect.utils;

import edu.dosw.proyect.models.User;
import edu.dosw.proyect.models.Organizer;
import edu.dosw.proyect.dtos.RegisterRequestDTO;

public class OrganizerCreator extends UserCreator {
    public OrganizerCreator() {
        super(new InstitutionalMailStrategy());
    }

    @Override
    public User createUser(RegisterRequestDTO request) {
        return new Organizer(request.getName(), request.getEmail(), request.getPassword());
    }
}
