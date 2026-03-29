package edu.dosw.proyect.services;

import edu.dosw.proyect.controllers.dtos.request.SaveLineupRequestDTO;
import edu.dosw.proyect.controllers.dtos.request.StarterEntryRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.TeamLineupResponseDTO;
import edu.dosw.proyect.controllers.mappers.TeamLineupMapper;
import edu.dosw.proyect.core.exceptions.BusinessRuleException;
import edu.dosw.proyect.core.exceptions.ResourceNotFoundException;
import edu.dosw.proyect.core.models.Equipo;
import edu.dosw.proyect.core.models.Partido;
import edu.dosw.proyect.core.models.TeamLineup;
import edu.dosw.proyect.core.models.User;
import edu.dosw.proyect.core.models.enums.FieldPosition;
import edu.dosw.proyect.core.models.enums.LineupStatus;
import edu.dosw.proyect.core.models.enums.MatchStatus;
import edu.dosw.proyect.core.models.enums.TacticalFormation;
import edu.dosw.proyect.core.repositories.EquipoRepository;
import edu.dosw.proyect.core.repositories.PartidoRepository;
import edu.dosw.proyect.core.repositories.TeamLineupRepository;
import edu.dosw.proyect.core.repositories.UserRepository;
import edu.dosw.proyect.core.services.impl.TeamLineupServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TeamLineupServiceImplTest {

    @Mock private TeamLineupRepository lineupRepository;
    @Mock private EquipoRepository     equipoRepository;
    @Mock private PartidoRepository    matchRepository;
    @Mock private UserRepository       userRepository;
    @Spy  private TeamLineupMapper     lineupMapper;

    @InjectMocks
    private TeamLineupServiceImpl service;

    // ─── fixtures ────────────────────────────────────────────────────
    private static final Long CAPTAIN_ID = 1L;
    private static final Long TEAM_ID    = 10L;
    private static final Long MATCH_ID   = 20L;
    private static final Long LINEUP_ID  = 100L;

    private User    captain;
    private Equipo  team;
    private Partido scheduledMatch;

    @BeforeEach
    void setUp() {
        captain = buildUser(CAPTAIN_ID, "Captain Test");

        team = Equipo.builder()
                .id(TEAM_ID)
                .nombre("Alpha FC")
                .capitan(captain)
                .build();

        scheduledMatch = buildMatch(MATCH_ID, TEAM_ID, null, MatchStatus.PROGRAMADO);
    }


    @Test
    void saveLineup_ValidRequest_SavesAndReturnsConfirmation() {
        SaveLineupRequestDTO request = validRequest();

        when(equipoRepository.findById(TEAM_ID)).thenReturn(Optional.of(team));
        when(matchRepository.findById(MATCH_ID)).thenReturn(Optional.of(scheduledMatch));
        when(lineupRepository.findByTeamIdAndMatchId(TEAM_ID, MATCH_ID))
                .thenReturn(Optional.empty());
        when(lineupRepository.save(any())).thenAnswer(inv -> {
            TeamLineup l = inv.getArgument(0);
            l.setId(LINEUP_ID);
            return l;
        });
        mockPlayersForStarters(request);

        TeamLineupResponseDTO resp = service.saveLineup(CAPTAIN_ID, request);

        assertNotNull(resp);
        assertEquals(TacticalFormation.F_1_2_3_1, resp.getFormation());
        assertEquals(LineupStatus.SAVED, resp.getStatus());
        assertEquals(7, resp.getStarters().size());
        assertTrue(resp.getMessage().contains("saved successfully"));
        verify(lineupRepository).save(any(TeamLineup.class));
    }

    @Test
    void saveLineup_WithReserves_IncludesReservesInResponse() {
        SaveLineupRequestDTO request = validRequest();
        request.setReserveIds(List.of(101L, 102L));

        when(equipoRepository.findById(TEAM_ID)).thenReturn(Optional.of(team));
        when(matchRepository.findById(MATCH_ID)).thenReturn(Optional.of(scheduledMatch));
        when(lineupRepository.findByTeamIdAndMatchId(TEAM_ID, MATCH_ID))
                .thenReturn(Optional.empty());
        when(lineupRepository.save(any())).thenAnswer(inv -> {
            TeamLineup l = inv.getArgument(0);
            l.setId(LINEUP_ID);
            return l;
        });
        mockPlayersForStarters(request);

        TeamLineupResponseDTO resp = service.saveLineup(CAPTAIN_ID, request);

        assertEquals(2, resp.getReserveIds().size());
    }

    @Test
    void saveLineup_NullReserves_DefaultsToEmptyList() {
        SaveLineupRequestDTO request = validRequest();
        request.setReserveIds(null);

        when(equipoRepository.findById(TEAM_ID)).thenReturn(Optional.of(team));
        when(matchRepository.findById(MATCH_ID)).thenReturn(Optional.of(scheduledMatch));
        when(lineupRepository.findByTeamIdAndMatchId(TEAM_ID, MATCH_ID))
                .thenReturn(Optional.empty());
        when(lineupRepository.save(any())).thenAnswer(inv -> {
            TeamLineup l = inv.getArgument(0);
            l.setId(LINEUP_ID);
            return l;
        });
        mockPlayersForStarters(request);

        TeamLineupResponseDTO resp = service.saveLineup(CAPTAIN_ID, request);

        assertNotNull(resp.getReserveIds());
        assertTrue(resp.getReserveIds().isEmpty());
    }


    @Test
    void saveLineup_NotCaptain_ThrowsBusinessRuleException() {
        when(equipoRepository.findById(TEAM_ID)).thenReturn(Optional.of(team));
        when(matchRepository.findById(MATCH_ID)).thenReturn(Optional.of(scheduledMatch));

        BusinessRuleException ex = assertThrows(BusinessRuleException.class,
                () -> service.saveLineup(99L, validRequest()));

        assertTrue(ex.getMessage().contains("captain"));
        verify(lineupRepository, never()).save(any());
    }

    @Test
    void saveLineup_TeamHasNoCaptain_ThrowsBusinessRuleException() {
        Equipo teamNoCaptain = Equipo.builder().id(TEAM_ID).nombre("Alpha FC")
                .capitan(null).build();

        when(equipoRepository.findById(TEAM_ID)).thenReturn(Optional.of(teamNoCaptain));
        when(matchRepository.findById(MATCH_ID)).thenReturn(Optional.of(scheduledMatch));

        assertThrows(BusinessRuleException.class,
                () -> service.saveLineup(CAPTAIN_ID, validRequest()));
    }


    @Test
    void saveLineup_MatchNotFound_ThrowsResourceNotFoundException() {
        when(equipoRepository.findById(TEAM_ID)).thenReturn(Optional.of(team));
        when(matchRepository.findById(MATCH_ID)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.saveLineup(CAPTAIN_ID, validRequest()));
    }

    @Test
    void saveLineup_MatchFinished_ThrowsBusinessRuleException() {
        Partido finished = buildMatch(MATCH_ID, TEAM_ID, null, MatchStatus.FINALIZADO);
        when(equipoRepository.findById(TEAM_ID)).thenReturn(Optional.of(team));
        when(matchRepository.findById(MATCH_ID)).thenReturn(Optional.of(finished));

        BusinessRuleException ex = assertThrows(BusinessRuleException.class,
                () -> service.saveLineup(CAPTAIN_ID, validRequest()));

        assertTrue(ex.getMessage().contains("scheduled matches"));
    }

    @Test
    void saveLineup_MatchCancelled_ThrowsBusinessRuleException() {
        Partido cancelled = buildMatch(MATCH_ID, TEAM_ID, null, MatchStatus.CANCELADO);
        when(equipoRepository.findById(TEAM_ID)).thenReturn(Optional.of(team));
        when(matchRepository.findById(MATCH_ID)).thenReturn(Optional.of(cancelled));

        assertThrows(BusinessRuleException.class,
                () -> service.saveLineup(CAPTAIN_ID, validRequest()));
    }

    @Test
    void saveLineup_TeamNotInMatch_ThrowsBusinessRuleException() {
        Partido unrelatedMatch = buildMatch(MATCH_ID, 999L, 998L, MatchStatus.PROGRAMADO);
        when(equipoRepository.findById(TEAM_ID)).thenReturn(Optional.of(team));
        when(matchRepository.findById(MATCH_ID)).thenReturn(Optional.of(unrelatedMatch));

        BusinessRuleException ex = assertThrows(BusinessRuleException.class,
                () -> service.saveLineup(CAPTAIN_ID, validRequest()));

        assertTrue(ex.getMessage().contains("not participating"));
    }

    @Test
    void saveLineup_TeamAsVisitor_IsAccepted() {
        Partido matchAsVisitor = buildMatch(MATCH_ID, 999L, TEAM_ID, MatchStatus.PROGRAMADO);
        when(equipoRepository.findById(TEAM_ID)).thenReturn(Optional.of(team));
        when(matchRepository.findById(MATCH_ID)).thenReturn(Optional.of(matchAsVisitor));
        when(lineupRepository.findByTeamIdAndMatchId(any(), any()))
                .thenReturn(Optional.empty());
        when(lineupRepository.save(any())).thenAnswer(inv -> {
            TeamLineup l = inv.getArgument(0); l.setId(LINEUP_ID); return l;
        });
        mockPlayersForStarters(validRequest());

        assertDoesNotThrow(() -> service.saveLineup(CAPTAIN_ID, validRequest()));
    }

    @Test
    void saveLineup_LineupAlreadyExists_ThrowsBusinessRuleException() {
        TeamLineup existing = buildSavedLineup();
        when(equipoRepository.findById(TEAM_ID)).thenReturn(Optional.of(team));
        when(matchRepository.findById(MATCH_ID)).thenReturn(Optional.of(scheduledMatch));
        when(lineupRepository.findByTeamIdAndMatchId(TEAM_ID, MATCH_ID))
                .thenReturn(Optional.of(existing));

        BusinessRuleException ex = assertThrows(BusinessRuleException.class,
                () -> service.saveLineup(CAPTAIN_ID, validRequest()));

        assertTrue(ex.getMessage().contains("already exists"));
    }


    @Test
    void saveLineup_OnlySixStarters_ThrowsBusinessRuleException() {
        SaveLineupRequestDTO request = requestWithStarterCount(6);
        when(equipoRepository.findById(TEAM_ID)).thenReturn(Optional.of(team));
        when(matchRepository.findById(MATCH_ID)).thenReturn(Optional.of(scheduledMatch));

        BusinessRuleException ex = assertThrows(BusinessRuleException.class,
                () -> service.saveLineup(CAPTAIN_ID, request));

        assertTrue(ex.getMessage().contains("7 starter players"));
    }

    @Test
    void saveLineup_EightStarters_ThrowsBusinessRuleException() {
        SaveLineupRequestDTO request = requestWithStarterCount(8);
        when(equipoRepository.findById(TEAM_ID)).thenReturn(Optional.of(team));
        when(matchRepository.findById(MATCH_ID)).thenReturn(Optional.of(scheduledMatch));

        BusinessRuleException ex = assertThrows(BusinessRuleException.class,
                () -> service.saveLineup(CAPTAIN_ID, request));

        assertTrue(ex.getMessage().contains("7 starter players"));
    }

    @Test
    void saveLineup_NullStarters_ThrowsBusinessRuleException() {
        SaveLineupRequestDTO request = validRequest();
        request.setStarters(null);
        when(equipoRepository.findById(TEAM_ID)).thenReturn(Optional.of(team));
        when(matchRepository.findById(MATCH_ID)).thenReturn(Optional.of(scheduledMatch));

        assertThrows(BusinessRuleException.class,
                () -> service.saveLineup(CAPTAIN_ID, request));
    }


    @Test
    void saveLineup_NoFormation_ThrowsBusinessRuleException() {
        SaveLineupRequestDTO request = validRequest();
        request.setFormation(null);
        when(equipoRepository.findById(TEAM_ID)).thenReturn(Optional.of(team));
        when(matchRepository.findById(MATCH_ID)).thenReturn(Optional.of(scheduledMatch));

        BusinessRuleException ex = assertThrows(BusinessRuleException.class,
                () -> service.saveLineup(CAPTAIN_ID, request));

        assertTrue(ex.getMessage().contains("formation"));
    }


    @Test
    void saveLineup_StarterMissingPosition_ThrowsBusinessRuleException() {
        SaveLineupRequestDTO request = validRequest();
        request.getStarters().get(0).setFieldPosition(null);
        when(equipoRepository.findById(TEAM_ID)).thenReturn(Optional.of(team));
        when(matchRepository.findById(MATCH_ID)).thenReturn(Optional.of(scheduledMatch));

        BusinessRuleException ex = assertThrows(BusinessRuleException.class,
                () -> service.saveLineup(CAPTAIN_ID, request));

        assertTrue(ex.getMessage().contains("field position"));
    }


    @Test
    void updateLineup_ValidRequest_UpdatesSuccessfully() {
        TeamLineup existing = buildSavedLineup();
        when(lineupRepository.findById(LINEUP_ID)).thenReturn(Optional.of(existing));
        when(equipoRepository.findById(TEAM_ID)).thenReturn(Optional.of(team));
        when(matchRepository.findById(MATCH_ID)).thenReturn(Optional.of(scheduledMatch));
        when(lineupRepository.save(any())).thenReturn(existing);
        mockPlayersForStarters(validRequest());

        TeamLineupResponseDTO resp =
                service.updateLineup(CAPTAIN_ID, LINEUP_ID, validRequest());

        assertNotNull(resp);
        assertTrue(resp.getMessage().contains("updated successfully"));
        verify(lineupRepository).save(existing);
    }

    @Test
    void updateLineup_LineupNotFound_ThrowsResourceNotFoundException() {
        when(lineupRepository.findById(LINEUP_ID)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.updateLineup(CAPTAIN_ID, LINEUP_ID, validRequest()));
    }

    @Test
    void updateLineup_LockedLineup_ThrowsBusinessRuleException() {
        TeamLineup locked = buildSavedLineup();
        locked.setStatus(LineupStatus.LOCKED);
        when(lineupRepository.findById(LINEUP_ID)).thenReturn(Optional.of(locked));
        when(equipoRepository.findById(TEAM_ID)).thenReturn(Optional.of(team));

        BusinessRuleException ex = assertThrows(BusinessRuleException.class,
                () -> service.updateLineup(CAPTAIN_ID, LINEUP_ID, validRequest()));

        assertTrue(ex.getMessage().contains("already started"));
        verify(lineupRepository, never()).save(any());
    }

    @Test
    void updateLineup_NotCaptain_ThrowsBusinessRuleException() {
        TeamLineup existing = buildSavedLineup();
        when(lineupRepository.findById(LINEUP_ID)).thenReturn(Optional.of(existing));
        when(equipoRepository.findById(TEAM_ID)).thenReturn(Optional.of(team));

        assertThrows(BusinessRuleException.class,
                () -> service.updateLineup(99L, LINEUP_ID, validRequest()));
    }

    @Test
    void getLineup_ExistingLineup_ReturnsCorrectly() {
        TeamLineup lineup = buildSavedLineup();
        when(equipoRepository.findById(TEAM_ID)).thenReturn(Optional.of(team));
        when(lineupRepository.findByTeamIdAndMatchId(TEAM_ID, MATCH_ID))
                .thenReturn(Optional.of(lineup));

        TeamLineupResponseDTO resp =
                service.getLineup(CAPTAIN_ID, TEAM_ID, MATCH_ID);

        assertNotNull(resp);
        assertEquals(TEAM_ID,  resp.getTeamId());
        assertEquals(MATCH_ID, resp.getMatchId());
    }

    @Test
    void getLineup_NoLineupExists_ThrowsResourceNotFoundWithMessage() {
        when(equipoRepository.findById(TEAM_ID)).thenReturn(Optional.of(team));
        when(lineupRepository.findByTeamIdAndMatchId(TEAM_ID, MATCH_ID))
                .thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> service.getLineup(CAPTAIN_ID, TEAM_ID, MATCH_ID));

        assertTrue(ex.getMessage().contains("no scheduled matches"));
    }

    @Test
    void getLineup_TeamNotFound_ThrowsResourceNotFoundException() {
        when(equipoRepository.findById(TEAM_ID)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.getLineup(CAPTAIN_ID, TEAM_ID, MATCH_ID));
    }

    @Test
    void getTeamLineups_NoLineups_ReturnsEmptyList() {
        when(equipoRepository.findById(TEAM_ID)).thenReturn(Optional.of(team));
        when(lineupRepository.findByTeamId(TEAM_ID)).thenReturn(Collections.emptyList());

        List<TeamLineupResponseDTO> result =
                service.getTeamLineups(CAPTAIN_ID, TEAM_ID);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getTeamLineups_WithLineups_ReturnsAll() {
        TeamLineup l1 = buildSavedLineup();
        TeamLineup l2 = buildSavedLineup();
        l2.setId(200L);

        when(equipoRepository.findById(TEAM_ID)).thenReturn(Optional.of(team));
        when(lineupRepository.findByTeamId(TEAM_ID)).thenReturn(List.of(l1, l2));

        List<TeamLineupResponseDTO> result =
                service.getTeamLineups(CAPTAIN_ID, TEAM_ID);

        assertEquals(2, result.size());
    }

    @Test
    void getTeamLineups_NotCaptain_ThrowsBusinessRuleException() {
        when(equipoRepository.findById(TEAM_ID)).thenReturn(Optional.of(team));

        assertThrows(BusinessRuleException.class,
                () -> service.getTeamLineups(99L, TEAM_ID));
    }

    // ═══════════════════════════════════════════════════════════════
    // FORMATION DISPLAY NAME
    // ═══════════════════════════════════════════════════════════════

    @Test
    void saveLineup_FormationDisplayNameIncludedInResponse() {
        SaveLineupRequestDTO request = validRequest();
        when(equipoRepository.findById(TEAM_ID)).thenReturn(Optional.of(team));
        when(matchRepository.findById(MATCH_ID)).thenReturn(Optional.of(scheduledMatch));
        when(lineupRepository.findByTeamIdAndMatchId(any(), any()))
                .thenReturn(Optional.empty());
        when(lineupRepository.save(any())).thenAnswer(inv -> {
            TeamLineup l = inv.getArgument(0); l.setId(LINEUP_ID); return l;
        });
        mockPlayersForStarters(request);

        TeamLineupResponseDTO resp = service.saveLineup(CAPTAIN_ID, request);

        assertEquals("1-2-3-1", resp.getFormationDisplay());
    }


    // ═══════════════════════════════════════════════════════════════
    // HELPERS
    // ═══════════════════════════════════════════════════════════════

    private SaveLineupRequestDTO validRequest() {
        SaveLineupRequestDTO req = new SaveLineupRequestDTO();
        req.setTeamId(TEAM_ID);
        req.setMatchId(MATCH_ID);
        req.setFormation(TacticalFormation.F_1_2_3_1);
        req.setStarters(buildSevenStarters());
        req.setReserveIds(new ArrayList<>());
        return req;
    }

    private SaveLineupRequestDTO requestWithStarterCount(int count) {
        SaveLineupRequestDTO req = validRequest();
        List<StarterEntryRequestDTO> starters = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            starters.add(new StarterEntryRequestDTO((long) i, FieldPosition.MIDFIELDER));
        }
        req.setStarters(starters);
        return req;
    }

    private List<StarterEntryRequestDTO> buildSevenStarters() {
        FieldPosition[] positions = {
                FieldPosition.GOALKEEPER,
                FieldPosition.DEFENDER, FieldPosition.DEFENDER,
                FieldPosition.MIDFIELDER, FieldPosition.MIDFIELDER, FieldPosition.MIDFIELDER,
                FieldPosition.FORWARD
        };
        List<StarterEntryRequestDTO> list = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            list.add(new StarterEntryRequestDTO((long) (i + 1), positions[i]));
        }
        return list;
    }

    private TeamLineup buildSavedLineup() {
        return TeamLineup.builder()
                .id(LINEUP_ID)
                .teamId(TEAM_ID)
                .teamName("Alpha FC")
                .matchId(MATCH_ID)
                .captainId(CAPTAIN_ID)
                .formation(TacticalFormation.F_1_2_3_1)
                .status(LineupStatus.SAVED)
                .starters(new ArrayList<>())
                .reserveIds(new ArrayList<>())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    private Partido buildMatch(Long id, Long homeId, Long awayId, MatchStatus status) {
        Equipo home = homeId != null ? Equipo.builder().id(homeId).nombre("Home").build() : null;
        Equipo away = awayId != null ? Equipo.builder().id(awayId).nombre("Away").build() : null;
        Partido p = new Partido();
        p.setId(id);
        p.setEquipoLocal(home);
        p.setEquipoVisitante(away);
        p.setEstado(status);
        return p;
    }

    /**
     * Builds a concrete User instance (anonymous subclass) to avoid mocking
     * final methods on an abstract class.
     */
    private User buildUser(Long id, String name) {
        User u = new User() {};
        u.setId(id);
        u.setName(name);
        return u;
    }

    /**
     * Stubs userRepository.findById for each starter using real User objects.
     */
    private void mockPlayersForStarters(SaveLineupRequestDTO request) {
        request.getStarters().forEach(s ->
                when(userRepository.findById(s.getPlayerId()))
                        .thenReturn(Optional.of(buildUser(s.getPlayerId(), "Player " + s.getPlayerId()))));
    }
}