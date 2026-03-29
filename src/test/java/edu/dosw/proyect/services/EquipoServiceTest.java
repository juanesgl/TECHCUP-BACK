package edu.dosw.proyect.services;

import edu.dosw.proyect.controllers.dtos.request.CrearEquipoRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.CrearEquipoResponseDTO;
import edu.dosw.proyect.core.exceptions.BusinessRuleException;
import edu.dosw.proyect.controllers.mappers.EquipoMapper;
import edu.dosw.proyect.core.models.Equipo;
import edu.dosw.proyect.core.repositories.EquipoRepository;
import edu.dosw.proyect.core.repositories.UserRepository;
import edu.dosw.proyect.core.repositories.InvitacionRepository;
import edu.dosw.proyect.core.repositories.JugadorRepository;
import edu.dosw.proyect.core.services.impl.EquipoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EquipoServiceTest {

    @Mock
    private EquipoRepository equipoRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EquipoMapper equipoMapper;

    @Mock
    private InvitacionRepository invitacionRepository;

    @Mock
    private JugadorRepository jugadorRepository;

    @InjectMocks
    private EquipoServiceImpl equipoService;

    private edu.dosw.proyect.core.models.User capitan;
    private CrearEquipoRequestDTO request;

    @BeforeEach
    void setUp() {
        capitan = edu.dosw.proyect.core.models.User.builder()
                .id(1L)
                .name("El Capitan")
                .email("capitan@test.com")
                .password("pass")
                .role("CAPTAIN")
                .academicProgram("sistemas")
                .build();

        request = new CrearEquipoRequestDTO();
        request.setNombreEquipo("Test FC");
        request.setJugadoresInvitadosIds(Arrays.asList(2L, 3L, 4L, 5L, 6L, 7L));
    }

    @Test
    void debeCrearEquipoExitosamente() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(capitan));
        when(equipoRepository.existsByNombre("Test FC")).thenReturn(false);
        when(jugadorRepository.findById(any(Long.class))).thenReturn(Optional.of(new edu.dosw.proyect.core.models.Jugador()));

        for (long i = 2; i <= 7; i++) {
            edu.dosw.proyect.core.models.User jugador = edu.dosw.proyect.core.models.User.builder()
                    .id(i)
                    .name("Compi " + i)
                    .email("mail"+i+"@test.com")
                    .password("pass")
                    .role("PLAYER")
                    .academicProgram("ia")
                    .build();
            when(userRepository.findById(i)).thenReturn(Optional.of(jugador));
        }

        CrearEquipoResponseDTO expectedResponse = CrearEquipoResponseDTO.builder()
                .mensajeConfirmacion("Exito")
                .notificacionesEnviadas(new ArrayList<>())
                .build();

        when(equipoMapper.toCrearEquipoResponseDTO(anyString(), anyList())).thenReturn(expectedResponse);

        CrearEquipoResponseDTO response = equipoService.crearEquipo(1L, request);

        assertEquals("Exito", response.getMensajeConfirmacion());
        verify(equipoRepository).save(any(Equipo.class));
        verify(userRepository).save(capitan);
    }

    @Test
    void debeFallarPorNombreDuplicado_TH01() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(capitan));
        when(equipoRepository.existsByNombre("Test FC")).thenReturn(true);

        BusinessRuleException exception = assertThrows(BusinessRuleException.class, () -> 
            equipoService.crearEquipo(1L, request)
        );

        assertEquals("Ya existe un equipo con ese nombre en el torneo", exception.getMessage());
        verify(equipoRepository, never()).save(any(Equipo.class));
    }

    @Test
    void debeFallarPorComposicionAcademicaInvalida_TH03() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(capitan));
        when(equipoRepository.existsByNombre("Test FC")).thenReturn(false);

        for (long i = 2; i <= 7; i++) {
            edu.dosw.proyect.core.models.User jugador = edu.dosw.proyect.core.models.User.builder()
                    .id(i)
                    .name("Compi " + i)
                    .email("mail"+i+"@test.com")
                    .password("pass")
                    .role("PLAYER")
                    .academicProgram("medicina")
                    .build();
            when(userRepository.findById(i)).thenReturn(Optional.of(jugador));
        }

        BusinessRuleException exception = assertThrows(BusinessRuleException.class, () -> 
            equipoService.crearEquipo(1L, request)
        );

        assertEquals("error de validaciÃ³n de composiciÃ³n del equipo", exception.getMessage());
        verify(equipoRepository, never()).save(any(Equipo.class));
    }
}

