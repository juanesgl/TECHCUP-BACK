package edu.dosw.proyect.controllers.dtos;

import lombok.Data;

@Data
public class PlayerFilterRequest {

    private String name;
    private String position;
    private Integer age;
    private String semester;
    private Boolean available;
}
