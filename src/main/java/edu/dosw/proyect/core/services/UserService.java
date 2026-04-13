package edu.dosw.proyect.core.services;

import edu.dosw.proyect.controllers.dtos.request.RegisterRequestDTO;
import edu.dosw.proyect.controllers.dtos.request.UpdateUserRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.RegisterResponseDTO;
import edu.dosw.proyect.controllers.dtos.response.UserResponseDTO;

import java.util.List;

public interface UserService {

    RegisterResponseDTO registerUser(RegisterRequestDTO request);

    List<UserResponseDTO> getAllUsers(Long requesterId);

    UserResponseDTO getUserById(Long userId, Long requesterId);

    UserResponseDTO updateUser(Long userId, Long requesterId, UpdateUserRequestDTO request);

    void deleteUser(Long userId, Long requesterId);
}