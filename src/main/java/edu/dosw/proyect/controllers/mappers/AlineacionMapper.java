package edu.dosw.proyect.controllers.mappers;

import edu.dosw.proyect.controllers.dtos.response.AlineacionRivalResponseDTO;
import edu.dosw.proyect.core.models.Alineacion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * MapStruct mapper.
 * Objeto origen: Alineacion (dominio) → AlineacionRivalResponseDTO.
 */
@Mapper(componentModel = "spring")
public interface AlineacionMapper {

    @Mapping(target = "partidoId",        source = "partido.id")
    @Mapping(target = "nombreEquipoRival",source = "equipo.nombre")
    @Mapping(target = "formacion",        source = "formacion")
    @Mapping(target = "titulares",        source = "titulares")
    @Mapping(target = "reservas",         source = "reservas")
    @Mapping(target = "mensaje",          constant = "Alineación del equipo rival disponible")
    AlineacionRivalResponseDTO toRivalResponseDTO(Alineacion alineacion);

    /**
     * Versión con partidoId explícito cuando no viene del objeto Alineacion.
     * Se usa desde AlineacionServiceImpl.
     */
    default AlineacionRivalResponseDTO toRivalResponseDTO(Alineacion alineacion, Long partidoId) {
        AlineacionRivalResponseDTO dto = toRivalResponseDTO(alineacion);
        dto.setPartidoId(partidoId);
        return dto;
    }
}