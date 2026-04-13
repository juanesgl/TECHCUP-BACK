package edu.dosw.proyect.persistence.mapper;

import edu.dosw.proyect.core.models.Partido;
import edu.dosw.proyect.persistence.entity.PartidoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * MapStruct mapper.
 * Objeto origen: PartidoEntity (persistencia) → Partido (dominio).
 * Usa múltiples mappers para objetos anidados.
 */
@Mapper(componentModel = "spring", uses = {
        TournamentPersistenceMapper.class,
        EquipoPersistenceMapper.class,
        CanchaPersistenceMapper.class,
        UserPersistenceMapper.class
})
public interface PartidoPersistenceMapper {

    @Mapping(target = "torneo",          source = "torneo")
    @Mapping(target = "equipoLocal",     source = "equipoLocal")
    @Mapping(target = "equipoVisitante", source = "equipoVisitante")
    @Mapping(target = "cancha",          source = "cancha")
    @Mapping(target = "arbitro",         source = "arbitro")
    @Mapping(target = "fechaHora",       source = "fechaHora")
    @Mapping(target = "golesLocal",      source = "golesLocal")
    @Mapping(target = "golesVisitante",  source = "golesVisitante")
    @Mapping(target = "fase",            source = "fase")
    @Mapping(target = "estado",          source = "estado")
    PartidoEntity toEntity(Partido domain);

    @Mapping(target = "torneo",               source = "torneo")
    @Mapping(target = "equipoLocal",          source = "equipoLocal")
    @Mapping(target = "equipoVisitante",      source = "equipoVisitante")
    @Mapping(target = "cancha",               source = "cancha")
    @Mapping(target = "arbitro",              source = "arbitro")
    @Mapping(target = "fechaHora",            source = "fechaHora")
    @Mapping(target = "golesLocal",           source = "golesLocal")
    @Mapping(target = "golesVisitante",       source = "golesVisitante")
    @Mapping(target = "fase",                 source = "fase")
    @Mapping(target = "estado",               source = "estado")
    @Mapping(target = "nombreEquipoLocal",    source = "equipoLocal.nombre")
    @Mapping(target = "nombreEquipoVisitante",source = "equipoVisitante.nombre")
    @Mapping(target = "fecha",                source = "fechaHora",
            dateFormat = "yyyy-MM-dd")
    @Mapping(target = "hora",                 ignore = true)
    @Mapping(target = "canchaLegacy",         ignore = true)
    @Mapping(target = "arbitroLegacy",        ignore = true)
    Partido toDomain(PartidoEntity entity);
}