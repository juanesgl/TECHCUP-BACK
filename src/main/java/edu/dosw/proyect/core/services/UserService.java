package edu.dosw.proyect.core.services;

import edu.dosw.proyect.controllers.dtos.RegisterRequestDTO;
import edu.dosw.proyect.controllers.dtos.RegisterResponseDTO;

public interface UserService {

    RegisterResponseDTO registerUser(RegisterRequestDTO request);
}
