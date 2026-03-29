package edu.dosw.proyect.core.services;

import edu.dosw.proyect.controllers.dtos.LoginRequestDTO;
import edu.dosw.proyect.controllers.dtos.LoginResponseDTO;

public interface AuthService {

    LoginResponseDTO loginUser(LoginRequestDTO request);
}
