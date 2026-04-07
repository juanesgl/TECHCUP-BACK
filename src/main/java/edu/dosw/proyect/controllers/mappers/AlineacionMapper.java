package edu.dosw.proyect.controllers.mappers;

import edu.dosw.proyect.controllers.dtos.response.AlineacionRivalResponseDTO;
import edu.dosw.proyect.core.models.Alineacion;
import org.springframework.stereotype.Component;

@Component
public class AlineacionMapper {

    public AlineacionRivalResponseDTO toRivalResponseDTO(Alineacion alineacion, Long partidoId) {
        return AlineacionRivalResponseDTO.builder()
                .partidoId(partidoId)
                .nombreEquipoRival(alineacion.getNombreEquipo())
                .formacion(alineacion.getFormacion())
                .titulares(alineacion.getTitulares())
                .reservas(alineacion.getReservas())
                .mensaje("Alineación del equipo rival disponible")
                .build();
    }
}