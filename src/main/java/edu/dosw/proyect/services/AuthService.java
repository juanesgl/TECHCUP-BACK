package edu.dosw.proyect.services;

import edu.dosw.proyect.dtos.LoginRequestDTO;
import edu.dosw.proyect.dtos.LoginResponseDTO;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthService {

    public LoginResponseDTO loginUser(LoginRequestDTO request) {
        if (request.getEmail() == null || request.getPassword() == null) {
            throw new IllegalArgumentException("Email y password son requeridos");
        }

        if ("admin@techcup.com".equals(request.getEmail()) && "admin123".equals(request.getPassword())) {
            String fakeToken = UUID.randomUUID().toString();
            return new LoginResponseDTO("Inicio de sesión exitoso", true, fakeToken);
        }

        if ("user@gmail.com".equals(request.getEmail()) && "user123".equals(request.getPassword())) {
            String fakeToken = UUID.randomUUID().toString();
            return new LoginResponseDTO("Inicio de sesión exitoso", true, fakeToken);
        }

        return new LoginResponseDTO("Credenciales invalidas", false, null);
    }
}
