package edu.dosw.proyect.persistence.mapper;

import edu.dosw.proyect.core.models.Invitacion;
import edu.dosw.proyect.persistence.entity.InvitacionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * MapStruct mapper.
 * Objeto origen: InvitacionEntity (persistencia) → Invitacion (dominio).
 * Usa EquipoPersistenceMapper y JugadorPersistenceMapper para objetos anidados.
 */
@Mapper(componentModel = "spring", uses = {
        EquipoPersistenceMapper.class,
        JugadorPersistenceMapper.class
})
public interface InvitacionPersistenceMapper {

    @Mapping(target = "equipo",         source = "equipo")
    @Mapping(target = "jugador",        source = "jugador")
    @Mapping(target = "estado",         source = "estado")
    @Mapping(target = "fechaEnvio",     source = "fechaEnvio")
    @Mapping(target = "fechaRespuesta", source = "fechaRespuesta")
    InvitacionEntity toEntity(Invitacion domain);

    @Mapping(target = "equipo",         source = "equipo")
    @Mapping(target = "jugador",        source = "jugador")
    @Mapping(target = "estado",         source = "estado")
    @Mapping(target = "fechaEnvio",     source = "fechaEnvio")
    @Mapping(target = "fechaRespuesta", source = "fechaRespuesta")
    Invitacion toDomain(InvitacionEntity entity);
}