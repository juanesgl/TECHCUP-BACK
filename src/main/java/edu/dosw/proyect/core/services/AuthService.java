package edu.dosw.proyect.core.services;

import edu.dosw.proyect.controllers.dtos.request.LoginRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.LoginResponseDTO;

public interface AuthService {

    LoginResponseDTO loginUser(LoginRequestDTO request);
}
