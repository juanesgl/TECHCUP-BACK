package edu.dosw.proyect.services;

import edu.dosw.proyect.controllers.dtos.request.SaveLineupRequestDTO;
import edu.dosw.proyect.controllers.dtos.request.StarterEntryRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.TeamLineupResponseDTO;
import edu.dosw.proyect.controllers.mappers.TeamLineupMapper;
import edu.dosw.proyect.core.exceptions.BusinessRuleException;
import edu.dosw.proyect.core.exceptions.ResourceNotFoundException;
import edu.dosw.proyect.core.models.Equipo;
import edu.dosw.proyect.core.models.Jugador;
import edu.dosw.proyect.core.models.Partido;
import edu.dosw.proyect.core.models.TeamLineup;
import edu.dosw.proyect.core.models.User;
import edu.dosw.proyect.core.models.enums.FieldPosition;
import edu.dosw.proyect.core.models.enums.LineupStatus;
import edu.dosw.proyect.core.models.enums.MatchStatus;
import edu.dosw.proyect.core.models.enums.TacticalFormation;
import edu.dosw.proyect.core.services.authorization.AuthorizationValidator;
import edu.dosw.proyect.core.services.impl.TeamLineupServiceImpl;
import edu.dosw.proyect.persistence.entity.EquipoEntity;
import edu.dosw.proyect.persistence.entity.PartidoEntity;
import edu.dosw.proyect.persistence.entity.UserEntity;
import edu.dosw.proyect.persistence.mapper.EquipoPersistenceMapper;
import edu.dosw.proyect.persistence.mapper.PartidoPersistenceMapper;
import edu.dosw.proyect.persistence.mapper.UserPersistenceMapper;
import edu.dosw.proyect.persistence.repository.EquipoRepository;
import edu.dosw.proyect.persistence.repository.PartidoRepository;
import edu.dosw.proyect.persistence.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TeamLineupServiceImplTest {

    @Mock private TeamLineupRepository lineupRepository;
    @Mock private EquipoRepository equipoRepository;
    @Mock private PartidoRepository matchRepository;
    @Mock private UserRepository userRepository;
    @Mock private TeamLineupMapper lineupMapper;
    @Mock private AuthorizationValidator authorizationValidator;
    @Mock private PartidoPersistenceMapper partidoMapper;
    @Mock private EquipoPersistenceMapper equipoMapper;
    @Mock private UserPersistenceMapper userMapper;

    @InjectMocks
    private TeamLineupServiceImpl teamLineupService;

    private UserEntity buildUserEntity(Long id) {
        UserEntity u = new UserEntity();
        u.setId(id);
        u.setName("User " + id);
        u.setRole("CAPTAIN");
        return u;
    }

    private User buildUser(Long id) {
        return User.builder().id(id).name("User " + id)
                .email("u@mail.com").password("p").role("CAPTAIN").build();
    }

    private Equipo buildEquipo(Long id, Long captainId) {
        Jugador capitan = new Jugador();
        capitan.setId(captainId);
        Equipo e = new Equipo();
        e.setId(id);
        e.setNombre("Alpha FC");
        e.setCapitan(capitan);
        return e;
    }

    private Partido buildPartido(Long localId, Long visitanteId) {
        Equipo local = new Equipo();
        local.setId(localId);
        Equipo visitante = new Equipo();
        visitante.setId(visitanteId);
        Partido p = new Partido();
        p.setId(1L);
        p.setEstado(MatchStatus.PROGRAMADO);
        p.setEquipoLocal(local);
        p.setEquipoVisitante(visitante);
        return p;
    }

    private SaveLineupRequestDTO buildRequest() {
        StarterEntryRequestDTO starter =
                new StarterEntryRequestDTO(1L, FieldPosition.FORWARD);
        SaveLineupRequestDTO req = new SaveLineupRequestDTO();
        req.setTeamId(1L);
        req.setMatchId(1L);
        req.setFormation(TacticalFormation.F_1_2_3_1);
        req.setStarters(List.of(starter, starter, starter,
                starter, starter, starter, starter));
        req.setReserveIds(List.of());
        return req;
    }

    @Test
    void saveLineup_HappyPath_RetornaCreated() {
        UserEntity captainEntity = buildUserEntity(1L);
        User captain = buildUser(1L);
        EquipoEntity equipoEntity = new EquipoEntity();
        Equipo equipo = buildEquipo(1L, 1L);
        PartidoEntity partidoEntity = new PartidoEntity();
        Partido partido = buildPartido(1L, 2L);
        TeamLineup lineup = TeamLineup.builder().id(1L)
                .teamId(1L).matchId(1L).status(LineupStatus.SAVED)
                .starters(List.of()).reserveIds(List.of()).build();
        TeamLineupResponseDTO dto = TeamLineupResponseDTO.builder()
                .lineupId(1L).build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(captainEntity));
        when(userMapper.toDomain(captainEntity)).thenReturn(captain);
        when(equipoRepository.findById(1L)).thenReturn(Optional.of(equipoEntity));
        when(equipoMapper.toDomain(equipoEntity)).thenReturn(equipo);
        when(matchRepository.findById(1L)).thenReturn(Optional.of(partidoEntity));
        when(partidoMapper.toDomain(partidoEntity)).thenReturn(partido);
        when(lineupRepository.findByTeamIdAndMatchId(1L, 1L))
                .thenReturn(Optional.empty());
        when(lineupMapper.toNewTeamLineup(any(), any(), any())).thenReturn(lineup);
        when(lineupMapper.toResponseDTO(any(), any())).thenReturn(dto);

        TeamLineupResponseDTO result =
                teamLineupService.saveLineup(1L, buildRequest());

        assertNotNull(result);
        verify(lineupRepository, times(1)).save(lineup);
    }

    @Test
    void saveLineup_CapitanNoEncontrado_LanzaException() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,
                () -> teamLineupService.saveLineup(99L, buildRequest()));
    }

    @Test
    void saveLineup_AlineacionYaExiste_LanzaException() {
        UserEntity captainEntity = buildUserEntity(1L);
        User captain = buildUser(1L);
        EquipoEntity equipoEntity = new EquipoEntity();
        Equipo equipo = buildEquipo(1L, 1L);
        PartidoEntity partidoEntity = new PartidoEntity();
        Partido partido = buildPartido(1L, 2L);
        TeamLineup existing = TeamLineup.builder().id(1L)
                .starters(List.of()).reserveIds(List.of()).build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(captainEntity));
        when(userMapper.toDomain(captainEntity)).thenReturn(captain);
        when(equipoRepository.findById(1L)).thenReturn(Optional.of(equipoEntity));
        when(equipoMapper.toDomain(equipoEntity)).thenReturn(equipo);
        when(matchRepository.findById(1L)).thenReturn(Optional.of(partidoEntity));
        when(partidoMapper.toDomain(partidoEntity)).thenReturn(partido);
        when(lineupRepository.findByTeamIdAndMatchId(1L, 1L))
                .thenReturn(Optional.of(existing));

        assertThrows(BusinessRuleException.class,
                () -> teamLineupService.saveLineup(1L, buildRequest()));
    }

    @Test
    void updateLineup_HappyPath_RetornaOk() {
        UserEntity captainEntity = buildUserEntity(1L);
        User captain = buildUser(1L);
        EquipoEntity equipoEntity = new EquipoEntity();
        Equipo equipo = buildEquipo(1L, 1L);
        TeamLineup existing = TeamLineup.builder().id(1L)
                .teamId(1L).matchId(1L).status(LineupStatus.SAVED)
                .starters(List.of()).reserveIds(List.of()).build();
        TeamLineupResponseDTO dto = TeamLineupResponseDTO.builder()
                .lineupId(1L).build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(captainEntity));
        when(userMapper.toDomain(captainEntity)).thenReturn(captain);
        when(lineupRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(equipoRepository.findById(1L)).thenReturn(Optional.of(equipoEntity));
        when(equipoMapper.toDomain(equipoEntity)).thenReturn(equipo);
        when(lineupMapper.toResponseDTO(any(), any())).thenReturn(dto);

        TeamLineupResponseDTO result =
                teamLineupService.updateLineup(1L, 1L, buildRequest());

        assertNotNull(result);
        verify(lineupRepository, times(1)).save(existing);
    }

    @Test
    void getTeamLineups_HappyPath_RetornaLista() {
        UserEntity captainEntity = buildUserEntity(1L);
        User captain = buildUser(1L);
        EquipoEntity equipoEntity = new EquipoEntity();
        Equipo equipo = buildEquipo(1L, 1L);
        TeamLineup lineup = TeamLineup.builder().id(1L)
                .starters(List.of()).reserveIds(List.of()).build();
        TeamLineupResponseDTO dto = TeamLineupResponseDTO.builder()
                .lineupId(1L).build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(captainEntity));
        when(userMapper.toDomain(captainEntity)).thenReturn(captain);
        lenient().when(equipoRepository.findById(1L)).thenReturn(Optional.of(equipoEntity));
        lenient().when(equipoMapper.toDomain(equipoEntity)).thenReturn(equipo);
        when(lineupRepository.findByTeamId(1L)).thenReturn(List.of(lineup));
        when(lineupMapper.toResponseDTO(any(), any())).thenReturn(dto);

        List<TeamLineupResponseDTO> result =
                teamLineupService.getTeamLineups(1L, 1L);

        assertEquals(1, result.size());
    }

    @Test
    void getLineup_HappyPath_RetornaOk() {
        UserEntity captainEntity = buildUserEntity(1L);
        User captain = buildUser(1L);
        EquipoEntity equipoEntity = new EquipoEntity();
        Equipo equipo = buildEquipo(1L, 1L);
        TeamLineup lineup = TeamLineup.builder().id(1L)
                .starters(List.of()).reserveIds(List.of()).build();
        TeamLineupResponseDTO dto = TeamLineupResponseDTO.builder()
                .lineupId(1L).build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(captainEntity));
        when(userMapper.toDomain(captainEntity)).thenReturn(captain);
        when(equipoRepository.findById(1L)).thenReturn(Optional.of(equipoEntity));
        when(equipoMapper.toDomain(equipoEntity)).thenReturn(equipo);
        when(lineupRepository.findByTeamIdAndMatchId(1L, 1L))
                .thenReturn(Optional.of(lineup));
        when(lineupMapper.toResponseDTO(any(), any())).thenReturn(dto);

        TeamLineupResponseDTO result =
                teamLineupService.getLineup(1L, 1L, 1L);

        assertNotNull(result);
    }

    @Test
    void saveLineup_EquipoNoParticipa_LanzaException() {
        UserEntity captainEntity = buildUserEntity(1L);
        User captain = buildUser(1L);
        EquipoEntity equipoEntity = new EquipoEntity();
        Equipo equipo = buildEquipo(1L, 1L);
        PartidoEntity partidoEntity = new PartidoEntity();
        Partido partido = buildPartido(3L, 4L); // equipo 1 no participa

        when(userRepository.findById(1L)).thenReturn(Optional.of(captainEntity));
        when(userMapper.toDomain(captainEntity)).thenReturn(captain);
        when(equipoRepository.findById(1L)).thenReturn(Optional.of(equipoEntity));
        when(equipoMapper.toDomain(equipoEntity)).thenReturn(equipo);
        when(matchRepository.findById(1L)).thenReturn(Optional.of(partidoEntity));
        when(partidoMapper.toDomain(partidoEntity)).thenReturn(partido);

        assertThrows(BusinessRuleException.class,
                () -> teamLineupService.saveLineup(1L, buildRequest()));
    }

    @Test
    void saveLineup_PartidoNoEsProgramado_LanzaException() {
        UserEntity captainEntity = buildUserEntity(1L);
        User captain = buildUser(1L);
        EquipoEntity equipoEntity = new EquipoEntity();
        Equipo equipo = buildEquipo(1L, 1L);
        PartidoEntity partidoEntity = new PartidoEntity();
        Partido partido = buildPartido(1L, 2L);
        partido.setEstado(MatchStatus.FINALIZADO);

        when(userRepository.findById(1L)).thenReturn(Optional.of(captainEntity));
        when(userMapper.toDomain(captainEntity)).thenReturn(captain);
        when(equipoRepository.findById(1L)).thenReturn(Optional.of(equipoEntity));
        when(equipoMapper.toDomain(equipoEntity)).thenReturn(equipo);
        when(matchRepository.findById(1L)).thenReturn(Optional.of(partidoEntity));
        when(partidoMapper.toDomain(partidoEntity)).thenReturn(partido);

        assertThrows(BusinessRuleException.class,
                () -> teamLineupService.saveLineup(1L, buildRequest()));
    }

    @Test
    void saveLineup_CapitanNoEsDueno_LanzaException() {
        UserEntity captainEntity = buildUserEntity(1L);
        User captain = buildUser(1L);
        EquipoEntity equipoEntity = new EquipoEntity();
        Equipo equipo = buildEquipo(1L, 99L); // capitan diferente
        PartidoEntity partidoEntity = new PartidoEntity();
        Partido partido = buildPartido(1L, 2L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(captainEntity));
        when(userMapper.toDomain(captainEntity)).thenReturn(captain);
        when(equipoRepository.findById(1L)).thenReturn(Optional.of(equipoEntity));
        when(equipoMapper.toDomain(equipoEntity)).thenReturn(equipo);
        when(matchRepository.findById(1L)).thenReturn(Optional.of(partidoEntity));
        when(partidoMapper.toDomain(partidoEntity)).thenReturn(partido);

        assertThrows(BusinessRuleException.class,
                () -> teamLineupService.saveLineup(1L, buildRequest()));
    }

    @Test
    void saveLineup_SinFormacion_LanzaException() {
        UserEntity captainEntity = buildUserEntity(1L);
        User captain = buildUser(1L);
        EquipoEntity equipoEntity = new EquipoEntity();
        Equipo equipo = buildEquipo(1L, 1L);
        PartidoEntity partidoEntity = new PartidoEntity();
        Partido partido = buildPartido(1L, 2L);

        SaveLineupRequestDTO request = buildRequest();
        request.setFormation(null);

        when(userRepository.findById(1L)).thenReturn(Optional.of(captainEntity));
        when(userMapper.toDomain(captainEntity)).thenReturn(captain);
        when(equipoRepository.findById(1L)).thenReturn(Optional.of(equipoEntity));
        when(equipoMapper.toDomain(equipoEntity)).thenReturn(equipo);
        when(matchRepository.findById(1L)).thenReturn(Optional.of(partidoEntity));
        when(partidoMapper.toDomain(partidoEntity)).thenReturn(partido);
        when(lineupRepository.findByTeamIdAndMatchId(1L, 1L))
                .thenReturn(Optional.empty());

        assertThrows(BusinessRuleException.class,
                () -> teamLineupService.saveLineup(1L, request));
    }

    @Test
    void saveLineup_SinPosicionEnTitular_LanzaException() {
        UserEntity captainEntity = buildUserEntity(1L);
        User captain = buildUser(1L);
        EquipoEntity equipoEntity = new EquipoEntity();
        Equipo equipo = buildEquipo(1L, 1L);
        PartidoEntity partidoEntity = new PartidoEntity();
        Partido partido = buildPartido(1L, 2L);

        StarterEntryRequestDTO starterSinPosicion =
                new StarterEntryRequestDTO(1L, null);
        SaveLineupRequestDTO request = new SaveLineupRequestDTO();
        request.setTeamId(1L);
        request.setMatchId(1L);
        request.setFormation(TacticalFormation.F_1_2_3_1);
        request.setStarters(List.of(starterSinPosicion, starterSinPosicion,
                starterSinPosicion, starterSinPosicion, starterSinPosicion,
                starterSinPosicion, starterSinPosicion));
        request.setReserveIds(List.of());

        when(userRepository.findById(1L)).thenReturn(Optional.of(captainEntity));
        when(userMapper.toDomain(captainEntity)).thenReturn(captain);
        when(equipoRepository.findById(1L)).thenReturn(Optional.of(equipoEntity));
        when(equipoMapper.toDomain(equipoEntity)).thenReturn(equipo);
        when(matchRepository.findById(1L)).thenReturn(Optional.of(partidoEntity));
        when(partidoMapper.toDomain(partidoEntity)).thenReturn(partido);
        when(lineupRepository.findByTeamIdAndMatchId(1L, 1L))
                .thenReturn(Optional.empty());

        assertThrows(BusinessRuleException.class,
                () -> teamLineupService.saveLineup(1L, request));
    }

    @Test
    void saveLineup_MenosDe7Titulares_LanzaException() {
        UserEntity captainEntity = buildUserEntity(1L);
        User captain = buildUser(1L);
        EquipoEntity equipoEntity = new EquipoEntity();
        Equipo equipo = buildEquipo(1L, 1L);
        PartidoEntity partidoEntity = new PartidoEntity();
        Partido partido = buildPartido(1L, 2L);

        SaveLineupRequestDTO request = new SaveLineupRequestDTO();
        request.setTeamId(1L);
        request.setMatchId(1L);
        request.setFormation(TacticalFormation.F_1_2_3_1);
        request.setStarters(List.of(
                new StarterEntryRequestDTO(1L, FieldPosition.FORWARD)));
        request.setReserveIds(List.of());

        when(userRepository.findById(1L)).thenReturn(Optional.of(captainEntity));
        when(userMapper.toDomain(captainEntity)).thenReturn(captain);
        when(equipoRepository.findById(1L)).thenReturn(Optional.of(equipoEntity));
        when(equipoMapper.toDomain(equipoEntity)).thenReturn(equipo);
        when(matchRepository.findById(1L)).thenReturn(Optional.of(partidoEntity));
        when(partidoMapper.toDomain(partidoEntity)).thenReturn(partido);
        when(lineupRepository.findByTeamIdAndMatchId(1L, 1L))
                .thenReturn(Optional.empty());

        assertThrows(BusinessRuleException.class,
                () -> teamLineupService.saveLineup(1L, request));
    }

    @Test
    void updateLineup_AlineacionBloqueada_LanzaException() {
        UserEntity captainEntity = buildUserEntity(1L);
        User captain = buildUser(1L);
        EquipoEntity equipoEntity = new EquipoEntity();
        Equipo equipo = buildEquipo(1L, 1L);
        TeamLineup locked = TeamLineup.builder().id(1L)
                .teamId(1L).matchId(1L).status(LineupStatus.LOCKED)
                .starters(List.of()).reserveIds(List.of()).build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(captainEntity));
        when(userMapper.toDomain(captainEntity)).thenReturn(captain);
        when(lineupRepository.findById(1L)).thenReturn(Optional.of(locked));
        when(equipoRepository.findById(1L)).thenReturn(Optional.of(equipoEntity));
        when(equipoMapper.toDomain(equipoEntity)).thenReturn(equipo);

        assertThrows(BusinessRuleException.class,
                () -> teamLineupService.updateLineup(1L, 1L, buildRequest()));
    }

    @Test
    void saveLineup_EquipoVisitanteParticipa_HappyPath() {
        UserEntity captainEntity = buildUserEntity(2L);
        User captain = buildUser(2L);
        EquipoEntity equipoEntity = new EquipoEntity();
        Equipo equipo = buildEquipo(2L, 2L);
        PartidoEntity partidoEntity = new PartidoEntity();
        Partido partido = buildPartido(1L, 2L);
        TeamLineup lineup = TeamLineup.builder().id(1L)
                .teamId(2L).matchId(1L).status(LineupStatus.SAVED)
                .starters(List.of()).reserveIds(List.of()).build();
        TeamLineupResponseDTO dto = TeamLineupResponseDTO.builder()
                .lineupId(1L).build();

        SaveLineupRequestDTO request = buildRequest();
        request.setTeamId(2L);

        when(userRepository.findById(2L)).thenReturn(Optional.of(captainEntity));
        when(userMapper.toDomain(captainEntity)).thenReturn(captain);
        when(equipoRepository.findById(2L)).thenReturn(Optional.of(equipoEntity));
        when(equipoMapper.toDomain(equipoEntity)).thenReturn(equipo);
        when(matchRepository.findById(1L)).thenReturn(Optional.of(partidoEntity));
        when(partidoMapper.toDomain(partidoEntity)).thenReturn(partido);
        when(lineupRepository.findByTeamIdAndMatchId(2L, 1L))
                .thenReturn(Optional.empty());
        when(lineupMapper.toNewTeamLineup(any(), any(), any())).thenReturn(lineup);
        when(lineupMapper.toResponseDTO(any(), any())).thenReturn(dto);

        TeamLineupResponseDTO result =
                teamLineupService.saveLineup(2L, request);

        assertNotNull(result);
    }

}