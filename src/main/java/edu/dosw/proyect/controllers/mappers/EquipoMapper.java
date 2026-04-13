package edu.dosw.proyect.controllers.mappers;

import edu.dosw.proyect.controllers.dtos.response.CrearEquipoResponseDTO;
import edu.dosw.proyect.controllers.dtos.response.EquipoResponseDTO;
import edu.dosw.proyect.core.models.Equipo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * MapStruct mapper.
 * Objeto origen: Equipo (dominio) → EquipoResponseDTO / CrearEquipoResponseDTO.
 */
@Mapper(componentModel = "spring")
public interface EquipoMapper {

    @Mapping(target = "equipoId",           source = "id")
    @Mapping(target = "nombre",             source = "nombre")
    @Mapping(target = "escudoUrl",          source = "escudoUrl")
    @Mapping(target = "colorUniformeLocal", source = "colorUniformeLocal")
    @Mapping(target = "colorUniformeVisita",source = "colorUniformeVisita")
    @Mapping(target = "estadoInscripcion",  source = "estadoInscripcion")
    @Mapping(target = "torneoId",           source = "torneo.tournId")
    @Mapping(target = "capitanId",          source = "capitan.id")
    @Mapping(target = "capitanNombre",      source = "capitan.nombre")
    EquipoResponseDTO toResponseDTO(Equipo equipo);

    List<EquipoResponseDTO> toResponseDTOList(List<Equipo> equipos);

    /**
     * Usado al crear un equipo — solo devuelve confirmación y notificaciones.
     */
    default CrearEquipoResponseDTO toCrearEquipoResponseDTO(String mensaje,
                                                            List<String> notificaciones) {
        return CrearEquipoResponseDTO.builder()
                .mensajeConfirmacion(mensaje)
                .notificacionesEnviadas(notificaciones)
                .build();
    }
}