package edu.dosw.proyect.services;

import edu.dosw.proyect.controllers.dtos.request.AnswerInvitationRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.InvitationResponseDTO;
import edu.dosw.proyect.controllers.mappers.InvitacionMapper;
import edu.dosw.proyect.core.exceptions.BusinessRuleException;
import edu.dosw.proyect.core.exceptions.ResourceNotFoundException;
import edu.dosw.proyect.core.models.Invitation;
import edu.dosw.proyect.core.models.enums.InvitationResponse;
import edu.dosw.proyect.core.services.impl.InvitationServiceImpl;
import edu.dosw.proyect.persistence.entity.InvitationEntity;
import edu.dosw.proyect.persistence.entity.PlayerEntity;
import edu.dosw.proyect.persistence.entity.TeamEntity;
import edu.dosw.proyect.persistence.mapper.InvitationPersistenceMapper;
import edu.dosw.proyect.persistence.mapper.PlayerPersistenceMapper;
import edu.dosw.proyect.persistence.repository.TeamPlayerRepository;
import edu.dosw.proyect.persistence.repository.InvitationRepository;
import edu.dosw.proyect.persistence.repository.PlayerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InvitationServiceImplTest {

    @Mock
    private InvitationRepository invitationRepository;

    @Mock
    private PlayerRepository playerRepository;
    @Mock
    private TeamPlayerRepository teamPlayerRepository;

    @Mock
    private InvitacionMapper invitacionMapper;

    @Mock
    private PlayerPersistenceMapper playerPersistenceMapper;

    @Mock
    private InvitationPersistenceMapper invitationPersistenceMapper;

    @InjectMocks
    private InvitationServiceImpl invitacionService;

    private PlayerEntity buildJugador(Long id, boolean tieneEquipo) {
        PlayerEntity j = new PlayerEntity();
        j.setId(id);
        j.setTieneEquipo(tieneEquipo);
        return j;
    }

    private InvitationEntity buildInvitacion(Long id, PlayerEntity jugador, String estado) {
        TeamEntity equipo = new TeamEntity();
        equipo.setNombre("Alpha FC");
        return InvitationEntity.builder()
                .id(id)
                .jugador(jugador)
                .equipo(equipo)
                .estado(estado)
                .build();
    }

    @Test
    void responderInvitacion_HappyPath_Aceptar() {
        PlayerEntity jugador = buildJugador(1L, false);
        InvitationEntity invitacion = buildInvitacion(1L, jugador, "PENDIENTE");
        AnswerInvitationRequestDTO request = new AnswerInvitationRequestDTO();
        request.setRespuesta(InvitationResponse.ACEPTAR);

        InvitationResponseDTO dto = InvitationResponseDTO.builder()
                .invitacionId(1L).estadoActualizado("ACEPTADA").build();
        Invitation invitationDomain = Invitation.builder()
                .id(1L).estado("ACEPTADA").build();

        when(playerRepository.findById(1L)).thenReturn(Optional.of(jugador));
        when(invitationRepository.findById(1L)).thenReturn(Optional.of(invitacion));
        when(teamPlayerRepository.findByEquipoId(any())).thenReturn(List.of());
        when(invitationPersistenceMapper.toDomain(any())).thenReturn(invitationDomain);
        when(invitacionMapper.toResponseDTO(any(), any())).thenReturn(dto);

        InvitationResponseDTO result =
                invitacionService.responderInvitacion(1L, 1L, request);

        assertNotNull(result);
        assertEquals("ACEPTADA", invitacion.getEstado());
        verify(playerRepository, times(1)).save(jugador);
    }

    @Test
    void responderInvitacion_HappyPath_Rechazar() {
        PlayerEntity jugador = buildJugador(1L, false);
        InvitationEntity invitacion = buildInvitacion(1L, jugador, "PENDIENTE");
        AnswerInvitationRequestDTO request = new AnswerInvitationRequestDTO();
        request.setRespuesta(InvitationResponse.RECHAZAR);

        Invitation invitationDomain = Invitation.builder()
                .id(1L).estado("RECHAZADA").build();
        InvitationResponseDTO dto = InvitationResponseDTO.builder()
                .invitacionId(1L).estadoActualizado("RECHAZADA").build();

        when(playerRepository.findById(1L)).thenReturn(Optional.of(jugador));
        when(invitationRepository.findById(1L)).thenReturn(Optional.of(invitacion));
        when(invitationPersistenceMapper.toDomain(any())).thenReturn(invitationDomain);
        when(invitacionMapper.toResponseDTO(any(), any())).thenReturn(dto);

        invitacionService.responderInvitacion(1L, 1L, request);

        assertEquals("RECHAZADA", invitacion.getEstado());
    }

    @Test
    void responderInvitacion_RespuestaNula_LanzaException() {
        AnswerInvitationRequestDTO request = new AnswerInvitationRequestDTO();
        request.setRespuesta(null);
        assertThrows(BusinessRuleException.class,
                () -> invitacionService.responderInvitacion(1L, 1L, request));
    }

    @Test
    void responderInvitacion_JugadorNoEncontrado_LanzaException() {
        AnswerInvitationRequestDTO request = new AnswerInvitationRequestDTO();
        request.setRespuesta(InvitationResponse.ACEPTAR);
        when(playerRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,
                () -> invitacionService.responderInvitacion(99L, 1L, request));
    }

    @Test
    void responderInvitacion_InvitacionNoEncontrada_LanzaException() {
        PlayerEntity jugador = buildJugador(1L, false);
        AnswerInvitationRequestDTO request = new AnswerInvitationRequestDTO();
        request.setRespuesta(InvitationResponse.ACEPTAR);
        when(playerRepository.findById(1L)).thenReturn(Optional.of(jugador));
        when(invitationRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,
                () -> invitacionService.responderInvitacion(1L, 99L, request));
    }

    @Test
    void responderInvitacion_NoPertenece_LanzaException() {
        PlayerEntity jugador = buildJugador(1L, false);
        PlayerEntity otro = buildJugador(2L, false);
        InvitationEntity invitacion = buildInvitacion(1L, otro, "PENDIENTE");
        AnswerInvitationRequestDTO request = new AnswerInvitationRequestDTO();
        request.setRespuesta(InvitationResponse.ACEPTAR);
        when(playerRepository.findById(1L)).thenReturn(Optional.of(jugador));
        when(invitationRepository.findById(1L)).thenReturn(Optional.of(invitacion));
        assertThrows(BusinessRuleException.class,
                () -> invitacionService.responderInvitacion(1L, 1L, request));
    }

    @Test
    void responderInvitacion_YaProcesada_LanzaException() {
        PlayerEntity jugador = buildJugador(1L, false);
        InvitationEntity invitacion = buildInvitacion(1L, jugador, "ACEPTADA");
        AnswerInvitationRequestDTO request = new AnswerInvitationRequestDTO();
        request.setRespuesta(InvitationResponse.ACEPTAR);
        when(playerRepository.findById(1L)).thenReturn(Optional.of(jugador));
        when(invitationRepository.findById(1L)).thenReturn(Optional.of(invitacion));
        assertThrows(BusinessRuleException.class,
                () -> invitacionService.responderInvitacion(1L, 1L, request));
    }

    @Test
    void responderInvitacion_YaTieneEquipo_LanzaException() {
        PlayerEntity jugador = buildJugador(1L, true);
        InvitationEntity invitacion = buildInvitacion(1L, jugador, "PENDIENTE");
        AnswerInvitationRequestDTO request = new AnswerInvitationRequestDTO();
        request.setRespuesta(InvitationResponse.ACEPTAR);
        when(playerRepository.findById(1L)).thenReturn(Optional.of(jugador));
        when(invitationRepository.findById(1L)).thenReturn(Optional.of(invitacion));
        assertThrows(BusinessRuleException.class,
                () -> invitacionService.responderInvitacion(1L, 1L, request));
    }
}