package edu.dosw.proyect.mappers;

import edu.dosw.proyect.dtos.response.InvitacionResponseDTO;
import edu.dosw.proyect.models.Invitacion;
import org.springframework.stereotype.Component;

@Component
public class InvitacionMapper {
    
    public InvitacionResponseDTO toResponseDTO(Invitacion invitacion, String mensaje) {
        return InvitacionResponseDTO.builder()
                .invitacionId(invitacion.getId())
                .mensajeCapitan(mensaje)
                .estadoActualizado(invitacion.getEstado().name())
                .build();
    }
}
