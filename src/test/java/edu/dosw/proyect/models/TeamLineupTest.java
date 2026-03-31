package edu.dosw.proyect.models;

import edu.dosw.proyect.core.models.StarterEntry;
import edu.dosw.proyect.core.models.TeamLineup;
import edu.dosw.proyect.core.models.enums.LineupStatus;
import edu.dosw.proyect.core.models.enums.TacticalFormation;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TeamLineupTest {
    @Test
    void teamLineup_Builder_TodosLosCampos() {
        StarterEntry starter = StarterEntry.builder()
                .playerId(1L).playerName("Juan").build();

        LocalDateTime now = LocalDateTime.now();
        TeamLineup lineup = TeamLineup.builder()
                .id(1L)
                .teamId(10L)
                .teamName("Alpha FC")
                .matchId(5L)
                .captainId(2L)
                .formation(TacticalFormation.F_1_2_3_1)
                .status(LineupStatus.SAVED)
                .createdAt(now)
                .updatedAt(now)
                .starters(List.of(starter))
                .reserveIds(List.of(8L, 9L))
                .build();

        assertEquals(1L, lineup.getId());
        assertEquals(10L, lineup.getTeamId());
        assertEquals("Alpha FC", lineup.getTeamName());
        assertEquals(5L, lineup.getMatchId());
        assertEquals(2L, lineup.getCaptainId());
        assertEquals(TacticalFormation.F_1_2_3_1, lineup.getFormation());
        assertEquals(LineupStatus.SAVED, lineup.getStatus());
        assertEquals(1, lineup.getStarters().size());
        assertEquals(2, lineup.getReserveIds().size());
        assertNotNull(lineup.getCreatedAt());
        assertNotNull(lineup.getUpdatedAt());
    }

    @Test
    void teamLineup_NoArgsConstructor_ListasInicializadas() {
        TeamLineup lineup = new TeamLineup();
        assertNull(lineup.getId());
        assertNotNull(lineup.getStarters());
        assertNotNull(lineup.getReserveIds());
        assertTrue(lineup.getStarters().isEmpty());
        assertTrue(lineup.getReserveIds().isEmpty());
    }

    @Test
    void teamLineup_Setters_FuncionanCorrectamente() {
        TeamLineup lineup = new TeamLineup();
        lineup.setId(1L);
        lineup.setTeamId(5L);
        lineup.setTeamName("Gamma FC");
        lineup.setMatchId(3L);
        lineup.setCaptainId(2L);
        lineup.setFormation(TacticalFormation.F_1_3_2_1);
        lineup.setStatus(LineupStatus.LOCKED);
        lineup.setCreatedAt(LocalDateTime.now());
        lineup.setUpdatedAt(LocalDateTime.now());
        lineup.setStarters(new ArrayList<>());
        lineup.setReserveIds(new ArrayList<>());

        assertEquals(1L, lineup.getId());
        assertEquals("Gamma FC", lineup.getTeamName());
        assertEquals(LineupStatus.LOCKED, lineup.getStatus());
    }
}
