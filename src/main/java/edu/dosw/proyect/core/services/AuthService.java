package edu.dosw.proyect.core.services;

import edu.dosw.proyect.controllers.dtos.LoginRequestDTO;
import edu.dosw.proyect.controllers.dtos.LoginResponseDTO;
import edu.dosw.proyect.core.models.User;
import edu.dosw.proyect.core.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    public LoginResponseDTO loginUser(LoginRequestDTO request) {
        if (request.getEmail() == null || request.getPassword() == null) {
            throw new IllegalArgumentException("Email y password son requeridos");
        }

        return userRepository.findByEmail(request.getEmail())
                .filter(user -> user.getPassword().equals(request.getPassword()))
                .map(user -> {
                    String fakeToken = UUID.randomUUID().toString();
                    return new LoginResponseDTO("Inicio de sesión exitoso", true, fakeToken);
                })
                .orElse(new LoginResponseDTO("Credenciales invalidas", false, null));
    }
}
