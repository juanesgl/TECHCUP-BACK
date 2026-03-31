package edu.dosw.proyect.services;

import edu.dosw.proyect.controllers.dtos.request.SaveLineupRequestDTO;
import edu.dosw.proyect.controllers.dtos.request.StarterEntryRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.TeamLineupResponseDTO;
import edu.dosw.proyect.controllers.mappers.TeamLineupMapper;
import edu.dosw.proyect.core.exceptions.BusinessRuleException;
import edu.dosw.proyect.core.exceptions.ResourceNotFoundException;
import edu.dosw.proyect.core.models.*;
import edu.dosw.proyect.core.models.enums.*;
import edu.dosw.proyect.persistence.repository.*;
import edu.dosw.proyect.core.services.impl.TeamLineupServiceImpl;
import edu.dosw.proyect.core.services.authorization.AuthorizationValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeamLineupServiceImplTest {

    @Mock private TeamLineupRepository lineupRepository;
    @Mock private EquipoRepository equipoRepository;
    @Mock private PartidoRepository matchRepository;
    @Mock private UserRepository userRepository;
    @Mock private TeamLineupMapper lineupMapper;
    @Mock private AuthorizationValidator authorizationValidator;

    @InjectMocks
    private TeamLineupServiceImpl lineupService;

    private User captain;
    private Equipo team;
    private Partido match;
    private SaveLineupRequestDTO request;
    private List<StarterEntryRequestDTO> starters;

    @BeforeEach
    void setUp() {
        captain = new User();
        captain.setId(1L);

        team = new Equipo();
        team.setId(10L);
        team.setNombre("Tech FC");

        Jugador capJugador = new Jugador();
        capJugador.setId(1L);
        team.setCapitan(capJugador);

        match = new Partido();
        match.setId(20L);
        match.setEquipoLocal(team);
        match.setEquipoVisitante(new Equipo()); // importante
        match.setEstado(MatchStatus.PROGRAMADO);

        starters = new ArrayList<>();
        for (long i = 1; i <= 7; i++) {
            starters.add(new StarterEntryRequestDTO(100L + i, FieldPosition.MIDFIELDER));
        }

        request = new SaveLineupRequestDTO();
        request.setTeamId(10L);
        request.setMatchId(20L);
        request.setFormation(TacticalFormation.F_1_2_3_1);
        request.setStarters(starters);
    }

    @Test
    void saveLineup_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(captain));
        when(equipoRepository.findById(10L)).thenReturn(Optional.of(team));
        when(matchRepository.findById(20L)).thenReturn(Optional.of(match));
        when(lineupRepository.findByTeamIdAndMatchId(10L, 20L)).thenReturn(Optional.empty());

        TeamLineup lineup = new TeamLineup();
        lineup.setId(50L);

        when(lineupMapper.toNewTeamLineup(any(), anyLong(), anyMap())).thenReturn(lineup);
        when(lineupMapper.toResponseDTO(any(), anyString())).thenReturn(new TeamLineupResponseDTO());

        TeamLineupResponseDTO response = lineupService.saveLineup(1L, request);

        assertNotNull(response);
        verify(lineupRepository).save(lineup);
        verify(authorizationValidator).validatePermission(captain, "MANAGE_LINEUP");
    }

    @Test
    void saveLineup_CaptainNotFound_ThrowsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> lineupService.saveLineup(1L, request));
    }

    @Test
    void saveLineup_TeamNotParticipating_ThrowsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(captain));
        when(equipoRepository.findById(10L)).thenReturn(Optional.of(team));

        Partido otherMatch = new Partido();
        otherMatch.setEquipoLocal(new Equipo());
        otherMatch.setEquipoVisitante(new Equipo());

        when(matchRepository.findById(20L)).thenReturn(Optional.of(otherMatch));

        assertThrows(BusinessRuleException.class,
                () -> lineupService.saveLineup(1L, request));
    }

    @Test
    void saveLineup_MatchNotScheduled_ThrowsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(captain));
        when(equipoRepository.findById(10L)).thenReturn(Optional.of(team));

        match.setEstado(MatchStatus.FINALIZADO);
        when(matchRepository.findById(20L)).thenReturn(Optional.of(match));

        assertThrows(BusinessRuleException.class,
                () -> lineupService.saveLineup(1L, request));
    }

    @Test
    void saveLineup_LineupAlreadyExists_ThrowsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(captain));
        when(equipoRepository.findById(10L)).thenReturn(Optional.of(team));
        when(matchRepository.findById(20L)).thenReturn(Optional.of(match));
        when(lineupRepository.findByTeamIdAndMatchId(10L, 20L))
                .thenReturn(Optional.of(new TeamLineup()));

        assertThrows(BusinessRuleException.class,
                () -> lineupService.saveLineup(1L, request));
    }

    @Test
    void saveLineup_InvalidStarterCount_ThrowsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(captain));
        when(equipoRepository.findById(10L)).thenReturn(Optional.of(team));
        when(matchRepository.findById(20L)).thenReturn(Optional.of(match));

        request.getStarters().remove(0);

        assertThrows(BusinessRuleException.class,
                () -> lineupService.saveLineup(1L, request));
    }

    @Test
    void saveLineup_MissingFormation_ThrowsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(captain));
        when(equipoRepository.findById(10L)).thenReturn(Optional.of(team));
        when(matchRepository.findById(20L)).thenReturn(Optional.of(match));

        request.setFormation(null);

        assertThrows(BusinessRuleException.class,
                () -> lineupService.saveLineup(1L, request));
    }

    @Test
    void saveLineup_MissingFieldPosition_ThrowsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(captain));
        when(equipoRepository.findById(10L)).thenReturn(Optional.of(team));
        when(matchRepository.findById(20L)).thenReturn(Optional.of(match));

        request.getStarters().get(0).setFieldPosition(null);

        assertThrows(BusinessRuleException.class,
                () -> lineupService.saveLineup(1L, request));
    }

    @Test
    void updateLineup_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(captain));

        TeamLineup existing = new TeamLineup();
        existing.setId(50L);
        existing.setTeamId(10L);
        existing.setMatchId(20L);
        existing.setStatus(LineupStatus.DRAFT);

        when(lineupRepository.findById(50L)).thenReturn(Optional.of(existing));
        when(equipoRepository.findById(10L)).thenReturn(Optional.of(team));
        when(matchRepository.findById(20L)).thenReturn(Optional.of(match));
        when(lineupMapper.toResponseDTO(any(), anyString()))
                .thenReturn(new TeamLineupResponseDTO());

        TeamLineupResponseDTO response =
                lineupService.updateLineup(1L, 50L, request);

        assertNotNull(response);
        verify(lineupRepository).save(existing);
    }

    @Test
    void updateLineup_Locked_ThrowsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(captain));

        TeamLineup existing = new TeamLineup();
        existing.setId(50L);
        existing.setTeamId(10L);
        existing.setStatus(LineupStatus.LOCKED);

        when(lineupRepository.findById(50L)).thenReturn(Optional.of(existing));

        assertThrows(BusinessRuleException.class,
                () -> lineupService.updateLineup(1L, 50L, request));
    }

    @Test
    void getLineup_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(captain));
        when(equipoRepository.findById(10L)).thenReturn(Optional.of(team));

        when(lineupRepository.findByTeamIdAndMatchId(10L, 20L))
                .thenReturn(Optional.of(new TeamLineup()));
        when(lineupMapper.toResponseDTO(any(), anyString()))
                .thenReturn(new TeamLineupResponseDTO());

        TeamLineupResponseDTO response =
                lineupService.getLineup(1L, 10L, 20L);

        assertNotNull(response);
    }

    @Test
    void getTeamLineups_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(captain));
        when(equipoRepository.findById(10L)).thenReturn(Optional.of(team));

        when(lineupRepository.findByTeamId(10L))
                .thenReturn(List.of(new TeamLineup()));
        when(lineupMapper.toResponseDTO(any(), anyString()))
                .thenReturn(new TeamLineupResponseDTO());

        List<TeamLineupResponseDTO> responses =
                lineupService.getTeamLineups(1L, 10L);

        assertEquals(1, responses.size());
    }
}