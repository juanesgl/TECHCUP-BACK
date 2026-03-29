package edu.dosw.proyect.core.utils;

import edu.dosw.proyect.core.models.User;
import edu.dosw.proyect.controllers.dtos.RegisterRequestDTO;

public class CaptainCreator extends UserCreator {
    public CaptainCreator() {
        super(new AnyMailStrategy());
    }

    @Override
    public User createUser(RegisterRequestDTO request) {
        return User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(request.getPassword())
                .role("CAPTAIN")
                .active(true)
                .build();
    }
}
