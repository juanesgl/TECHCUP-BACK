package edu.dosw.proyect.services;

import edu.dosw.proyect.dtos.request.RespuestaInvitacionRequestDTO;
import edu.dosw.proyect.dtos.response.InvitacionResponseDTO;
import edu.dosw.proyect.exceptions.BusinessRuleException;
import edu.dosw.proyect.mappers.InvitacionMapper;
import edu.dosw.proyect.models.User;
import edu.dosw.proyect.models.Equipo;
import edu.dosw.proyect.models.Invitacion;
import edu.dosw.proyect.models.SportProfile;
import edu.dosw.proyect.models.enums.EstadoInvitacion;
import edu.dosw.proyect.models.enums.RespuestaInvitacion;
import edu.dosw.proyect.repositories.InvitacionRepository;
import edu.dosw.proyect.repositories.UserRepository;
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
    private UserRepository userRepository;

    @Mock
    private InvitacionMapper invitacionMapper;

    @InjectMocks
    private edu.dosw.proyect.services.impl.InvitacionServiceImpl invitacionService;

    static class TestPlayer extends User {
        private SportProfile sportProfile;
        public TestPlayer(Long id, String name, SportProfile sportProfile) {
            super(name, "test@test", "pass", "PLAYER");
            setId(id);
            this.sportProfile = sportProfile;
        }
        @Override
        public SportProfile getSportProfile() {
            return sportProfile;
        }
    }

    private TestPlayer jugador;
    private Invitacion invitacion;
    private RespuestaInvitacionRequestDTO request;

    @BeforeEach
    void setUp() {
        SportProfile profile = new SportProfile();
        jugador = new TestPlayer(1L, "Test Jugador", profile);

        TestPlayer capitan = new TestPlayer(2L, "Capitan", new SportProfile());
        Equipo equipo = Equipo.builder().id(1L).nombre("Test FC").build();

        invitacion = new Invitacion(1L, jugador, equipo, capitan, EstadoInvitacion.PENDIENTE);
        request = new RespuestaInvitacionRequestDTO();
    }

    @Test
    void debeAceptarInvitacionExitosamente() {
        request.setRespuesta(RespuestaInvitacion.ACEPTAR);
        when(userRepository.findById(1L)).thenReturn(Optional.of(jugador));
        when(invitacionRepository.findById(1L)).thenReturn(Optional.of(invitacion));
        when(invitacionMapper.toResponseDTO(any(), anyString()))
                .thenReturn(InvitacionResponseDTO.builder().estadoActualizado("ACEPTADA").build());

        InvitacionResponseDTO response = invitacionService.responderInvitacion(1L, 1L, request);

        assertEquals("ACEPTADA", response.getEstadoActualizado());
        assertEquals(EstadoInvitacion.ACEPTADA, invitacion.getEstado());
        assertEquals("Test FC", jugador.getSportProfile().getEquipoActual().getNombre());
        
        verify(invitacionRepository).save(invitacion);
        verify(userRepository).save(jugador);
    }

    @Test
    void debeRechazarInvitacionExitosamente() {
        request.setRespuesta(RespuestaInvitacion.RECHAZAR);
        when(userRepository.findById(1L)).thenReturn(Optional.of(jugador));
        when(invitacionRepository.findById(1L)).thenReturn(Optional.of(invitacion));
        when(invitacionMapper.toResponseDTO(any(), anyString()))
                .thenReturn(InvitacionResponseDTO.builder().estadoActualizado("RECHAZADA").build());

        InvitacionResponseDTO response = invitacionService.responderInvitacion(1L, 1L, request);

        assertEquals("RECHAZADA", response.getEstadoActualizado());
        assertEquals(EstadoInvitacion.RECHAZADA, invitacion.getEstado());
        assertNull(jugador.getSportProfile().getEquipoActual());

        verify(invitacionRepository).save(invitacion);
        verify(userRepository, never()).save(any());
    }

    @Test
    void debeFallarSiJugadorYaTieneEquipo_TH01() {
        request.setRespuesta(RespuestaInvitacion.ACEPTAR);
        jugador.getSportProfile().setEquipoActual(Equipo.builder().id(2L).nombre("Otro FC").build());

        when(userRepository.findById(1L)).thenReturn(Optional.of(jugador));
        when(invitacionRepository.findById(1L)).thenReturn(Optional.of(invitacion));

        BusinessRuleException ex = assertThrows(BusinessRuleException.class, 
            () -> invitacionService.responderInvitacion(1L, 1L, request));

        assertEquals("Ya perteneces a un equipo de futbol, no puedes aceptar la invitación", ex.getMessage());
        assertEquals(EstadoInvitacion.RECHAZADA, invitacion.getEstado());
        
        verify(invitacionRepository).save(invitacion);
    }
}
