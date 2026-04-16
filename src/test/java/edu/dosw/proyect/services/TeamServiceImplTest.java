package edu.dosw.proyect.services;

import edu.dosw.proyect.controllers.dtos.request.CreateTeamRequestDTO;
import edu.dosw.proyect.controllers.dtos.request.UpdateTeamRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.CreateTeamResponseDTO;
import edu.dosw.proyect.controllers.dtos.response.TeamResponseDTO;
import edu.dosw.proyect.controllers.mappers.TeamMapper;
import edu.dosw.proyect.core.exceptions.BusinessRuleException;
import edu.dosw.proyect.core.exceptions.ResourceNotFoundException;
import edu.dosw.proyect.core.services.impl.TeamServiceImpl;
import edu.dosw.proyect.persistence.entity.PlayerEntity;
import edu.dosw.proyect.persistence.entity.TeamEntity;
import edu.dosw.proyect.persistence.entity.TeamPlayerEntity;
import edu.dosw.proyect.persistence.entity.TournamentEntity;
import edu.dosw.proyect.persistence.entity.UserEntity;
import edu.dosw.proyect.persistence.mapper.TeamPersistenceMapper;
import edu.dosw.proyect.persistence.repository.InvitationRepository;
import edu.dosw.proyect.persistence.repository.PlayerRepository;
import edu.dosw.proyect.persistence.repository.TeamPlayerRepository;
import edu.dosw.proyect.persistence.repository.TeamRepository;
import edu.dosw.proyect.persistence.repository.TournamentRepository;
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

    @Mock private TeamRepository         teamRepository;
    @Mock private UserRepository         userRepository;
    @Mock private PlayerRepository       playerRepository;
    @Mock private InvitationRepository   invitationRepository;
    @Mock private TeamPlayerRepository   teamPlayerRepository;
    @Mock private TournamentRepository   torneoRepository;
    @Mock private TeamMapper             teamMapper;
    @Mock private TeamPersistenceMapper  teamPersistenceMapper;

    @InjectMocks
    private TeamServiceImpl equipoService;

    // ─── Helpers ─────────────────────────────────────────────────────────────

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

    // ─── crearEquipo ─────────────────────────────────────────────────────────

    @Test
    void crearEquipo_HappyPath_RetornaResponse() {
        UserEntity capitan = buildUserEntity(1L, "sistemas");
        List<Long> ids = List.of(2L, 3L, 4L, 5L, 6L, 7L, 8L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(capitan));
        when(teamRepository.existsByNombre("Alpha FC")).thenReturn(false);
        for (long i = 2; i <= 8; i++) {
            when(userRepository.findById(i))
                    .thenReturn(Optional.of(buildUserEntity(i, "sistemas")));
        }
        when(teamRepository.save(any())).thenReturn(new TeamEntity());
        when(teamMapper.toCrearEquipoResponseDTO(any(), any()))
                .thenReturn(CreateTeamResponseDTO.builder()
                        .mensajeConfirmacion("Equipo creado").build());

        CreateTeamResponseDTO result = equipoService.crearEquipo(1L, buildRequest(ids));

        assertNotNull(result);
        verify(teamRepository, times(1)).save(any());
    }

    @Test
    void crearEquipo_CapitanNoEncontrado_LanzaException() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());
        CreateTeamRequestDTO request = buildRequest(List.of());

        assertThrows(ResourceNotFoundException.class,
                () -> equipoService.crearEquipo(99L, request));
    }

    @Test
    void crearEquipo_NombreDuplicado_LanzaException() {
        UserEntity capitan = buildUserEntity(1L, "sistemas");
        when(userRepository.findById(1L)).thenReturn(Optional.of(capitan));
        when(teamRepository.existsByNombre("Alpha FC")).thenReturn(true);
        CreateTeamRequestDTO request = buildRequest(List.of());

        assertThrows(BusinessRuleException.class,
                () -> equipoService.crearEquipo(1L, request));
    }

    @Test
    void crearEquipo_MenosDe7Jugadores_LanzaException() {
        UserEntity capitan = buildUserEntity(1L, "sistemas");
        when(userRepository.findById(1L)).thenReturn(Optional.of(capitan));
        when(teamRepository.existsByNombre("Alpha FC")).thenReturn(false);
        CreateTeamRequestDTO request = buildRequest(List.of(2L, 3L));

        assertThrows(BusinessRuleException.class,
                () -> equipoService.crearEquipo(1L, request));
    }

    @Test
    void crearEquipo_MasDe12Jugadores_LanzaException() {
        UserEntity capitan = buildUserEntity(1L, "sistemas");
        // 12 invitados + capitán = 13 → supera MAX_JUGADORES
        List<Long> ids = List.of(2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L, 11L, 12L, 13L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(capitan));
        when(teamRepository.existsByNombre("Alpha FC")).thenReturn(false);
        for (long i = 2; i <= 13; i++) {
            when(userRepository.findById(i))
                    .thenReturn(Optional.of(buildUserEntity(i, "sistemas")));
        }

        CreateTeamRequestDTO request = buildRequest(ids);
        assertThrows(BusinessRuleException.class,
                () -> equipoService.crearEquipo(1L, request));
    }

    @Test
    void crearEquipo_MenosDe50PorCientoCarrerasAdmitidas_LanzaException() {
        UserEntity capitan = buildUserEntity(1L, "medicina"); // NO admitida
        List<Long> ids = List.of(2L, 3L, 4L, 5L, 6L, 7L, 8L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(capitan));
        when(teamRepository.existsByNombre("Alpha FC")).thenReturn(false);
        for (long i = 2; i <= 8; i++) {
            when(userRepository.findById(i))
                    .thenReturn(Optional.of(buildUserEntity(i, "medicina")));
        }

        CreateTeamRequestDTO request = buildRequest(ids);
        assertThrows(BusinessRuleException.class,
                () -> equipoService.crearEquipo(1L, request));
    }

    @Test
    void crearEquipo_JugadorYaTieneEquipo_LanzaException() {
        UserEntity capitan = buildUserEntity(1L, "sistemas");
        List<Long> ids = List.of(2L, 3L, 4L, 5L, 6L, 7L, 8L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(capitan));
        when(teamRepository.existsByNombre("Alpha FC")).thenReturn(false);
        for (long i = 2; i <= 8; i++) {
            when(userRepository.findById(i))
                    .thenReturn(Optional.of(buildUserEntity(i, "sistemas")));
        }

        PlayerEntity jugadorConEquipo = mock(PlayerEntity.class);
        when(jugadorConEquipo.isTieneEquipo()).thenReturn(true);
        when(playerRepository.findById(2L)).thenReturn(Optional.of(jugadorConEquipo));

        CreateTeamRequestDTO request = buildRequest(ids);
        assertThrows(BusinessRuleException.class,
                () -> equipoService.crearEquipo(1L, request));
    }

    // ─── consultarEquipo ─────────────────────────────────────────────────────

    @Test
    void consultarEquipo_Existe_RetornaDTO() {
        TeamEntity entity = new TeamEntity();
        entity.setId(1L);

        when(teamRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(teamPersistenceMapper.toDomain(entity)).thenReturn(null);
        when(teamMapper.toResponseDTO(any())).thenReturn(new TeamResponseDTO());

        assertNotNull(equipoService.consultarEquipo(1L));
    }

    @Test
    void consultarEquipo_NoExiste_LanzaResourceNotFound() {
        when(teamRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> equipoService.consultarEquipo(99L));
    }

    // ─── consultarEquiposPorTorneo ────────────────────────────────────────────

    @Test
    void consultarEquiposPorTorneo_TorneoExiste_RetornaLista() {
        TournamentEntity torneo = mock(TournamentEntity.class);
        when(torneo.getId()).thenReturn(1L);
        when(torneoRepository.findByTournId("T1")).thenReturn(Optional.of(torneo));

        TeamEntity e = new TeamEntity();
        when(teamRepository.findByTorneoId(1L)).thenReturn(List.of(e));
        when(teamPersistenceMapper.toDomain(e)).thenReturn(null);
        when(teamMapper.toResponseDTO(any())).thenReturn(new TeamResponseDTO());

        List<TeamResponseDTO> result = equipoService.consultarEquiposPorTorneo("T1");

        assertEquals(1, result.size());
    }

    @Test
    void consultarEquiposPorTorneo_TorneoNoExiste_LanzaResourceNotFound() {
        when(torneoRepository.findByTournId("X")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> equipoService.consultarEquiposPorTorneo("X"));
    }

    // ─── actualizarEquipo ─────────────────────────────────────────────────────

    @Test
    void actualizarEquipo_CapitanValido_ActualizaCampos() {
        PlayerEntity jugadorCapitan = mock(PlayerEntity.class);
        when(jugadorCapitan.getId()).thenReturn(1L);

        TeamEntity equipo = new TeamEntity();
        equipo.setId(1L);
        equipo.setNombre("Viejo nombre");
        equipo.setCapitan(jugadorCapitan);

        when(teamRepository.findById(1L)).thenReturn(Optional.of(equipo));
        when(teamRepository.existsByNombre("Nuevo nombre")).thenReturn(false);
        when(teamRepository.save(any())).thenReturn(equipo);
        when(teamPersistenceMapper.toDomain(any())).thenReturn(null);
        when(teamMapper.toResponseDTO(any())).thenReturn(new TeamResponseDTO());

        UpdateTeamRequestDTO req = new UpdateTeamRequestDTO();
        req.setNombreEquipo("Nuevo nombre");
        req.setEscudo("nuevo.png");
        req.setColoresUniforme("Azul");

        assertNotNull(equipoService.actualizarEquipo(1L, 1L, req));
    }

    @Test
    void actualizarEquipo_NombreDuplicado_LanzaException() {
        PlayerEntity jugadorCapitan = mock(PlayerEntity.class);
        when(jugadorCapitan.getId()).thenReturn(1L);

        TeamEntity equipo = new TeamEntity();
        equipo.setNombre("Nombre actual");
        equipo.setCapitan(jugadorCapitan);

        when(teamRepository.findById(1L)).thenReturn(Optional.of(equipo));
        when(teamRepository.existsByNombre("Nombre nuevo")).thenReturn(true);

        UpdateTeamRequestDTO req = new UpdateTeamRequestDTO();
        req.setNombreEquipo("Nombre nuevo");

        assertThrows(BusinessRuleException.class,
                () -> equipoService.actualizarEquipo(1L, 1L, req));
    }

    @Test
    void actualizarEquipo_NoEsCapitan_LanzaException() {
        PlayerEntity jugadorCapitan = mock(PlayerEntity.class);
        when(jugadorCapitan.getId()).thenReturn(1L);

        TeamEntity equipo = new TeamEntity();
        equipo.setCapitan(jugadorCapitan);

        when(teamRepository.findById(1L)).thenReturn(Optional.of(equipo));

        UpdateTeamRequestDTO request = new UpdateTeamRequestDTO();
        assertThrows(BusinessRuleException.class,
                () -> equipoService.actualizarEquipo(1L, 99L, request));
    }

    @Test
    void actualizarEquipo_EquipoNoExiste_LanzaResourceNotFound() {
        when(teamRepository.findById(99L)).thenReturn(Optional.empty());
        UpdateTeamRequestDTO request = new UpdateTeamRequestDTO();

        assertThrows(ResourceNotFoundException.class,
                () -> equipoService.actualizarEquipo(99L, 1L, request));
    }

    // ─── eliminarEquipo ───────────────────────────────────────────────────────

    @Test
    void eliminarEquipo_CapitanValido_Elimina() {
        PlayerEntity jugador = mock(PlayerEntity.class);
        when(jugador.getId()).thenReturn(1L);

        TeamEntity equipo = new TeamEntity();
        equipo.setId(1L);
        equipo.setCapitan(jugador);

        TeamPlayerEntity tp = mock(TeamPlayerEntity.class);
        when(tp.getJugador()).thenReturn(jugador);

        when(teamRepository.findById(1L)).thenReturn(Optional.of(equipo));
        when(teamPlayerRepository.findByEquipoId(1L)).thenReturn(List.of(tp));

        equipoService.eliminarEquipo(1L, 1L);

        verify(teamRepository).delete(equipo);
    }

    @Test
    void eliminarEquipo_NoEsCapitan_LanzaException() {
        PlayerEntity jugadorCapitan = mock(PlayerEntity.class);
        when(jugadorCapitan.getId()).thenReturn(1L);

        TeamEntity equipo = new TeamEntity();
        equipo.setCapitan(jugadorCapitan);

        when(teamRepository.findById(1L)).thenReturn(Optional.of(equipo));

        assertThrows(BusinessRuleException.class,
                () -> equipoService.eliminarEquipo(1L, 99L));
    }

    @Test
    void eliminarEquipo_EquipoNoExiste_LanzaResourceNotFound() {
        when(teamRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> equipoService.eliminarEquipo(99L, 1L));
    }

    // ─── consultarJugadoresEquipo ─────────────────────────────────────────────

    @Test
    void consultarJugadoresEquipo_ConUsuario_RetornaListaConNombre() {
        when(teamRepository.findById(1L)).thenReturn(Optional.of(new TeamEntity()));

        UserEntity usuario = new UserEntity();
        usuario.setName("Carlos");

        PlayerEntity jugador = mock(PlayerEntity.class);
        when(jugador.getId()).thenReturn(10L);
        when(jugador.getUsuario()).thenReturn(usuario);

        TeamPlayerEntity tp = mock(TeamPlayerEntity.class);
        when(tp.isActivo()).thenReturn(true);
        when(tp.getJugador()).thenReturn(jugador);

        when(teamPlayerRepository.findByEquipoId(1L)).thenReturn(List.of(tp));

        List<String> result = equipoService.consultarJugadoresEquipo(1L);

        assertEquals(1, result.size());
        assertTrue(result.get(0).contains("Carlos"));
    }

    @Test
    void consultarJugadoresEquipo_SinUsuario_RetornaFallbackConId() {
        when(teamRepository.findById(1L)).thenReturn(Optional.of(new TeamEntity()));

        PlayerEntity jugador = mock(PlayerEntity.class);
        when(jugador.getId()).thenReturn(10L);
        when(jugador.getUsuario()).thenReturn(null);

        TeamPlayerEntity tp = mock(TeamPlayerEntity.class);
        when(tp.isActivo()).thenReturn(true);
        when(tp.getJugador()).thenReturn(jugador);

        when(teamPlayerRepository.findByEquipoId(1L)).thenReturn(List.of(tp));

        List<String> result = equipoService.consultarJugadoresEquipo(1L);

        assertTrue(result.get(0).contains("Jugador #10"));
    }

    @Test
    void consultarJugadoresEquipo_EquipoNoExiste_LanzaResourceNotFound() {
        when(teamRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> equipoService.consultarJugadoresEquipo(99L));
    }
}