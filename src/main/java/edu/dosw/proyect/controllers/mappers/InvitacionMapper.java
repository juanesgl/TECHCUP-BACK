package edu.dosw.proyect.controllers.mappers;

import edu.dosw.proyect.controllers.dtos.response.InvitacionResponseDTO;
import edu.dosw.proyect.core.models.Invitacion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * MapStruct mapper.
 * Objeto origen: Invitacion (dominio) → InvitacionResponseDTO.
 */
@Mapper(componentModel = "spring")
public interface InvitacionMapper {

    @Mapping(target = "invitacionId",    source = "id")
    @Mapping(target = "estadoActualizado",source = "estado")
    @Mapping(target = "mensajeCapitan",  ignore = true)
    InvitacionResponseDTO toResponseDTO(Invitacion invitacion);

    /**
     * Versión con mensaje explícito — se usa desde InvitacionServiceImpl.
     */
    default InvitacionResponseDTO toResponseDTO(Invitacion invitacion, String mensaje) {
        InvitacionResponseDTO dto = toResponseDTO(invitacion);
        dto.setMensajeCapitan(mensaje);
        return dto;
    }
}