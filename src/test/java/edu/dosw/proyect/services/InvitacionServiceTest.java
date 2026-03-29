package edu.dosw.proyect.services;

import edu.dosw.proyect.controllers.dtos.request.RespuestaInvitacionRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.InvitacionResponseDTO;
import edu.dosw.proyect.core.exceptions.BusinessRuleException;
import edu.dosw.proyect.controllers.mappers.InvitacionMapper;
import edu.dosw.proyect.core.models.User;
import edu.dosw.proyect.core.models.Equipo;
import edu.dosw.proyect.core.models.Invitacion;
import edu.dosw.proyect.core.models.Jugador;
import edu.dosw.proyect.core.models.enums.RespuestaInvitacion;
import edu.dosw.proyect.core.services.impl.InvitacionServiceImpl;
import edu.dosw.proyect.core.repositories.InvitacionRepository;
import edu.dosw.proyect.core.repositories.JugadorRepository;
import edu.dosw.proyect.core.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InvitacionServiceTest {

    @Mock
    private InvitacionRepository invitacionRepository;

    @Mock
    private JugadorRepository jugadorRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private InvitacionMapper invitacionMapper;

    @InjectMocks
    private InvitacionServiceImpl invitacionService;

    private Jugador jugador;
    private Invitacion invitacion;
    private RespuestaInvitacionRequestDTO request;

    @BeforeEach
    void setUp() {
        jugador = mock(Jugador.class);
        lenient().when(jugador.getId()).thenReturn(1L);
        lenient().when(jugador.getNombre()).thenReturn("Test Jugador");

        User user = User.builder()
                .id(1L)
                .name("Test Jugador")
                .email("test@test.com")
                .password("pass")
                .role("PLAYER")
                .build();

        lenient().when(jugador.getUsuario()).thenReturn(user);

        Equipo equipo = Equipo.builder().id(1L).nombre("Test FC").build();

        invitacion = Invitacion.builder()
                .id(1L)
                .jugador(jugador)
                .equipo(equipo)
                .estado("PENDIENTE")
                .build();
        request = new RespuestaInvitacionRequestDTO();
    }

    @Test
    void debeAceptarInvitacionExitosamente() {
        request.setRespuesta(RespuestaInvitacion.ACEPTAR);
        when(jugadorRepository.findById(1L)).thenReturn(Optional.of(jugador));
        when(invitacionRepository.findById(1L)).thenReturn(Optional.of(invitacion));
        when(invitacionMapper.toResponseDTO(any(), anyString()))
                .thenReturn(InvitacionResponseDTO.builder().estadoActualizado("ACEPTADA").build());

        InvitacionResponseDTO response = invitacionService.responderInvitacion(1L, 1L, request);

        assertEquals("ACEPTADA", response.getEstadoActualizado());
        assertEquals("ACEPTADA", invitacion.getEstado());

        verify(invitacionRepository).save(invitacion);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void debeRechazarInvitacionExitosamente() {
        request.setRespuesta(RespuestaInvitacion.RECHAZAR);
        when(jugadorRepository.findById(1L)).thenReturn(Optional.of(jugador));
        when(invitacionRepository.findById(1L)).thenReturn(Optional.of(invitacion));
        when(invitacionMapper.toResponseDTO(any(), anyString()))
                .thenReturn(InvitacionResponseDTO.builder().estadoActualizado("RECHAZADA").build());

        InvitacionResponseDTO response = invitacionService.responderInvitacion(1L, 1L, request);

        assertEquals("RECHAZADA", response.getEstadoActualizado());
        assertEquals("RECHAZADA", invitacion.getEstado());

        verify(invitacionRepository).save(invitacion);
        verify(userRepository, never()).save(any());
    }

    @Test
    void debeFallarSiJugadorYaTieneEquipo_TH01() {
        request.setRespuesta(RespuestaInvitacion.ACEPTAR);

        when(jugadorRepository.findById(1L)).thenReturn(Optional.of(jugador));
        when(invitacionRepository.findById(1L)).thenReturn(Optional.of(invitacion));

        BusinessRuleException ex = assertThrows(BusinessRuleException.class,
                () -> invitacionService.responderInvitacion(1L, 1L, request));

        assertEquals("Ya perteneces a un equipo de futbol, no puedes aceptar la invitación", ex.getMessage());
        assertEquals("RECHAZADA", invitacion.getEstado());

        verify(invitacionRepository).save(invitacion);
    }
}
