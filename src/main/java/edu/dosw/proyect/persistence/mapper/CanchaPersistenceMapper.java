package edu.dosw.proyect.persistence.mapper;

import edu.dosw.proyect.core.models.Cancha;
import edu.dosw.proyect.persistence.entity.CanchaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * MapStruct mapper.
 * Objeto origen: CanchaEntity (persistencia) → Cancha (dominio).
 * Usa TournamentPersistenceMapper para el torneo anidado.
 */
@Mapper(componentModel = "spring", uses = { TournamentPersistenceMapper.class })
public interface CanchaPersistenceMapper {

    @Mapping(target = "nombre",     source = "nombre")
    @Mapping(target = "direccion",  source = "direccion")
    @Mapping(target = "descripcion",source = "descripcion")
    @Mapping(target = "torneo",     source = "torneo")
    CanchaEntity toEntity(Cancha domain);

    @Mapping(target = "nombre",     source = "nombre")
    @Mapping(target = "direccion",  source = "direccion")
    @Mapping(target = "descripcion",source = "descripcion")
    @Mapping(target = "torneo",     source = "torneo")
    Cancha toDomain(CanchaEntity entity);
}