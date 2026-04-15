package edu.dosw.proyect.persistence.mapper;

import edu.dosw.proyect.core.models.Invitation;
import edu.dosw.proyect.persistence.entity.InvitationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {
        TeamPersistenceMapper.class,
        PlayerPersistenceMapper.class
})
public interface InvitationPersistenceMapper {

    @Mapping(target = "equipo", source = "equipo")
    @Mapping(target = "jugador", source = "jugador")
    @Mapping(target = "estado", source = "estado")
    @Mapping(target = "fechaEnvio", source = "fechaEnvio")
    @Mapping(target = "fechaRespuesta", source = "fechaRespuesta")
    InvitationEntity toEntity(Invitation domain);

    @Mapping(target = "team", source = "team")
    @Mapping(target = "jugador", source = "jugador")
    @Mapping(target = "estado", source = "estado")
    @Mapping(target = "fechaEnvio", source = "fechaEnvio")
    @Mapping(target = "fechaRespuesta", source = "fechaRespuesta")
    Invitation toDomain(InvitationEntity entity);
}