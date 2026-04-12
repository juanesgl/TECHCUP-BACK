package edu.dosw.proyect.controllers.dtos.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequestDTO {
    private String name;
    private String lastName;
    private String academicProgram;
}