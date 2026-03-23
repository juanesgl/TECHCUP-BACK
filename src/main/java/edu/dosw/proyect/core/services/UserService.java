package edu.dosw.proyect.core.services;

import edu.dosw.proyect.controllers.dtos.RegisterRequestDTO;
import edu.dosw.proyect.controllers.dtos.RegisterResponseDTO;
import edu.dosw.proyect.core.models.User;
import edu.dosw.proyect.core.utils.*;
import edu.dosw.proyect.utils.*;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class UserService {
    private final Map<Long, User> userRepository = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public RegisterResponseDTO registerUser(RegisterRequestDTO request) {
        UserCreator creator = getCreatorByRole(request.getRole());
        
        if (creator == null) {
            throw new IllegalArgumentException("Rol no soportado: " + request.getRole());
        }

        if (!creator.validateEmail(request.getEmail())) {
            throw new IllegalArgumentException("Dominio de correo invalido para el rol: " + request.getRole());
        }

        User newUser = creator.createUser(request);
        newUser.setId(idGenerator.getAndIncrement());
        
        userRepository.put(newUser.getId(), newUser);

        return new RegisterResponseDTO("Usuario registrado exitosamente", newUser.getId());
    }

    private UserCreator getCreatorByRole(String role) {
        if (role == null) return null;
        
        switch (role.toUpperCase()) {
            case "STUDENT": return new StudentCreator();
            case "GRADUATE": return new GraduateCreator();
            case "PROFESSOR": return new ProfessorCreator();
            case "ADMIN": return new AdminCreator();
            case "FAMILY_MEMBER": return new FamilyCreator();
            case "ORGANIZER": return new OrganizerCreator();
            case "REFEREE": return new RefereeCreator();
            default: return null;
        }
    }

    public Map<Long, User> getUserRepository() {
        return userRepository;
    }
}
