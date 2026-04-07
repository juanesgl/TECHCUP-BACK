package edu.dosw.proyect.controllers.mappers;

import edu.dosw.proyect.controllers.dtos.response.InvitacionResponseDTO;
import edu.dosw.proyect.core.models.Invitacion;
import org.springframework.stereotype.Component;

@Component
public class InvitacionMapper {

    public InvitacionResponseDTO toResponseDTO(Invitacion invitacion, String mensaje) {
        return InvitacionResponseDTO.builder()
                .invitacionId(invitacion.getId())
                .mensajeCapitan(mensaje)
                .estadoActualizado(invitacion.getEstado())
                .build();
    }
}

