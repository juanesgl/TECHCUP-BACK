package edu.dosw.proyect.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Payment {

    private Integer id;
    private Integer userId;
    private Integer tournamentId;
    private String fileUrl;
    private String status;


}
