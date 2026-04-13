package edu.dosw.proyect.core.services.impl;

import edu.dosw.proyect.controllers.dtos.request.RegisterRequestDTO;
import edu.dosw.proyect.controllers.dtos.request.UpdateUserRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.RegisterResponseDTO;
import edu.dosw.proyect.controllers.dtos.response.UserResponseDTO;
import edu.dosw.proyect.core.exceptions.BusinessRuleException;
import edu.dosw.proyect.core.exceptions.ResourceNotFoundException;
import edu.dosw.proyect.core.models.User;
import edu.dosw.proyect.core.services.UserService;
import edu.dosw.proyect.core.utils.*;
import edu.dosw.proyect.persistence.entity.UserEntity;
import edu.dosw.proyect.persistence.mapper.UserPersistenceMapper;
import edu.dosw.proyect.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository      userRepository;
    private final PasswordEncoder     passwordEncoder;
    private final UserPersistenceMapper userMapper;


    @Override
    public RegisterResponseDTO registerUser(RegisterRequestDTO request) {
        UserCreator creator = getCreatorByRole(request.getRole());

        if (creator == null) {
            throw new IllegalArgumentException("Rol no soportado: " + request.getRole());
        }
        if (!creator.validateEmail(request.getEmail())) {
            throw new IllegalArgumentException(
                    "Dominio de correo inválido para el rol: " + request.getRole());
        }
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalStateException(
                    "El correo ya está registrado: " + request.getEmail());
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
        UserEntity entity = userMapper.toEntity(newUser);

        UserEntity saved;
        try {
            saved = userRepository.save(entity);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException(
                    "El correo ya está registrado: " + request.getEmail());
        }

        return new RegisterResponseDTO("Usuario registrado exitosamente", saved.getId());
    }


    @Override
    public List<UserResponseDTO> getAllUsers(Long requesterId) {
        UserEntity requester = findRequester(requesterId);
        validateIsAdmin(requester);

        return userRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }


    @Override
    public UserResponseDTO getUserById(Long userId, Long requesterId) {
        UserEntity requester = findRequester(requesterId);
        validateAdminOrOwner(requester, userId);

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario con ID " + userId + " no encontrado"));

        return toResponseDTO(user);
    }


    @Override
    public UserResponseDTO updateUser(Long userId, Long requesterId,
                                      UpdateUserRequestDTO request) {
        UserEntity requester = findRequester(requesterId);
        validateAdminOrOwner(requester, userId);

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario con ID " + userId + " no encontrado"));

        if (request.getName() != null && !request.getName().isBlank()) {
            user.setName(request.getName());
        }
        if (request.getLastName() != null && !request.getLastName().isBlank()) {
            user.setLastName(request.getLastName());
        }

        if (request.getAcademicProgram() != null && !request.getAcademicProgram().isBlank()) {
            user.setAcademicProgram(request.getAcademicProgram());
        }

        UserEntity updated = userRepository.save(user);
        log.info("Usuario ID {} actualizado por requester ID {}", userId, requesterId);
        return toResponseDTO(updated);
    }


    @Override
    public void deleteUser(Long userId, Long requesterId) {
        UserEntity requester = findRequester(requesterId);
        validateIsAdmin(requester);

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario con ID " + userId + " no encontrado"));

        userRepository.delete(user);
        log.info("Usuario ID {} eliminado por admin ID {}", userId, requesterId);
    }


    private UserEntity findRequester(Long requesterId) {
        return userRepository.findById(requesterId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario solicitante con ID " + requesterId + " no encontrado"));
    }

    private void validateIsAdmin(UserEntity user) {
        if (!"ADMIN".equals(user.getRole()) && !"ADMINISTRATOR".equals(user.getRole())) {
            throw new BusinessRuleException(
                    "Solo el administrador puede realizar esta acción");
        }
    }

    private void validateAdminOrOwner(UserEntity requester, Long targetUserId) {
        boolean isAdmin = "ADMIN".equals(requester.getRole())
                || "ADMINISTRATOR".equals(requester.getRole());
        boolean isOwner = requester.getId().equals(targetUserId);

        if (!isAdmin && !isOwner) {
            throw new BusinessRuleException(
                    "No tiene permisos para acceder a la información de este usuario");
        }
    }

    private UserResponseDTO toResponseDTO(UserEntity entity) {
        return UserResponseDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .lastName(entity.getLastName())
                .email(entity.getEmail())
                .role(entity.getRole())
                .academicProgram(entity.getAcademicProgram())
                .active(entity.isActive())
                .registrationDate(entity.getRegistrationDate())
                .build();
    }

    private UserCreator getCreatorByRole(String role) {
        if (role == null) return null;
        return switch (role.toUpperCase()) {
            case "STUDENT"       -> new StudentCreator();
            case "GRADUATE"      -> new GraduateCreator();
            case "PROFESSOR"     -> new ProfessorCreator();
            case "ADMIN"         -> new AdminCreator();
            case "FAMILY_MEMBER" -> new FamilyCreator();
            case "ORGANIZER"     -> new OrganizerCreator();
            case "REFEREE"       -> new RefereeCreator();
            case "CAPTAIN"       -> new CaptainCreator();
            case "PLAYER"        -> new PlayerCreator();
            case "ADMINISTRATOR" -> new AdminCreator();
            default              -> null;
        };
    }
}