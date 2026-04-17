package edu.dosw.proyect.controllers.mappers;

import edu.dosw.proyect.controllers.dtos.response.CreateTeamResponseDTO;
import edu.dosw.proyect.controllers.dtos.response.TeamResponseDTO;
import edu.dosw.proyect.core.models.Team;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;


@Mapper(componentModel = "spring")
public interface TeamMapper {

    @Mapping(target = "equipoId",           source = "id")
    @Mapping(target = "nombre",             source = "nombre")
    @Mapping(target = "escudoUrl",          source = "escudoUrl")
    @Mapping(target = "colorUniformeLocal", source = "colorUniformeLocal")
    @Mapping(target = "colorUniformeVisita",source = "colorUniformeVisita")
    @Mapping(target = "estadoInscripcion",  source = "estadoInscripcion")
    @Mapping(target = "torneoId",           source = "torneo.tournId")
    @Mapping(target = "capitanId",          source = "capitan.id")
    @Mapping(target = "capitanNombre",      source = "capitan.nombre")
    TeamResponseDTO toResponseDTO(Team team);

    List<TeamResponseDTO> toResponseDTOList(List<Team> teams);

    /**
     * Usado al crear un equipo — solo devuelve confirmación y notificaciones.
     */
    default CreateTeamResponseDTO toCrearEquipoResponseDTO(String mensaje,
                                                           List<String> notificaciones) {
        return CreateTeamResponseDTO.builder()
                .mensajeConfirmacion(mensaje)
                .notificacionesEnviadas(notificaciones)
                .build();
    }
}