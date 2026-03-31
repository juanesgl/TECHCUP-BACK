package edu.dosw.proyect.core.services;

import edu.dosw.proyect.controllers.dtos.RegisterRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.RegisterResponseDTO;

public interface UserService {

    RegisterResponseDTO registerUser(RegisterRequestDTO request);
}
