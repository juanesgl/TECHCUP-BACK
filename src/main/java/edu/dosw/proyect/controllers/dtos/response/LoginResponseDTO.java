package edu.dosw.proyect.controllers.dtos.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO {
    private String message;
    private boolean success;
    private String token;
}
