package edu.dosw.proyect.services;

import edu.dosw.proyect.controllers.dtos.request.CrearEquipoRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.CrearEquipoResponseDTO;
import edu.dosw.proyect.controllers.mappers.EquipoMapper;
import edu.dosw.proyect.core.exceptions.BusinessRuleException;
import edu.dosw.proyect.core.exceptions.ResourceNotFoundException;
import edu.dosw.proyect.core.services.impl.EquipoServiceImpl;
import edu.dosw.proyect.persistence.entity.EquipoEntity;
import edu.dosw.proyect.persistence.entity.UserEntity;
import edu.dosw.proyect.persistence.mapper.EquipoPersistenceMapper;
import edu.dosw.proyect.persistence.repository.EquipoRepository;
import edu.dosw.proyect.persistence.repository.InvitacionRepository;
import edu.dosw.proyect.persistence.repository.JugadorRepository;
import edu.dosw.proyect.persistence.repository.UserRepository;
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
class EquipoServiceImplTest {

    @Mock private EquipoRepository equipoRepository;
    @Mock private UserRepository userRepository;
    @Mock private JugadorRepository jugadorRepository;
    @Mock private InvitacionRepository invitacionRepository;
    @Mock private EquipoMapper equipoMapper;
    @Mock private EquipoPersistenceMapper equipoPersistenceMapper;

    @InjectMocks
    private EquipoServiceImpl equipoService;

    private UserEntity buildUserEntity(Long id, String programa) {
        UserEntity u = new UserEntity();
        u.setId(id);
        u.setName("Jugador " + id);
        u.setEmail("j" + id + "@mail.com");
        u.setAcademicProgram(programa);
        return u;
    }

    private CrearEquipoRequestDTO buildRequest(List<Long> jugadores) {
        CrearEquipoRequestDTO req = new CrearEquipoRequestDTO();
        req.setNombreEquipo("Alpha FC");
        req.setEscudo("alpha.png");
        req.setColoresUniforme("Rojo");
        req.setJugadoresInvitadosIds(jugadores);
        return req;
    }

    @Test
    void crearEquipo_HappyPath_RetornaResponse() {
        UserEntity capitan = buildUserEntity(1L, "sistemas");
        UserEntity j2 = buildUserEntity(2L, "sistemas");
        UserEntity j3 = buildUserEntity(3L, "ia");
        UserEntity j4 = buildUserEntity(4L, "sistemas");
        UserEntity j5 = buildUserEntity(5L, "ia");
        UserEntity j6 = buildUserEntity(6L, "ciberseguridad");
        UserEntity j7 = buildUserEntity(7L, "sistemas");

        CrearEquipoRequestDTO request = buildRequest(List.of(2L, 3L, 4L, 5L, 6L, 7L));

        when(userRepository.findById(1L)).thenReturn(Optional.of(capitan));
        when(equipoRepository.existsByNombre("Alpha FC")).thenReturn(false);
        when(userRepository.findById(2L)).thenReturn(Optional.of(j2));
        when(userRepository.findById(3L)).thenReturn(Optional.of(j3));
        when(userRepository.findById(4L)).thenReturn(Optional.of(j4));
        when(userRepository.findById(5L)).thenReturn(Optional.of(j5));
        when(userRepository.findById(6L)).thenReturn(Optional.of(j6));
        when(userRepository.findById(7L)).thenReturn(Optional.of(j7));
        when(equipoRepository.save(any())).thenReturn(new EquipoEntity());
        when(equipoMapper.toCrearEquipoResponseDTO(any(), any()))
                .thenReturn(CrearEquipoResponseDTO.builder()
                        .mensajeConfirmacion("Equipo creado").build());

        CrearEquipoResponseDTO result = equipoService.crearEquipo(1L, request);

        assertNotNull(result);
        verify(equipoRepository, times(1)).save(any());
    }

    @Test
    void crearEquipo_CapitanNoEncontrado_LanzaException() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,
                () -> equipoService.crearEquipo(99L, buildRequest(List.of())));
    }

    @Test
    void crearEquipo_NombreDuplicado_LanzaException() {
        UserEntity capitan = buildUserEntity(1L, "sistemas");
        when(userRepository.findById(1L)).thenReturn(Optional.of(capitan));
        when(equipoRepository.existsByNombre("Alpha FC")).thenReturn(true);

        assertThrows(BusinessRuleException.class,
                () -> equipoService.crearEquipo(1L, buildRequest(List.of())));
    }

    @Test
    void crearEquipo_MenosDe7Jugadores_LanzaException() {
        UserEntity capitan = buildUserEntity(1L, "sistemas");
        when(userRepository.findById(1L)).thenReturn(Optional.of(capitan));
        when(equipoRepository.existsByNombre("Alpha FC")).thenReturn(false);

        assertThrows(BusinessRuleException.class,
                () -> equipoService.crearEquipo(1L, buildRequest(List.of(2L, 3L))));
    }
}