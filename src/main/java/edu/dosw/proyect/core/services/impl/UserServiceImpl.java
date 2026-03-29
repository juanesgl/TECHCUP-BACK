package edu.dosw.proyect.core.services.impl;

import edu.dosw.proyect.controllers.dtos.RegisterRequestDTO;
import edu.dosw.proyect.controllers.dtos.RegisterResponseDTO;
import edu.dosw.proyect.core.models.User;
import edu.dosw.proyect.core.repositories.UserRepository;
import edu.dosw.proyect.core.services.UserService;
import edu.dosw.proyect.core.utils.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public RegisterResponseDTO registerUser(RegisterRequestDTO request) {
        UserCreator creator = getCreatorByRole(request.getRole());

        if (creator == null) {
            throw new IllegalArgumentException("Rol no soportado: " + request.getRole());
        }

        if (!creator.validateEmail(request.getEmail())) {
            throw new IllegalArgumentException("Dominio de correo invalido para el rol: " + request.getRole());
        }

        RegisterRequestDTO hashedRequest = new RegisterRequestDTO(
                request.getName(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                request.getRole(),
                request.getPreferredPosition(),
                request.getSkillLevel()
        );

        User newUser = creator.createUser(hashedRequest);
        User saved = userRepository.save(newUser);

        return new RegisterResponseDTO("Usuario registrado exitosamente", saved.getId());
    }

    private UserCreator getCreatorByRole(String role) {
        if (role == null) return null;

        return switch (role.toUpperCase()) {
            case "STUDENT"      -> new StudentCreator();
            case "GRADUATE"     -> new GraduateCreator();
            case "PROFESSOR"    -> new ProfessorCreator();
            case "ADMIN"        -> new AdminCreator();
            case "FAMILY_MEMBER"-> new FamilyCreator();
            case "ORGANIZER"    -> new OrganizerCreator();
            case "REFEREE"      -> new RefereeCreator();
            default             -> null;
        };
    }
}