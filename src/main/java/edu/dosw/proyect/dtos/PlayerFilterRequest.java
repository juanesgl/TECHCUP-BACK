package edu.dosw.proyect.dtos;

import lombok.Data;

@Data
public class PlayerFilterRequest {

    private String name;
    private String position;
    private Integer age;
}
