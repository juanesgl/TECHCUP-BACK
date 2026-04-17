package edu.dosw.proyect.controllers.mappers;

import edu.dosw.proyect.controllers.dtos.response.InvitationResponseDTO;
import edu.dosw.proyect.core.models.Invitation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface InvitacionMapper {

    @Mapping(target = "invitacionId",    source = "id")
    @Mapping(target = "estadoActualizado",source = "estado")
    @Mapping(target = "mensajeCapitan",  ignore = true)
    InvitationResponseDTO toResponseDTO(Invitation invitation);

    /**
     * Versión con mensaje explícito — se usa desde InvitacionServiceImpl.
     */
    default InvitationResponseDTO toResponseDTO(Invitation invitation, String mensaje) {
        InvitationResponseDTO dto = toResponseDTO(invitation);
        dto.setMensajeCapitan(mensaje);
        return dto;
    }
}