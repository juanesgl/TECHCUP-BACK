package edu.dosw.proyect.controllers.dtos.responses;

import edu.dosw.proyect.controllers.dtos.response.TeamStandingDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TeamStandingDTOTest {

    @Test
    void teamStandingDTO_Builder_ConstruyeCorrectamente() {
        TeamStandingDTO dto = TeamStandingDTO.builder()
                .position(1)
                .teamId(1L)
                .teamName("Alpha FC")
                .matchesPlayed(10)
                .wins(7)
                .draws(2)
                .losses(1)
                .goalsFor(20)
                .goalsAgainst(8)
                .goalDifference(12)
                .points(23)
                .build();

        assertEquals(1, dto.getPosition());
        assertEquals(1L, dto.getTeamId());
        assertEquals("Alpha FC", dto.getTeamName());
        assertEquals(10, dto.getMatchesPlayed());
        assertEquals(7, dto.getWins());
        assertEquals(2, dto.getDraws());
        assertEquals(1, dto.getLosses());
        assertEquals(20, dto.getGoalsFor());
        assertEquals(8, dto.getGoalsAgainst());
        assertEquals(12, dto.getGoalDifference());
        assertEquals(23, dto.getPoints());
    }

    @Test
    void teamStandingDTO_NoArgsConstructor_CreaVacio() {
        TeamStandingDTO dto = new TeamStandingDTO();
        assertNull(dto.getTeamId());
        assertNull(dto.getTeamName());
        assertEquals(0, dto.getPoints());
    }

    @Test
    void teamStandingDTO_Setters_FuncionanCorrectamente() {
        TeamStandingDTO dto = new TeamStandingDTO();
        dto.setPosition(2);
        dto.setTeamId(3L);
        dto.setTeamName("Beta FC");
        dto.setPoints(15);

        assertEquals(2, dto.getPosition());
        assertEquals(3L, dto.getTeamId());
        assertEquals("Beta FC", dto.getTeamName());
        assertEquals(15, dto.getPoints());
    }
}