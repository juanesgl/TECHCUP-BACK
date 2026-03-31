package edu.dosw.proyect.services;

import edu.dosw.proyect.controllers.dtos.request.RespuestaInvitacionRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.InvitacionResponseDTO;
import edu.dosw.proyect.controllers.mappers.InvitacionMapper;
import edu.dosw.proyect.core.exceptions.BusinessRuleException;
import edu.dosw.proyect.core.exceptions.ResourceNotFoundException;
import edu.dosw.proyect.core.models.Equipo;
import edu.dosw.proyect.core.models.Invitacion;
import edu.dosw.proyect.core.models.Jugador;
import edu.dosw.proyect.core.models.User;
import edu.dosw.proyect.core.models.enums.RespuestaInvitacion;
import edu.dosw.proyect.persistence.repository.*;
import edu.dosw.proyect.core.services.impl.InvitacionServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
    private InvitacionMapper invitacionMapper;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private InvitacionServiceImpl invitacionService;

    private Jugador buildJugador(Long id, boolean tieneEquipo) {
        User usuario = User.builder()
                .id(id).name("Juan").email("juan@mail.com")
                .password("pass").role("PLAYER").build();
        Jugador j = new Jugador();
        j.setId(id);
        j.setUsuario(usuario);
        j.setTieneEquipo(tieneEquipo);
        j.setNombre("Juan");
        return j;
    }

    private Invitacion buildInvitacion(Long id, Jugador jugador, String estado) {
        Equipo equipo = new Equipo();
        equipo.setNombre("Alpha FC");
        return Invitacion.builder()
                .id(id)
                .jugador(jugador)
                .equipo(equipo)
                .estado(estado)
                .build();
    }


    @Test
    void responderInvitacion_HappyPath_Aceptar() {
        Jugador jugador = buildJugador(1L, false);
        Invitacion invitacion = buildInvitacion(1L, jugador, "PENDIENTE");

        RespuestaInvitacionRequestDTO request = new RespuestaInvitacionRequestDTO();
        request.setRespuesta(RespuestaInvitacion.ACEPTAR);

        InvitacionResponseDTO expectedDTO = InvitacionResponseDTO.builder()
                .invitacionId(1L)
                .estadoActualizado("ACEPTADA")
                .build();

        when(jugadorRepository.findById(1L)).thenReturn(Optional.of(jugador));
        when(invitacionRepository.findById(1L)).thenReturn(Optional.of(invitacion));
        when(invitacionMapper.toResponseDTO(any(), any())).thenReturn(expectedDTO);
        when(userRepository.save(any())).thenReturn(jugador.getUsuario());

        InvitacionResponseDTO result =
                invitacionService.responderInvitacion(1L, 1L, request);

        assertNotNull(result);
        assertEquals("ACEPTADA", result.getEstadoActualizado());
        assertEquals("ACEPTADA", invitacion.getEstado());
        assertTrue(jugador.isTieneEquipo());
        verify(jugadorRepository, times(1)).save(jugador);
        verify(invitacionRepository, times(1)).save(invitacion);
    }

    @Test
    void responderInvitacion_HappyPath_Rechazar() {
        Jugador jugador = buildJugador(1L, false);
        Invitacion invitacion = buildInvitacion(1L, jugador, "PENDIENTE");

        RespuestaInvitacionRequestDTO request = new RespuestaInvitacionRequestDTO();
        request.setRespuesta(RespuestaInvitacion.RECHAZAR);

        InvitacionResponseDTO expectedDTO = InvitacionResponseDTO.builder()
                .invitacionId(1L)
                .estadoActualizado("RECHAZADA")
                .build();

        when(jugadorRepository.findById(1L)).thenReturn(Optional.of(jugador));
        when(invitacionRepository.findById(1L)).thenReturn(Optional.of(invitacion));
        when(invitacionMapper.toResponseDTO(any(), any())).thenReturn(expectedDTO);

        invitacionService.responderInvitacion(1L, 1L, request);

        assertEquals("RECHAZADA", invitacion.getEstado());
        verify(invitacionRepository, times(1)).save(invitacion);
    }


    @Test
    void responderInvitacion_RespuestaNula_LanzaBusinessRule() {
        RespuestaInvitacionRequestDTO request = new RespuestaInvitacionRequestDTO();
        request.setRespuesta(null);

        assertThrows(BusinessRuleException.class,
                () -> invitacionService.responderInvitacion(1L, 1L, request));
        verify(jugadorRepository, never()).findById(any());
    }

    @Test
    void responderInvitacion_JugadorNoEncontrado_LanzaNotFound() {
        RespuestaInvitacionRequestDTO request = new RespuestaInvitacionRequestDTO();
        request.setRespuesta(RespuestaInvitacion.ACEPTAR);

        when(jugadorRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> invitacionService.responderInvitacion(99L, 1L, request));
    }

    @Test
    void responderInvitacion_InvitacionNoEncontrada_LanzaNotFound() {
        Jugador jugador = buildJugador(1L, false);
        RespuestaInvitacionRequestDTO request = new RespuestaInvitacionRequestDTO();
        request.setRespuesta(RespuestaInvitacion.ACEPTAR);

        when(jugadorRepository.findById(1L)).thenReturn(Optional.of(jugador));
        when(invitacionRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> invitacionService.responderInvitacion(1L, 99L, request));
    }

    @Test
    void responderInvitacion_InvitacionNoPertenece_LanzaBusinessRule() {
        Jugador jugador = buildJugador(1L, false);
        Jugador otroJugador = buildJugador(2L, false);
        Invitacion invitacion = buildInvitacion(1L, otroJugador, "PENDIENTE");

        RespuestaInvitacionRequestDTO request = new RespuestaInvitacionRequestDTO();
        request.setRespuesta(RespuestaInvitacion.ACEPTAR);

        when(jugadorRepository.findById(1L)).thenReturn(Optional.of(jugador));
        when(invitacionRepository.findById(1L)).thenReturn(Optional.of(invitacion));

        assertThrows(BusinessRuleException.class,
                () -> invitacionService.responderInvitacion(1L, 1L, request));
    }

    @Test
    void responderInvitacion_InvitacionYaProcesada_LanzaBusinessRule() {
        Jugador jugador = buildJugador(1L, false);
        Invitacion invitacion = buildInvitacion(1L, jugador, "ACEPTADA");

        RespuestaInvitacionRequestDTO request = new RespuestaInvitacionRequestDTO();
        request.setRespuesta(RespuestaInvitacion.ACEPTAR);

        when(jugadorRepository.findById(1L)).thenReturn(Optional.of(jugador));
        when(invitacionRepository.findById(1L)).thenReturn(Optional.of(invitacion));

        assertThrows(BusinessRuleException.class,
                () -> invitacionService.responderInvitacion(1L, 1L, request));
    }

    @Test
    void responderInvitacion_JugadorYaTieneEquipo_LanzaBusinessRule() {
        Jugador jugador = buildJugador(1L, true);
        Invitacion invitacion = buildInvitacion(1L, jugador, "PENDIENTE");

        RespuestaInvitacionRequestDTO request = new RespuestaInvitacionRequestDTO();
        request.setRespuesta(RespuestaInvitacion.ACEPTAR);

        when(jugadorRepository.findById(1L)).thenReturn(Optional.of(jugador));
        when(invitacionRepository.findById(1L)).thenReturn(Optional.of(invitacion));

        assertThrows(BusinessRuleException.class,
                () -> invitacionService.responderInvitacion(1L, 1L, request));
        assertEquals("RECHAZADA", invitacion.getEstado());
    }
}