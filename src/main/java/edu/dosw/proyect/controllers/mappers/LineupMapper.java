package edu.dosw.proyect.controllers.mappers;

import edu.dosw.proyect.controllers.dtos.response.OpponentLineupResponseDTO;
import edu.dosw.proyect.core.models.Lineup;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface LineupMapper {

    @Mapping(target = "partidoId",        source = "partido.id")
    @Mapping(target = "nombreEquipoRival",source = "team.nombre")
    @Mapping(target = "formacion",        source = "formacion")
    @Mapping(target = "titulares",        source = "titulares")
    @Mapping(target = "reservas",         source = "reservas")
    @Mapping(target = "mensaje",          constant = "Alineación del equipo rival disponible")
    OpponentLineupResponseDTO toRivalResponseDTO(Lineup lineup);

    /**
     * Versión con partidoId explícito cuando no viene del objeto Alineacion.
     * Se usa desde AlineacionServiceImpl.
     */
    default OpponentLineupResponseDTO toRivalResponseDTO(Lineup lineup, Long partidoId) {
        OpponentLineupResponseDTO dto = toRivalResponseDTO(lineup);
        dto.setPartidoId(partidoId);
        return dto;
    }
}