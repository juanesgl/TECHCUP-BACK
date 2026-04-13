package edu.dosw.proyect.services;

import edu.dosw.proyect.controllers.dtos.request.RespuestaInvitacionRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.InvitacionResponseDTO;
import edu.dosw.proyect.controllers.mappers.InvitacionMapper;
import edu.dosw.proyect.core.exceptions.BusinessRuleException;
import edu.dosw.proyect.core.exceptions.ResourceNotFoundException;
import edu.dosw.proyect.core.models.Invitacion;
import edu.dosw.proyect.core.models.enums.RespuestaInvitacion;
import edu.dosw.proyect.core.services.impl.InvitacionServiceImpl;
import edu.dosw.proyect.persistence.entity.EquipoEntity;
import edu.dosw.proyect.persistence.entity.InvitacionEntity;
import edu.dosw.proyect.persistence.entity.JugadorEntity;
import edu.dosw.proyect.persistence.mapper.InvitacionPersistenceMapper;
import edu.dosw.proyect.persistence.mapper.JugadorPersistenceMapper;
import edu.dosw.proyect.persistence.repository.EquipoJugadorRepository;
import edu.dosw.proyect.persistence.repository.InvitacionRepository;
import edu.dosw.proyect.persistence.repository.JugadorRepository;
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
class InvitacionServiceImplTest {

    @Mock
    private InvitacionRepository invitacionRepository;

    @Mock
    private JugadorRepository jugadorRepository;
    @Mock
    private EquipoJugadorRepository equipoJugadorRepository;

    @Mock
    private InvitacionMapper invitacionMapper;

    @Mock
    private JugadorPersistenceMapper jugadorPersistenceMapper;

    @Mock
    private InvitacionPersistenceMapper invitacionPersistenceMapper;

    @InjectMocks
    private InvitacionServiceImpl invitacionService;

    private JugadorEntity buildJugador(Long id, boolean tieneEquipo) {
        JugadorEntity j = new JugadorEntity();
        j.setId(id);
        j.setTieneEquipo(tieneEquipo);
        return j;
    }

    private InvitacionEntity buildInvitacion(Long id, JugadorEntity jugador, String estado) {
        EquipoEntity equipo = new EquipoEntity();
        equipo.setNombre("Alpha FC");
        return InvitacionEntity.builder()
                .id(id)
                .jugador(jugador)
                .equipo(equipo)
                .estado(estado)
                .build();
    }

    @Test
    void responderInvitacion_HappyPath_Aceptar() {
        JugadorEntity jugador = buildJugador(1L, false);
        InvitacionEntity invitacion = buildInvitacion(1L, jugador, "PENDIENTE");
        RespuestaInvitacionRequestDTO request = new RespuestaInvitacionRequestDTO();
        request.setRespuesta(RespuestaInvitacion.ACEPTAR);

        InvitacionResponseDTO dto = InvitacionResponseDTO.builder()
                .invitacionId(1L).estadoActualizado("ACEPTADA").build();
        Invitacion invitacionDomain = Invitacion.builder()
                .id(1L).estado("ACEPTADA").build();

        when(jugadorRepository.findById(1L)).thenReturn(Optional.of(jugador));
        when(invitacionRepository.findById(1L)).thenReturn(Optional.of(invitacion));
        when(equipoJugadorRepository.findByEquipoId(any())).thenReturn(List.of());
        when(invitacionPersistenceMapper.toDomain(any())).thenReturn(invitacionDomain);
        when(invitacionMapper.toResponseDTO(any(), any())).thenReturn(dto);

        InvitacionResponseDTO result =
                invitacionService.responderInvitacion(1L, 1L, request);

        assertNotNull(result);
        assertEquals("ACEPTADA", invitacion.getEstado());
        verify(jugadorRepository, times(1)).save(jugador);
    }

    @Test
    void responderInvitacion_HappyPath_Rechazar() {
        JugadorEntity jugador = buildJugador(1L, false);
        InvitacionEntity invitacion = buildInvitacion(1L, jugador, "PENDIENTE");
        RespuestaInvitacionRequestDTO request = new RespuestaInvitacionRequestDTO();
        request.setRespuesta(RespuestaInvitacion.RECHAZAR);

        Invitacion invitacionDomain = Invitacion.builder()
                .id(1L).estado("RECHAZADA").build();
        InvitacionResponseDTO dto = InvitacionResponseDTO.builder()
                .invitacionId(1L).estadoActualizado("RECHAZADA").build();

        when(jugadorRepository.findById(1L)).thenReturn(Optional.of(jugador));
        when(invitacionRepository.findById(1L)).thenReturn(Optional.of(invitacion));
        when(invitacionPersistenceMapper.toDomain(any())).thenReturn(invitacionDomain);
        when(invitacionMapper.toResponseDTO(any(), any())).thenReturn(dto);

        invitacionService.responderInvitacion(1L, 1L, request);

        assertEquals("RECHAZADA", invitacion.getEstado());
    }

    @Test
    void responderInvitacion_RespuestaNula_LanzaException() {
        RespuestaInvitacionRequestDTO request = new RespuestaInvitacionRequestDTO();
        request.setRespuesta(null);
        assertThrows(BusinessRuleException.class,
                () -> invitacionService.responderInvitacion(1L, 1L, request));
    }

    @Test
    void responderInvitacion_JugadorNoEncontrado_LanzaException() {
        RespuestaInvitacionRequestDTO request = new RespuestaInvitacionRequestDTO();
        request.setRespuesta(RespuestaInvitacion.ACEPTAR);
        when(jugadorRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,
                () -> invitacionService.responderInvitacion(99L, 1L, request));
    }

    @Test
    void responderInvitacion_InvitacionNoEncontrada_LanzaException() {
        JugadorEntity jugador = buildJugador(1L, false);
        RespuestaInvitacionRequestDTO request = new RespuestaInvitacionRequestDTO();
        request.setRespuesta(RespuestaInvitacion.ACEPTAR);
        when(jugadorRepository.findById(1L)).thenReturn(Optional.of(jugador));
        when(invitacionRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,
                () -> invitacionService.responderInvitacion(1L, 99L, request));
    }

    @Test
    void responderInvitacion_NoPertenece_LanzaException() {
        JugadorEntity jugador = buildJugador(1L, false);
        JugadorEntity otro = buildJugador(2L, false);
        InvitacionEntity invitacion = buildInvitacion(1L, otro, "PENDIENTE");
        RespuestaInvitacionRequestDTO request = new RespuestaInvitacionRequestDTO();
        request.setRespuesta(RespuestaInvitacion.ACEPTAR);
        when(jugadorRepository.findById(1L)).thenReturn(Optional.of(jugador));
        when(invitacionRepository.findById(1L)).thenReturn(Optional.of(invitacion));
        assertThrows(BusinessRuleException.class,
                () -> invitacionService.responderInvitacion(1L, 1L, request));
    }

    @Test
    void responderInvitacion_YaProcesada_LanzaException() {
        JugadorEntity jugador = buildJugador(1L, false);
        InvitacionEntity invitacion = buildInvitacion(1L, jugador, "ACEPTADA");
        RespuestaInvitacionRequestDTO request = new RespuestaInvitacionRequestDTO();
        request.setRespuesta(RespuestaInvitacion.ACEPTAR);
        when(jugadorRepository.findById(1L)).thenReturn(Optional.of(jugador));
        when(invitacionRepository.findById(1L)).thenReturn(Optional.of(invitacion));
        assertThrows(BusinessRuleException.class,
                () -> invitacionService.responderInvitacion(1L, 1L, request));
    }

    @Test
    void responderInvitacion_YaTieneEquipo_LanzaException() {
        JugadorEntity jugador = buildJugador(1L, true);
        InvitacionEntity invitacion = buildInvitacion(1L, jugador, "PENDIENTE");
        RespuestaInvitacionRequestDTO request = new RespuestaInvitacionRequestDTO();
        request.setRespuesta(RespuestaInvitacion.ACEPTAR);
        when(jugadorRepository.findById(1L)).thenReturn(Optional.of(jugador));
        when(invitacionRepository.findById(1L)).thenReturn(Optional.of(invitacion));
        assertThrows(BusinessRuleException.class,
                () -> invitacionService.responderInvitacion(1L, 1L, request));
    }
}