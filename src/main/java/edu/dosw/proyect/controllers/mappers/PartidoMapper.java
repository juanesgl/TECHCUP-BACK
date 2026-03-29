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
                .equipoLocal(partido.getEquipoLocal() != null ? partido.getEquipoLocal().getNombre() : "TBD")
                .equipoVisitante(
                        partido.getEquipoVisitante() != null ? partido.getEquipoVisitante().getNombre() : "TBD")
                .fecha(partido.getFechaHora() != null ? partido.getFechaHora().toLocalDate() : null)
                .hora(partido.getFechaHora() != null ? partido.getFechaHora().toLocalTime() : null)
                .cancha(partido.getCancha() != null ? partido.getCancha().getNombre() : "TBD")
                .arbitro(partido.getArbitro() != null ? partido.getArbitro().getName() : "TBD")
                .estado(partido.getEstado() != null ? partido.getEstado().name() : null)
                .tournamentId(partido.getTorneo() != null ? partido.getTorneo().getTournId() : null)
                .build();
    }

    public List<PartidoResponseDTO> toResponseDTOList(List<Partido> partidos) {
        return partidos.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }
}

