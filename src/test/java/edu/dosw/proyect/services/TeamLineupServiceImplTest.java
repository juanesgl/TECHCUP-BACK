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
import edu.dosw.proyect.persistence.repository.EquipoRepository;
import edu.dosw.proyect.persistence.repository.PartidoRepository;
import edu.dosw.proyect.persistence.repository.TeamLineupRepository;
import edu.dosw.proyect.persistence.repository.UserRepository;
import edu.dosw.proyect.core.services.impl.TeamLineupServiceImpl;
import edu.dosw.proyect.core.services.authorization.AuthorizationValidator;
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
    private TeamLineupRepository lineupRepository;
    @Mock
    private EquipoRepository equipoRepository;
    @Mock
    private PartidoRepository matchRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private TeamLineupMapper lineupMapper;
    @Mock
    private AuthorizationValidator authorizationValidator;

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

        assertThrows(ResourceNotFoundException.class, () -> lineupService.saveLineup(1L, request));
    }

    @Test
    void saveLineup_TeamNotParticipating_ThrowsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(captain));
        when(equipoRepository.findById(10L)).thenReturn(Optional.of(team));
        
        Partido otherMatch = new Partido();
        otherMatch.setId(30L);
        otherMatch.setEquipoLocal(new Equipo()); // Different team
        when(matchRepository.findById(20L)).thenReturn(Optional.of(otherMatch));

        BusinessRuleException ex = assertThrows(BusinessRuleException.class, () -> lineupService.saveLineup(1L, request));
        assertTrue(ex.getMessage().contains("participating"));
    }

    @Test
    void saveLineup_MatchNotScheduled_ThrowsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(captain));
        when(equipoRepository.findById(10L)).thenReturn(Optional.of(team));
        
        match.setEstado(MatchStatus.FINALIZADO);
        when(matchRepository.findById(20L)).thenReturn(Optional.of(match));

        BusinessRuleException ex = assertThrows(BusinessRuleException.class, () -> lineupService.saveLineup(1L, request));
        assertTrue(ex.getMessage().contains("scheduled"));
    }

    @Test
    void saveLineup_LineupAlreadyExists_ThrowsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(captain));
        when(equipoRepository.findById(10L)).thenReturn(Optional.of(team));
        when(matchRepository.findById(20L)).thenReturn(Optional.of(match));
        when(lineupRepository.findByTeamIdAndMatchId(10L, 20L)).thenReturn(Optional.of(new TeamLineup()));

        BusinessRuleException ex = assertThrows(BusinessRuleException.class, () -> lineupService.saveLineup(1L, request));
        assertTrue(ex.getMessage().contains("already exists"));
    }

    @Test
    void saveLineup_InvalidStarterCount_ThrowsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(captain));
        when(equipoRepository.findById(10L)).thenReturn(Optional.of(team));
        when(matchRepository.findById(20L)).thenReturn(Optional.of(match));
        
        request.getStarters().remove(0); // Only 6 starters

        BusinessRuleException ex = assertThrows(BusinessRuleException.class, () -> lineupService.saveLineup(1L, request));
        assertTrue(ex.getMessage().contains("exactly 7"));
    }

    @Test
    void saveLineup_MissingFormation_ThrowsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(captain));
        when(equipoRepository.findById(10L)).thenReturn(Optional.of(team));
        when(matchRepository.findById(20L)).thenReturn(Optional.of(match));
        
        request.setFormation(null);

        BusinessRuleException ex = assertThrows(BusinessRuleException.class, () -> lineupService.saveLineup(1L, request));
        assertTrue(ex.getMessage().contains("formation"));
    }

    @Test
    void saveLineup_MissingFieldPosition_ThrowsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(captain));
        when(equipoRepository.findById(10L)).thenReturn(Optional.of(team));
        when(matchRepository.findById(20L)).thenReturn(Optional.of(match));
        
        request.getStarters().get(0).setFieldPosition(null);

        BusinessRuleException ex = assertThrows(BusinessRuleException.class, () -> lineupService.saveLineup(1L, request));
        assertTrue(ex.getMessage().contains("field position"));
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
        when(lineupMapper.toResponseDTO(any(), anyString())).thenReturn(new TeamLineupResponseDTO());

        TeamLineupResponseDTO response = lineupService.updateLineup(1L, 50L, request);

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
        when(equipoRepository.findById(10L)).thenReturn(Optional.of(team));

        BusinessRuleException ex = assertThrows(BusinessRuleException.class, () -> lineupService.updateLineup(1L, 50L, request));
        assertTrue(ex.getMessage().contains("modified"));
    }

    @Test
    void getLineup_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(captain));
        when(equipoRepository.findById(10L)).thenReturn(Optional.of(team));
        
        TeamLineup lineup = new TeamLineup();
        when(lineupRepository.findByTeamIdAndMatchId(10L, 20L)).thenReturn(Optional.of(lineup));
        when(lineupMapper.toResponseDTO(any(), anyString())).thenReturn(new TeamLineupResponseDTO());

        TeamLineupResponseDTO response = lineupService.getLineup(1L, 10L, 20L);

        assertNotNull(response);
    }

    @Test
    void getTeamLineups_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(captain));
        when(equipoRepository.findById(10L)).thenReturn(Optional.of(team));
        
        when(lineupRepository.findByTeamId(10L)).thenReturn(List.of(new TeamLineup()));
        when(lineupMapper.toResponseDTO(any(), anyString())).thenReturn(new TeamLineupResponseDTO());

        List<TeamLineupResponseDTO> responses = lineupService.getTeamLineups(1L, 10L);

        assertEquals(1, responses.size());
    }
}
