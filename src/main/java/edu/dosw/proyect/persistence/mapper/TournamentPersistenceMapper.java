package edu.dosw.proyect.persistence.mapper;

import edu.dosw.proyect.core.models.Tournament;
import edu.dosw.proyect.persistence.entity.TournamentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * MapStruct mapper.
 * Objeto origen: TournamentEntity (persistencia) → Tournament (dominio).
 * Nota: los campos "canchas", "regulation", "importantDates", etc.
 * solo existen en Tournament (dominio), no en TournamentEntity.
 */
@Mapper(componentModel = "spring", uses = { UserPersistenceMapper.class })
public interface TournamentPersistenceMapper {

    @Mapping(target = "tournId",                 source = "tournId")
    @Mapping(target = "name",                    source = "name")
    @Mapping(target = "startDate",               source = "startDate")
    @Mapping(target = "endDate",                 source = "endDate")
    @Mapping(target = "maxTeams",                source = "maxTeams")
    @Mapping(target = "costPerTeam",             source = "costPerTeam")
    @Mapping(target = "status",                  source = "status")
    @Mapping(target = "organizador",             source = "organizador")
    @Mapping(target = "registrationCloseDate",   source = "registrationCloseDate")
    TournamentEntity toEntity(Tournament domain);

    @Mapping(target = "tournId",                 source = "tournId")
    @Mapping(target = "name",                    source = "name")
    @Mapping(target = "startDate",               source = "startDate")
    @Mapping(target = "endDate",                 source = "endDate")
    @Mapping(target = "maxTeams",                source = "maxTeams")
    @Mapping(target = "costPerTeam",             source = "costPerTeam")
    @Mapping(target = "status",                  source = "status")
    @Mapping(target = "organizador",             source = "organizador")
    @Mapping(target = "registrationCloseDate",   source = "registrationCloseDate")
    @Mapping(target = "organizerId",             ignore = true)
    @Mapping(target = "importantDates",          ignore = true)
    @Mapping(target = "matchSchedules",          ignore = true)
    @Mapping(target = "sanctions",               ignore = true)
    @Mapping(target = "regulation",              ignore = true)
    @Mapping(target = "canchas",                 ignore = true)
    Tournament toDomain(TournamentEntity entity);
}