package edu.dosw.proyect.core.services;

import edu.dosw.proyect.controllers.dtos.RegisterRequestDTO;
import edu.dosw.proyect.controllers.dtos.RegisterResponseDTO;
import edu.dosw.proyect.core.models.User;
import edu.dosw.proyect.core.repositories.UserRepository;
import edu.dosw.proyect.core.utils.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public RegisterResponseDTO registerUser(RegisterRequestDTO request) {
        UserCreator creator = getCreatorByRole(request.getRole());

        if (creator == null) {
            throw new IllegalArgumentException("Rol no soportado: " + request.getRole());
        }

        if (!creator.validateEmail(request.getEmail())) {
            throw new IllegalArgumentException("Dominio de correo invalido para el rol: " + request.getRole());
        }

        User newUser = creator.createUser(request);
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