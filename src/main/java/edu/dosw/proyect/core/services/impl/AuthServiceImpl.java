package edu.dosw.proyect.core.services.impl;

import edu.dosw.proyect.controllers.dtos.LoginRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.LoginResponseDTO;
import edu.dosw.proyect.core.models.User;
import edu.dosw.proyect.persistence.repository.UserRepository;
import edu.dosw.proyect.core.services.AuthService;
import edu.dosw.proyect.core.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    @Override
    public LoginResponseDTO loginUser(LoginRequestDTO request) {
        if (request.getEmail() == null || request.getPassword() == null) {
            log.warn("Intento de login sin email o password");
            throw new IllegalArgumentException("Email y password son requeridos");
        }

        User user = userRepository.findByEmail(request.getEmail())
                .orElse(null);

        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            log.warn("Intento fallido de login para: {}", request.getEmail());
            return new LoginResponseDTO("Credenciales invalidas", false, null);
        }
        String jwtToken = jwtProvider.generateToken(user.getEmail(), user.getRole(), user.getId());
        
        log.info("Login exitoso para usuario: {} con rol: {}", user.getEmail(), user.getRole());
        return new LoginResponseDTO("Inicio de sesion exitoso", true, jwtToken);
    }
}

