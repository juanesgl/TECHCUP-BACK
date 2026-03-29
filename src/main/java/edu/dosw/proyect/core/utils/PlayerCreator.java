package edu.dosw.proyect.core.utils;

import edu.dosw.proyect.core.models.User;
import edu.dosw.proyect.controllers.dtos.RegisterRequestDTO;

public class PlayerCreator extends UserCreator {
    public PlayerCreator() {
        super(new AnyMailStrategy());
    }

    @Override
    public User createUser(RegisterRequestDTO request) {
        return User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(request.getPassword())
                .role("PLAYER")
                .active(true)
                .build();
    }
}
