package edu.dosw.proyect.services;

import edu.dosw.proyect.controllers.dtos.request.MatchEventRequestDTO;
import edu.dosw.proyect.controllers.dtos.request.RegisterMatchRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.RegisterMatchResponseDTO;
import edu.dosw.proyect.core.exceptions.BusinessRuleException;
import edu.dosw.proyect.core.exceptions.ResourceNotFoundException;
import edu.dosw.proyect.core.models.enums.MatchStatus;
import edu.dosw.proyect.core.models.enums.TipoEvento;
import edu.dosw.proyect.core.services.impl.MatchRegistrationServiceImpl;
import edu.dosw.proyect.persistence.entity.*;
import edu.dosw.proyect.persistence.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MatchRegistrationServiceImplTest {

    @Mock private PartidoRepository matchRepository;
    @Mock private JugadorRepository playerRepository;
    @Mock private EquipoRepository teamRepository;
    @Mock private EventoPartidoRepository eventRepository;
    @Mock private EstadisticaEquipoRepository statsRepository;

    @InjectMocks
    private MatchRegistrationServiceImpl matchRegistrationService;

    private PartidoEntity matchEntity;
    private EquipoEntity localTeam;
    private EquipoEntity awayTeam;
    private TournamentEntity tournament;

    @BeforeEach
    void setUp() {
        tournament = new TournamentEntity();
        tournament.setId(5L); // Es Long

        localTeam = new EquipoEntity();
        localTeam.setId(10L);
        localTeam.setNombre("Local FC");

        awayTeam = new EquipoEntity();
        awayTeam.setId(20L);
        awayTeam.setNombre("Away FC");

        matchEntity = new PartidoEntity();
        matchEntity.setId(100L);
        matchEntity.setEquipoLocal(localTeam);
        matchEntity.setEquipoVisitante(awayTeam);
        matchEntity.setTorneo(tournament);
        matchEntity.setEstado(MatchStatus.PROGRAMADO);
    }

    @Test
    void registerMatch_MatchNotFound() {
        when(matchRepository.findById(100L)).thenReturn(Optional.empty());
        
        RegisterMatchRequestDTO request = new RegisterMatchRequestDTO();
        assertThrows(ResourceNotFoundException.class, () -> matchRegistrationService.registerMatch(100L, request));
    }

    @Test
    void registerMatch_MatchCanceled() {
        matchEntity.setEstado(MatchStatus.CANCELADO);
        when(matchRepository.findById(100L)).thenReturn(Optional.of(matchEntity));
        
        RegisterMatchRequestDTO request = new RegisterMatchRequestDTO();
        assertThrows(BusinessRuleException.class, () -> matchRegistrationService.registerMatch(100L, request));
    }

    @Test
    void registerMatch_MatchAlreadyFinalized() {
        matchEntity.setEstado(MatchStatus.FINALIZADO);
        when(matchRepository.findById(100L)).thenReturn(Optional.of(matchEntity));
        
        RegisterMatchRequestDTO request = new RegisterMatchRequestDTO();
        assertThrows(BusinessRuleException.class, () -> matchRegistrationService.registerMatch(100L, request));
    }

    @Test
    void registerMatch_SuccessLocalWins_WithEvents() {
        when(matchRepository.findById(100L)).thenReturn(Optional.of(matchEntity));
        
        RegisterMatchRequestDTO request = new RegisterMatchRequestDTO();
        request.setHomeGoals(2);
        request.setAwayGoals(1);

        MatchEventRequestDTO goalEvent = new MatchEventRequestDTO();
        goalEvent.setEventType(TipoEvento.GOL);
        goalEvent.setPlayerId(1L);
        goalEvent.setTeamId(10L);
        goalEvent.setMinute(45);

        MatchEventRequestDTO yellowEvent = new MatchEventRequestDTO();
        yellowEvent.setEventType(TipoEvento.TARJETA_AMARILLA);
        yellowEvent.setPlayerId(2L);
        yellowEvent.setTeamId(20L);
        yellowEvent.setMinute(60);

        request.setEvents(List.of(goalEvent, yellowEvent));

        JugadorEntity player1 = new JugadorEntity(); player1.setId(1L);
        JugadorEntity player2 = new JugadorEntity(); player2.setId(2L);
        
        when(playerRepository.findById(1L)).thenReturn(Optional.of(player1));
        when(playerRepository.findById(2L)).thenReturn(Optional.of(player2));
        
        when(teamRepository.findById(10L)).thenReturn(Optional.of(localTeam));
        when(teamRepository.findById(20L)).thenReturn(Optional.of(awayTeam));

        when(statsRepository.findByEquipoIdAndTorneoId(10L, 5L)).thenReturn(Optional.empty());
        when(statsRepository.findByEquipoIdAndTorneoId(20L, 5L)).thenReturn(Optional.empty());

        RegisterMatchResponseDTO response = matchRegistrationService.registerMatch(100L, request);

        assertNotNull(response);
        assertEquals("LOCAL", response.getOutcome());
        assertEquals(2, response.getHomeGoals());
        assertEquals(1, response.getAwayGoals());
        assertEquals(1, response.getScorers().size());
        assertEquals(1, response.getYellowCardPlayers().size());
        
        verify(matchRepository).save(any());
        verify(eventRepository, times(2)).save(any());
        verify(statsRepository, times(2)).save(any());
    }

    @Test
    void registerMatch_SuccessAwayWins() {
        when(matchRepository.findById(100L)).thenReturn(Optional.of(matchEntity));
        
        RegisterMatchRequestDTO request = new RegisterMatchRequestDTO();
        request.setHomeGoals(0);
        request.setAwayGoals(3);

        when(statsRepository.findByEquipoIdAndTorneoId(10L, 5L)).thenReturn(Optional.empty());
        when(statsRepository.findByEquipoIdAndTorneoId(20L, 5L)).thenReturn(Optional.empty());

        RegisterMatchResponseDTO response = matchRegistrationService.registerMatch(100L, request);

        assertEquals("VISITANTE", response.getOutcome());
        verify(matchRepository).save(matchEntity);
    }

    @Test
    void registerMatch_SuccessDraw() {
        when(matchRepository.findById(100L)).thenReturn(Optional.of(matchEntity));
        
        RegisterMatchRequestDTO request = new RegisterMatchRequestDTO();
        request.setHomeGoals(1);
        request.setAwayGoals(1);

        when(statsRepository.findByEquipoIdAndTorneoId(10L, 5L)).thenReturn(Optional.empty());
        when(statsRepository.findByEquipoIdAndTorneoId(20L, 5L)).thenReturn(Optional.empty());

        RegisterMatchResponseDTO response = matchRegistrationService.registerMatch(100L, request);

        assertEquals("EMPATE", response.getOutcome());
        verify(matchRepository).save(matchEntity);
    }
    
    @Test
    void registerMatch_EventPlayerNotFound() {
        when(matchRepository.findById(100L)).thenReturn(Optional.of(matchEntity));
        
        RegisterMatchRequestDTO request = new RegisterMatchRequestDTO();
        request.setHomeGoals(0);
        request.setAwayGoals(0);

        MatchEventRequestDTO event = new MatchEventRequestDTO();
        event.setPlayerId(99L);
        request.setEvents(List.of(event));

        when(playerRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> matchRegistrationService.registerMatch(100L, request));
    }

    @Test
    void registerMatch_EventTeamNotParticipating() {
        when(matchRepository.findById(100L)).thenReturn(Optional.of(matchEntity));
        
        RegisterMatchRequestDTO request = new RegisterMatchRequestDTO();
        request.setHomeGoals(0);
        request.setAwayGoals(0);

        MatchEventRequestDTO event = new MatchEventRequestDTO();
        event.setPlayerId(1L);
        event.setTeamId(99L);
        request.setEvents(List.of(event));

        JugadorEntity player = new JugadorEntity(); player.setId(1L);
        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));
        
        EquipoEntity otherTeam = new EquipoEntity(); otherTeam.setId(99L);
        when(teamRepository.findById(99L)).thenReturn(Optional.of(otherTeam));

        assertThrows(BusinessRuleException.class, () -> matchRegistrationService.registerMatch(100L, request));
    }
}
