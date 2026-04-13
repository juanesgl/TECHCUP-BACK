package edu.dosw.proyect.controllers.dtos.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO {
    private String message;
    private boolean success;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String token;
}
