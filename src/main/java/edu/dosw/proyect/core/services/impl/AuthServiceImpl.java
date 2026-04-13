package edu.dosw.proyect.core.services.impl;

import edu.dosw.proyect.controllers.dtos.request.LoginRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.LoginResponseDTO;
import edu.dosw.proyect.persistence.entity.UserEntity;
import edu.dosw.proyect.persistence.mapper.UserPersistenceMapper;
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
    private final UserPersistenceMapper userMapper;

    @Override
    public LoginResponseDTO loginUser(LoginRequestDTO request) {
        UserEntity entity = userRepository.findByEmail(request.getEmail()).orElse(null);

        if (entity == null || !passwordEncoder.matches(request.getPassword(), entity.getPassword())) {
            log.warn("Intento fallido de login para: {}", request.getEmail());
            return new LoginResponseDTO("Credenciales invalidas", false, null, null, null, null);
        }

        String jwtToken = jwtProvider.generateToken(
                entity.getEmail(), entity.getRole(), entity.getId());

        log.info("Login exitoso para usuario: {} con rol: {}", entity.getEmail(), entity.getRole());
        return new LoginResponseDTO("Inicio de sesion exitoso", true, jwtToken,
                entity.getId(), entity.getName(), entity.getRole());
    }
}