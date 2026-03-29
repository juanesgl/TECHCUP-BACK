package edu.dosw.proyect.controllers.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class CrearEquipoRequestDTO {
    
    @NotBlank(message = "El nombre del equipo es obligatorio")
    @Size(max = 60, message = "El nombre del equipo no puede tener mÃ¡s de 60 caracteres")
    private String nombreEquipo;
    
    private String escudo;
    
    private String coloresUniforme;
    
    @NotNull(message = "La lista de jugadores invitados es obligatoria")
    @Size(min = 6, max = 11, message = "Debes invitar entre 6 y 11 jugadores")
    private List<Long> jugadoresInvitadosIds;
}

