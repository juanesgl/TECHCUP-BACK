package edu.dosw.proyect.persistence.mapper;

import edu.dosw.proyect.core.models.SoccerField;
import edu.dosw.proyect.persistence.entity.SoccerFieldEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = { TournamentPersistenceMapper.class })
public interface SoccerFieldPersistenceMapper {

    @Mapping(target = "nombre", source = "nombre")
    @Mapping(target = "direccion", source = "direccion")
    @Mapping(target = "descripcion", source = "descripcion")
    @Mapping(target = "torneo", source = "torneo")
    SoccerFieldEntity toEntity(SoccerField domain);

    @Mapping(target = "nombre", source = "nombre")
    @Mapping(target = "direccion", source = "direccion")
    @Mapping(target = "descripcion", source = "descripcion")
    @Mapping(target = "torneo", source = "torneo")
    SoccerField toDomain(SoccerFieldEntity entity);
}