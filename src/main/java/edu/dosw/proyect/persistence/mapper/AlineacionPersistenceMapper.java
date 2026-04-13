package edu.dosw.proyect.persistence.mapper;

import edu.dosw.proyect.core.models.Alineacion;
import edu.dosw.proyect.core.models.AlineacionJugador;
import edu.dosw.proyect.persistence.entity.AlineacionEntity;
import edu.dosw.proyect.persistence.entity.AlineacionJugadorEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * MapStruct mapper.
 * Objeto origen: AlineacionEntity (persistencia) → Alineacion (dominio).
 * Usa PartidoPersistenceMapper, EquipoPersistenceMapper y JugadorPersistenceMapper.
 */
@Mapper(componentModel = "spring", uses = {
        PartidoPersistenceMapper.class,
        EquipoPersistenceMapper.class,
        JugadorPersistenceMapper.class
})
public interface AlineacionPersistenceMapper {

    @Mapping(target = "partido",      source = "partido")
    @Mapping(target = "equipo",       source = "equipo")
    @Mapping(target = "formacion",    source = "formacion")
    @Mapping(target = "fechaRegistro",source = "fechaRegistro")
    @Mapping(target = "jugadores",    source = "jugadores")
    AlineacionEntity toEntity(Alineacion domain);

    @Mapping(target = "partido",      source = "partido")
    @Mapping(target = "equipo",       source = "equipo")
    @Mapping(target = "formacion",    source = "formacion")
    @Mapping(target = "fechaRegistro",source = "fechaRegistro")
    @Mapping(target = "jugadores",    source = "jugadores")
    Alineacion toDomain(AlineacionEntity entity);

    @Mapping(target = "jugador",         source = "jugador")
    @Mapping(target = "rol",             source = "rol")
    @Mapping(target = "posicionEnCancha",source = "posicionEnCancha")
    @Mapping(target = "numeroCamiseta",  source = "numeroCamiseta")
    @Mapping(target = "alineacion",      ignore = true)
    AlineacionJugadorEntity toJugadorEntity(AlineacionJugador domain);

    @Mapping(target = "jugador",         source = "jugador")
    @Mapping(target = "rol",             source = "rol")
    @Mapping(target = "posicionEnCancha",source = "posicionEnCancha")
    @Mapping(target = "numeroCamiseta",  source = "numeroCamiseta")
    @Mapping(target = "alineacion",      ignore = true)
    AlineacionJugador toJugadorDomain(AlineacionJugadorEntity entity);
}