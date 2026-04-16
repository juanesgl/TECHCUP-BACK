package edu.dosw.proyect.persistence;

import edu.dosw.proyect.core.models.TeamLineup;
import edu.dosw.proyect.core.models.enums.LineupStatus;
import edu.dosw.proyect.core.models.enums.TacticalFormation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TeamLineupRepositoryTest {

    private TeamLineupRepository repository;

    @BeforeEach
    void setUp() {
        repository = new TeamLineupRepository();
    }

    private TeamLineup buildLineup(Long teamId, Long matchId) {
        return TeamLineup.builder()
                .teamId(teamId)
                .matchId(matchId)
                .teamName("Alpha FC")
                .formation(TacticalFormation.F_1_2_3_1)
                .status(LineupStatus.SAVED)
                .starters(List.of())
                .reserveIds(List.of())
                .build();
    }

    @Test
    void save_NuevoLineup_AsignaIdAutomaticamente() {
        TeamLineup lineup = buildLineup(1L, 1L);

        TeamLineup saved = repository.save(lineup);

        assertNotNull(saved.getId());
        assertEquals(1L, saved.getId());
    }

    @Test
    void save_LineupExistente_ActualizaSinCambiarId() {
        TeamLineup lineup = buildLineup(1L, 1L);
        repository.save(lineup);
        lineup.setTeamName("Beta FC");

        TeamLineup updated = repository.save(lineup);

        assertEquals("Beta FC", updated.getTeamName());
        assertEquals(1L, updated.getId());
    }

    @Test
    void findById_LineupExistente_RetornaLineup() {
        TeamLineup lineup = buildLineup(1L, 1L);
        repository.save(lineup);

        Optional<TeamLineup> result = repository.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    void findById_NoExiste_RetornaEmpty() {
        Optional<TeamLineup> result = repository.findById(999L);
        assertFalse(result.isPresent());
    }

    @Test
    void findByTeamIdAndMatchId_HappyPath_RetornaLineup() {
        TeamLineup lineup = buildLineup(10L, 5L);
        repository.save(lineup);

        Optional<TeamLineup> result =
                repository.findByTeamIdAndMatchId(10L, 5L);

        assertTrue(result.isPresent());
        assertEquals(10L, result.get().getTeamId());
        assertEquals(5L, result.get().getMatchId());
    }

    @Test
    void findByTeamIdAndMatchId_NoExiste_RetornaEmpty() {
        Optional<TeamLineup> result =
                repository.findByTeamIdAndMatchId(99L, 99L);
        assertFalse(result.isPresent());
    }

    @Test
    void findByTeamId_VariosLineups_RetornaLosDElEquipo() {
        repository.save(buildLineup(1L, 1L));
        repository.save(buildLineup(1L, 2L));
        repository.save(buildLineup(2L, 1L));

        List<TeamLineup> result = repository.findByTeamId(1L);

        assertEquals(2, result.size());
        result.forEach(l -> assertEquals(1L, l.getTeamId()));
    }

    @Test
    void findByTeamId_SinLineups_RetornaVacio() {
        List<TeamLineup> result = repository.findByTeamId(99L);
        assertTrue(result.isEmpty());
    }

    @Test
    void findAll_VariosLineups_RetornaTodos() {
        repository.save(buildLineup(1L, 1L));
        repository.save(buildLineup(2L, 2L));
        repository.save(buildLineup(3L, 3L));

        List<TeamLineup> result = repository.findAll();

        assertEquals(3, result.size());
    }

    @Test
    void findAll_Vacio_RetornaListaVacia() {
        List<TeamLineup> result = repository.findAll();
        assertTrue(result.isEmpty());
    }

    @Test
    void save_MultiplesLineups_AsignaIdsSecuenciales() {
        TeamLineup l1 = repository.save(buildLineup(1L, 1L));
        TeamLineup l2 = repository.save(buildLineup(2L, 2L));
        TeamLineup l3 = repository.save(buildLineup(3L, 3L));

        assertEquals(1L, l1.getId());
        assertEquals(2L, l2.getId());
        assertEquals(3L, l3.getId());
    }
}
