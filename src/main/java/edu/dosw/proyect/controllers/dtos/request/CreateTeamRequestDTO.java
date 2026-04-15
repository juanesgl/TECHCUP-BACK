package edu.dosw.proyect.controllers.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class CreateTeamRequestDTO {

    @NotBlank(message = "El nombre del equipo es obligatorio")
    @Size(max = 60, message = "El nombre del equipo no puede tener más de 60 caracteres")
    private String nombreEquipo;

    private String escudo;

    private String coloresUniforme;

    @NotNull(message = "La lista de jugadores invitados es obligatoria")
    @Size(min = 7, max = 11,
            message = "Debes invitar entre 7 y 11 jugadores (el capitán se suma para llegar al mínimo de 8)")
    private List<Long> jugadoresInvitadosIds;
}