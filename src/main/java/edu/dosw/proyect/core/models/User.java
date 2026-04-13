package edu.dosw.proyect.core.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    private Long id;
    private String name;
    private String lastName;
    private String email;
    private String password;
    private String role;
    private LocalDateTime registrationDate;

    @Builder.Default
    private boolean active = true;

    private String academicProgram;
}