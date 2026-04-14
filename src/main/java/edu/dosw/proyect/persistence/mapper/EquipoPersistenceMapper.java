package edu.dosw.proyect.persistence.mapper;

import edu.dosw.proyect.core.models.Equipo;
import edu.dosw.proyect.persistence.entity.EquipoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = { TournamentPersistenceMapper.class, JugadorPersistenceMapper.class })
public interface EquipoPersistenceMapper {

    @Mapping(target = "nombre", source = "nombre")
    @Mapping(target = "escudoUrl", source = "escudoUrl")
    @Mapping(target = "colorUniformeLocal", source = "colorUniformeLocal")
    @Mapping(target = "colorUniformeVisita", source = "colorUniformeVisita")
    @Mapping(target = "estadoInscripcion", source = "estadoInscripcion")
    @Mapping(target = "torneo", source = "torneo")
    @Mapping(target = "capitan", source = "capitan")
    @Mapping(target = "equipoJugadores", ignore = true)
    EquipoEntity toEntity(Equipo domain);

    @Mapping(target = "nombre", source = "nombre")
    @Mapping(target = "escudoUrl", source = "escudoUrl")
    @Mapping(target = "colorUniformeLocal", source = "colorUniformeLocal")
    @Mapping(target = "colorUniformeVisita", source = "colorUniformeVisita")
    @Mapping(target = "estadoInscripcion", source = "estadoInscripcion")
    @Mapping(target = "torneo", source = "torneo")
    @Mapping(target = "capitan", source = "capitan")
    @Mapping(target = "equipoJugadores", ignore = true)
    @Mapping(target = "escudo", ignore = true)
    @Mapping(target = "coloresUniforme", ignore = true)
    @Mapping(target = "capitanLegacy", ignore = true)
    @Mapping(target = "jugadores", ignore = true)
    Equipo toDomain(EquipoEntity entity);
}