package edu.dosw.proyect.controllers.mappers;

import edu.dosw.proyect.controllers.dtos.response.PartidoResponseDTO;
import edu.dosw.proyect.core.models.Partido;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PartidoMapper {
    public PartidoResponseDTO toResponseDTO(Partido partido) {
        return PartidoResponseDTO.builder()
                .id(partido.getId())
                .equipoLocal(partido.getNombreEquipoLocal())
                .equipoVisitante(partido.getNombreEquipoVisitante())
                .fecha(partido.getFecha())
                .hora(partido.getHora())
                .cancha(partido.getCancha())
                .arbitro(partido.getArbitro())
                .estado(partido.getEstado().name())
                .tournamentId(partido.getTorneo() != null ? partido.getTorneo().getTournId() : null)
                .build();
    }

    public List<PartidoResponseDTO> toResponseDTOList(List<Partido> partidos) {
        return partidos.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }
}
