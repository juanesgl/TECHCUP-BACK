package edu.dosw.proyect.controllers.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDTO {
    private String name;
    private String email;
    private String password;
    private String role;
    private String preferredPosition;
    private int skillLevel;
}

