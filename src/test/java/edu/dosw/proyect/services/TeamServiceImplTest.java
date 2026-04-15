package edu.dosw.proyect.services;

import edu.dosw.proyect.controllers.dtos.request.CreateTeamRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.CreateTeamResponseDTO;
import edu.dosw.proyect.controllers.mappers.TeamMapper;
import edu.dosw.proyect.core.exceptions.BusinessRuleException;
import edu.dosw.proyect.core.exceptions.ResourceNotFoundException;
import edu.dosw.proyect.core.services.impl.TeamServiceImpl;
import edu.dosw.proyect.persistence.entity.TeamEntity;
import edu.dosw.proyect.persistence.entity.UserEntity;
import edu.dosw.proyect.persistence.mapper.TeamPersistenceMapper;
import edu.dosw.proyect.persistence.repository.TeamRepository;
import edu.dosw.proyect.persistence.repository.InvitationRepository;
import edu.dosw.proyect.persistence.repository.PlayerRepository;
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
class TeamServiceImplTest {

    @Mock private TeamRepository teamRepository;
    @Mock private UserRepository userRepository;
    @Mock private PlayerRepository playerRepository;
    @Mock private InvitationRepository invitationRepository;
    @Mock private TeamMapper teamMapper;
    @Mock private TeamPersistenceMapper teamPersistenceMapper;

    @InjectMocks
    private TeamServiceImpl equipoService;

    private UserEntity buildUserEntity(Long id, String programa) {
        UserEntity u = new UserEntity();
        u.setId(id);
        u.setName("Jugador " + id);
        u.setEmail("j" + id + "@mail.com");
        u.setAcademicProgram(programa);
        return u;
    }

    private CreateTeamRequestDTO buildRequest(List<Long> jugadores) {
        CreateTeamRequestDTO req = new CreateTeamRequestDTO();
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
        UserEntity j8 = buildUserEntity(8L, "ia");

        CreateTeamRequestDTO request = buildRequest(List.of(2L, 3L, 4L, 5L, 6L, 7L, 8L));

        when(userRepository.findById(1L)).thenReturn(Optional.of(capitan));
        when(teamRepository.existsByNombre("Alpha FC")).thenReturn(false);
        when(userRepository.findById(2L)).thenReturn(Optional.of(j2));
        when(userRepository.findById(3L)).thenReturn(Optional.of(j3));
        when(userRepository.findById(4L)).thenReturn(Optional.of(j4));
        when(userRepository.findById(5L)).thenReturn(Optional.of(j5));
        when(userRepository.findById(6L)).thenReturn(Optional.of(j6));
        when(userRepository.findById(7L)).thenReturn(Optional.of(j7));
        when(userRepository.findById(8L)).thenReturn(Optional.of(j8));
        when(teamRepository.save(any())).thenReturn(new TeamEntity());
        when(teamMapper.toCrearEquipoResponseDTO(any(), any()))
                .thenReturn(CreateTeamResponseDTO.builder()
                        .mensajeConfirmacion("Equipo creado").build());

        CreateTeamResponseDTO result = equipoService.crearEquipo(1L, request);

        assertNotNull(result);
        verify(teamRepository, times(1)).save(any());
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
        when(teamRepository.existsByNombre("Alpha FC")).thenReturn(true);

        assertThrows(BusinessRuleException.class,
                () -> equipoService.crearEquipo(1L, buildRequest(List.of())));
    }

    @Test
    void crearEquipo_MenosDe7Jugadores_LanzaException() {
        UserEntity capitan = buildUserEntity(1L, "sistemas");
        when(userRepository.findById(1L)).thenReturn(Optional.of(capitan));
        when(teamRepository.existsByNombre("Alpha FC")).thenReturn(false);

        assertThrows(BusinessRuleException.class,
                () -> equipoService.crearEquipo(1L, buildRequest(List.of(2L, 3L))));
    }
}