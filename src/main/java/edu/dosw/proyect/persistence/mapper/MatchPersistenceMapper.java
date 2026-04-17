package edu.dosw.proyect.persistence.mapper;

import edu.dosw.proyect.core.models.Partido;
import edu.dosw.proyect.persistence.entity.MatchEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {
                TournamentPersistenceMapper.class,
                TeamPersistenceMapper.class,
                SoccerFieldPersistenceMapper.class,
                UserPersistenceMapper.class
})
public interface MatchPersistenceMapper {

        @Mapping(target = "torneo", source = "torneo")
        @Mapping(target = "equipoLocal", source = "teamLocal")
        @Mapping(target = "equipoVisitante", source = "teamVisitante")
        @Mapping(target = "cancha", source = "soccerField")
        @Mapping(target = "arbitro", source = "arbitro")
        @Mapping(target = "fechaHora", source = "fechaHora")
        @Mapping(target = "golesLocal", source = "golesLocal")
        @Mapping(target = "golesVisitante", source = "golesVisitante")
        @Mapping(target = "fase", source = "fase")
        @Mapping(target = "estado", source = "estado")
        MatchEntity toEntity(Partido domain);

        @Mapping(target = "torneo", source = "torneo")
        @Mapping(target = "teamLocal", source = "equipoLocal")
        @Mapping(target = "teamVisitante", source = "equipoVisitante")
        @Mapping(target = "soccerField", source = "cancha")
        @Mapping(target = "arbitro", source = "arbitro")
        @Mapping(target = "fechaHora", source = "fechaHora")
        @Mapping(target = "golesLocal", source = "golesLocal")
        @Mapping(target = "golesVisitante", source = "golesVisitante")
        @Mapping(target = "fase", source = "fase")
        @Mapping(target = "estado", source = "estado")
        @Mapping(target = "nombreEquipoLocal", source = "equipoLocal.nombre")
        @Mapping(target = "nombreEquipoVisitante", source = "equipoVisitante.nombre")
        @Mapping(target = "fecha", source = "fechaHora", dateFormat = "yyyy-MM-dd")
        @Mapping(target = "hora", ignore = true)
        @Mapping(target = "canchaLegacy", ignore = true)
        @Mapping(target = "arbitroLegacy", ignore = true)
        Partido toDomain(MatchEntity entity);
}