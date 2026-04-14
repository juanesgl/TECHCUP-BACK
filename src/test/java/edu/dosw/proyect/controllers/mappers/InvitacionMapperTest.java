package edu.dosw.proyect.controllers.mappers;

import edu.dosw.proyect.controllers.dtos.response.InvitacionResponseDTO;
import edu.dosw.proyect.core.models.Equipo;
import edu.dosw.proyect.core.models.Invitacion;
import edu.dosw.proyect.core.models.Jugador;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

class InvitacionMapperTest {

    private final InvitacionMapper mapper = Mappers.getMapper(InvitacionMapper.class);

    @Test
    void toResponseDTO_HappyPath_MapeaCorrectamente() {
        Equipo equipo = new Equipo();
        equipo.setNombre("Alpha FC");

        Jugador jugador = new Jugador();
        jugador.setNombre("Juan");

        Invitacion invitacion = Invitacion.builder()
                .id(1L)
                .equipo(equipo)
                .jugador(jugador)
                .estado("ACEPTADA")
                .build();

        InvitacionResponseDTO dto = mapper.toResponseDTO(
                invitacion, "Juan ha aceptado la invitacion");

        assertNotNull(dto);
        assertEquals(1L, dto.getInvitacionId());
        assertEquals("Juan ha aceptado la invitacion", dto.getMensajeCapitan());
        assertEquals("ACEPTADA", dto.getEstadoActualizado());
    }

    @Test
    void toResponseDTO_EstadoRechazada_MapeaCorrectamente() {
        Invitacion invitacion = Invitacion.builder()
                .id(2L)
                .estado("RECHAZADA")
                .build();

        InvitacionResponseDTO dto = mapper.toResponseDTO(
                invitacion, "Juan ha rechazado la invitacion");

        assertEquals(2L, dto.getInvitacionId());
        assertEquals("RECHAZADA", dto.getEstadoActualizado());
    }
}