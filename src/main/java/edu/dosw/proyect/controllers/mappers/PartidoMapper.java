package edu.dosw.proyect.controllers.mappers;

import edu.dosw.proyect.controllers.dtos.response.PartidoResponseDTO;
import edu.dosw.proyect.core.models.Partido;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * MapStruct mapper.
 * Objeto origen: Partido (dominio) → PartidoResponseDTO.
 * Convierte LocalDateTime a LocalDate (fecha) y LocalTime (hora) usando métodos named.
 */
@Mapper(componentModel = "spring")
public interface PartidoMapper {

    @Mapping(target = "equipoLocal",     source = "equipoLocal.nombre",
            defaultValue = "TBD")
    @Mapping(target = "equipoVisitante", source = "equipoVisitante.nombre",
            defaultValue = "TBD")
    @Mapping(target = "fecha",           source = "fechaHora",
            qualifiedByName = "toLocalDate")
    @Mapping(target = "hora",            source = "fechaHora",
            qualifiedByName = "toLocalTime")
    @Mapping(target = "cancha",          source = "cancha.nombre",
            defaultValue = "TBD")
    @Mapping(target = "arbitro",         source = "arbitro.name",
            defaultValue = "TBD")
    @Mapping(target = "estado",          source = "estado")
    @Mapping(target = "tournamentId",    source = "torneo.tournId")
    PartidoResponseDTO toResponseDTO(Partido partido);

    List<PartidoResponseDTO> toResponseDTOList(List<Partido> partidos);

    @Named("toLocalDate")
    default LocalDate toLocalDate(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.toLocalDate() : null;
    }

    @Named("toLocalTime")
    default LocalTime toLocalTime(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.toLocalTime() : null;
    }
}