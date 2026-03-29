package edu.dosw.proyect.controllers.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PlayerResponse {

    private Long id;
    private String name;
    private String position;
    private Integer age;
}

