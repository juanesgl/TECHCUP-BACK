package edu.dosw.proyect.controllers.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {
    private Long          id;
    private String        name;
    private String        lastName;
    private String        email;
    private String        role;
    private String        academicProgram;
    private boolean       active;
    private LocalDateTime registrationDate;
}