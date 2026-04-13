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
import edu.dosw.proyect.core.models.enums.TacticalFormation;
import edu.dosw.proyect.core.models.enums.MatchStatus;
import edu.dosw.proyect.core.models.enums.FieldPosition;
import edu.dosw.proyect.core.services.authorization.AuthorizationValidator;
import edu.dosw.proyect.core.services.impl.TeamLineupServiceImpl;
import edu.dosw.proyect.persistence.entity.EquipoEntity;
import edu.dosw.proyect.persistence.entity.PartidoEntity;
import edu.dosw.proyect.persistence.entity.TeamLineupEntity;
import edu.dosw.proyect.persistence.entity.UserEntity;
import edu.dosw.proyect.persistence.mapper.EquipoPersistenceMapper;
import edu.dosw.proyect.persistence.mapper.PartidoPersistenceMapper;
import edu.dosw.proyect.persistence.mapper.TeamLineupPersistenceMapper;
import edu.dosw.proyect.persistence.mapper.UserPersistenceMapper;
import edu.dosw.proyect.persistence.repository.EquipoRepository;
import edu.dosw.proyect.persistence.repository.PartidoRepository;
import edu.dosw.proyect.persistence.repository.TeamLineupJpaRepository;
import edu.dosw.proyect.persistence.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeamLineupServiceImplTest {

    @Mock
    private TeamLineupJpaRepository lineupRepository;
    @Mock
    private EquipoRepository equipoRepository;
    @Mock
    private PartidoRepository matchRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private TeamLineupMapper lineupMapper;
    @Mock
    private TeamLineupPersistenceMapper lineupPersistenceMapper;
    @Mock
    private AuthorizationValidator authorizationValidator;
    @Mock
    private PartidoPersistenceMapper partidoMapper;
    @Mock
    private EquipoPersistenceMapper equipoMapper;
    @Mock
    private UserPersistenceMapper userMapper;

    @InjectMocks
    private TeamLineupServiceImpl service;

    private UserEntity captainEntity;
    private User captainUser;
    private Jugador captainJugador;
    private EquipoEntity equipoEntity;
    private Equipo equipo;
    private PartidoEntity partidoEntity;
    private Partido partido;

    @BeforeEach
    void setUp() {
        captainEntity = new UserEntity();
        captainEntity.setId(1L);
        captainUser = new User();
        captainUser.setId(1L);
        captainJugador = new Jugador();
        captainJugador.setId(1L);

        equipoEntity = new EquipoEntity();
        equipoEntity.setId(10L);
        equipo = new Equipo();
        equipo.setId(10L);
        equipo.setCapitan(captainJugador);
        equipo.setNombre("Los Leones");

        partidoEntity = new PartidoEntity();
        partidoEntity.setId(100L);
        partido = new Partido();
        partido.setId(100L);
        partido.setEquipoLocal(equipo);
        partido.setEstado(MatchStatus.PROGRAMADO);
    }

    private SaveLineupRequestDTO buildValidRequest() {
        SaveLineupRequestDTO dto = new SaveLineupRequestDTO();
        dto.setTeamId(10L);
        dto.setMatchId(100L);
        dto.setFormation(TacticalFormation.F_1_2_3_1);

        List<StarterEntryRequestDTO> starters = new ArrayList<>();
        for (int i = 1; i <= 7; i++) {
            StarterEntryRequestDTO s = new StarterEntryRequestDTO();
            s.setPlayerId((long) i);
            s.setFieldPosition(FieldPosition.DEFENDER); // Enum en vez de String
            starters.add(s);
        }
        dto.setStarters(starters);
        return dto;
    }

    @Test
    void saveLineup_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        SaveLineupRequestDTO request = new SaveLineupRequestDTO();

        assertThrows(ResourceNotFoundException.class, () -> service.saveLineup(1L, request));
    }

    @Test
    void saveLineup_NotCaptain() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(captainEntity));
        when(userMapper.toDomain(captainEntity)).thenReturn(captainUser);
        doNothing().when(authorizationValidator).validatePermission(captainUser, "MANAGE_LINEUP");

        equipo.setCapitan(new Jugador()); // Another valid Jugador
        when(equipoRepository.findById(10L)).thenReturn(Optional.of(equipoEntity));
        when(equipoMapper.toDomain(equipoEntity)).thenReturn(equipo);

        when(matchRepository.findById(100L)).thenReturn(Optional.of(partidoEntity));
        when(partidoMapper.toDomain(partidoEntity)).thenReturn(partido);

        SaveLineupRequestDTO request = buildValidRequest();

        assertThrows(BusinessRuleException.class, () -> service.saveLineup(1L, request));
    }

    @Test
    void saveLineup_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(captainEntity));
        when(userMapper.toDomain(captainEntity)).thenReturn(captainUser);
        doNothing().when(authorizationValidator).validatePermission(captainUser, "MANAGE_LINEUP");

        when(equipoRepository.findById(10L)).thenReturn(Optional.of(equipoEntity));
        when(equipoMapper.toDomain(equipoEntity)).thenReturn(equipo);

        when(matchRepository.findById(100L)).thenReturn(Optional.of(partidoEntity));
        when(partidoMapper.toDomain(partidoEntity)).thenReturn(partido);

        SaveLineupRequestDTO request = buildValidRequest();
        when(lineupRepository.findByTeamIdAndMatchId(10L, 100L)).thenReturn(Optional.empty());

        TeamLineup lineup = new TeamLineup();
        when(lineupMapper.toNewTeamLineup(eq(request), eq(1L), anyMap())).thenReturn(lineup);

        TeamLineupEntity savedEntity = new TeamLineupEntity();
        when(lineupPersistenceMapper.toEntity(lineup)).thenReturn(savedEntity);
        when(lineupRepository.save(savedEntity)).thenReturn(savedEntity);

        TeamLineup savedDomain = new TeamLineup();
        when(lineupPersistenceMapper.toDomain(savedEntity)).thenReturn(savedDomain);

        when(lineupMapper.toResponseDTO(eq(savedDomain), anyString())).thenReturn(new TeamLineupResponseDTO());

        TeamLineupResponseDTO response = service.saveLineup(1L, request);

        assertNotNull(response);
        verify(lineupRepository).save(any());
    }

    @Test
    void getLineup_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(captainEntity));
        when(userMapper.toDomain(captainEntity)).thenReturn(captainUser);

        when(equipoRepository.findById(10L)).thenReturn(Optional.of(equipoEntity));
        when(equipoMapper.toDomain(equipoEntity)).thenReturn(equipo);

        TeamLineupEntity lineupEntity = new TeamLineupEntity();
        when(lineupRepository.findByTeamIdAndMatchId(10L, 100L)).thenReturn(Optional.of(lineupEntity));

        when(lineupPersistenceMapper.toDomain(lineupEntity)).thenReturn(new TeamLineup());
        when(lineupMapper.toResponseDTO(any(), anyString())).thenReturn(new TeamLineupResponseDTO());

        TeamLineupResponseDTO resp = service.getLineup(1L, 10L, 100L);
        assertNotNull(resp);
    }
}