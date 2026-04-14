package edu.dosw.proyect.controllers.mappers;

import edu.dosw.proyect.controllers.dtos.response.CrearEquipoResponseDTO;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EquipoMapperTest {

    private final EquipoMapper mapper = Mappers.getMapper(EquipoMapper.class);

    @Test
    void toCrearEquipoResponseDTO_HappyPath_MapeaCorrectamente() {
        String mensaje = "Equipo creado exitosamente";
        List<String> notificaciones = List.of(
                "Invitacion enviada a Jugador 1",
                "Invitacion enviada a Jugador 2"
        );

        CrearEquipoResponseDTO dto = mapper.toCrearEquipoResponseDTO(mensaje, notificaciones);

        assertNotNull(dto);
        assertEquals(mensaje, dto.getMensajeConfirmacion());
        assertEquals(2, dto.getNotificacionesEnviadas().size());
    }

    @Test
    void toCrearEquipoResponseDTO_ListaVacia_RetornaVacia() {
        CrearEquipoResponseDTO dto = mapper.toCrearEquipoResponseDTO(
                "Equipo creado", List.of());

        assertNotNull(dto);
        assertTrue(dto.getNotificacionesEnviadas().isEmpty());
    }
}