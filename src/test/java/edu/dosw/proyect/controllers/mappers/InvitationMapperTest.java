package edu.dosw.proyect.controllers.mappers;

import edu.dosw.proyect.controllers.dtos.response.InvitationResponseDTO;
import edu.dosw.proyect.core.models.Invitation;
import edu.dosw.proyect.core.models.Player;
import edu.dosw.proyect.core.models.Team;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

class InvitationMapperTest {

    private final InvitacionMapper mapper = Mappers.getMapper(InvitacionMapper.class);

    @Test
    void toResponseDTO_HappyPath_MapeaCorrectamente() {
        Team team = new Team();
        team.setNombre("Alpha FC");

        Player jugador = new Player();
        jugador.setNombre("Juan");

        Invitation invitation = Invitation.builder()
                .id(1L)
                .team(team)
                .jugador(jugador)
                .estado("ACEPTADA")
                .build();

        InvitationResponseDTO dto = mapper.toResponseDTO(
                invitation, "Juan ha aceptado la invitacion");

        assertNotNull(dto);
        assertEquals(1L, dto.getInvitacionId());
        assertEquals("Juan ha aceptado la invitacion", dto.getMensajeCapitan());
        assertEquals("ACEPTADA", dto.getEstadoActualizado());
    }

    @Test
    void toResponseDTO_EstadoRechazada_MapeaCorrectamente() {
        Invitation invitation = Invitation.builder()
                .id(2L)
                .estado("RECHAZADA")
                .build();

        InvitationResponseDTO dto = mapper.toResponseDTO(
                invitation, "Juan ha rechazado la invitacion");

        assertEquals(2L, dto.getInvitacionId());
        assertEquals("RECHAZADA", dto.getEstadoActualizado());
    }

    @Test
    void toResponseDTO_SinMensajeExplicito_MensajeNulo() {
        Invitation invitation = Invitation.builder()
                .id(3L)
                .estado("PENDIENTE")
                .build();

        InvitationResponseDTO dto = mapper.toResponseDTO(invitation);

        assertEquals(3L, dto.getInvitacionId());
        assertEquals("PENDIENTE", dto.getEstadoActualizado());
        assertNull(dto.getMensajeCapitan());
    }
}