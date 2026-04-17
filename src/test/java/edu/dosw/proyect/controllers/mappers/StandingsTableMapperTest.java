package edu.dosw.proyect.controllers.mappers;

import edu.dosw.proyect.controllers.dtos.response.RegisterMatchResultResponseDTO;
import edu.dosw.proyect.controllers.dtos.response.StandingsTableResponseDTO;
import edu.dosw.proyect.controllers.dtos.response.TeamStandingDTO;
import edu.dosw.proyect.core.models.Team;
import edu.dosw.proyect.core.models.Partido;
import edu.dosw.proyect.core.models.enums.MatchStatus;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StandingsTableMapperTest {

    private final StandingsTableMapper mapper = Mappers.getMapper(StandingsTableMapper.class);

    @Test
    void toTeamStandingDTO_HappyPath_MapeaCorrectamente() {
        TeamStandingDTO dto = mapper.toTeamStandingDTO(1, 10L, "Alpha FC", 10, 6, 2, 2, 20, 10);

        assertEquals(1, dto.getPosition());
        assertEquals(10L, dto.getTeamId());
        assertEquals("Alpha FC", dto.getTeamName());
        assertEquals(10, dto.getMatchesPlayed());
        assertEquals(6, dto.getWins());
        assertEquals(2, dto.getDraws());
        assertEquals(2, dto.getLosses());
        assertEquals(20, dto.getGoalsFor());
        assertEquals(10, dto.getGoalsAgainst());
        assertEquals(10, dto.getGoalDifference());
        assertEquals(20, dto.getPoints());
    }

    @Test
    void toTeamStandingDTO_Empates_PuntosCorrectos() {
        TeamStandingDTO dto = mapper.toTeamStandingDTO(2, 2L, "Beta FC", 5, 0, 5, 0, 5, 5);

        assertEquals(5, dto.getPoints());
        assertEquals(0, dto.getGoalDifference());
    }

    @Test
    void toTeamStandingDTO_SoloVictorias_PuntosTriples() {
        TeamStandingDTO dto = mapper.toTeamStandingDTO(1, 1L, "Gamma FC", 3, 3, 0, 0, 9, 0);

        assertEquals(9, dto.getPoints());
        assertEquals(9, dto.getGoalDifference());
    }

    @Test
    void toTeamStandingDTO_SoloDerrotas_PuntoCero() {
        TeamStandingDTO dto = mapper.toTeamStandingDTO(5, 5L, "Delta FC", 3, 0, 0, 3, 0, 6);

        assertEquals(0, dto.getPoints());
        assertEquals(-6, dto.getGoalDifference());
    }

    @Test
    void toStandingsTableResponseDTO_HappyPath_MapeaCorrectamente() {
        TeamStandingDTO standing = TeamStandingDTO.builder()
                .position(1).teamId(1L).teamName("Alpha").points(9).build();

        StandingsTableResponseDTO dto = mapper.toStandingsTableResponseDTO(
                "TOURN-1", "TechCup 2026", 5, List.of(standing));

        assertEquals("TOURN-1", dto.getTournamentId());
        assertEquals("TechCup 2026", dto.getTournamentName());
        assertEquals(1, dto.getTotalTeams());
        assertEquals(5, dto.getTotalMatchesPlayed());
        assertEquals(1, dto.getStandings().size());
    }

    @Test
    void toStandingsTableResponseDTO_ListaVacia_RetornaCero() {
        StandingsTableResponseDTO dto = mapper.toStandingsTableResponseDTO(
                "TOURN-1", "TechCup 2026", 0, List.of());

        assertEquals(0, dto.getTotalTeams());
        assertEquals(0, dto.getTotalMatchesPlayed());
        assertTrue(dto.getStandings().isEmpty());
    }

    @Test
    void toStandingsTableResponseDTO_VariosEquipos_ConteoCorecto() {
        List<TeamStandingDTO> standings = List.of(
                TeamStandingDTO.builder().position(1).teamId(1L).teamName("A").points(9).build(),
                TeamStandingDTO.builder().position(2).teamId(2L).teamName("B").points(6).build(),
                TeamStandingDTO.builder().position(3).teamId(3L).teamName("C").points(3).build()
        );

        StandingsTableResponseDTO dto = mapper.toStandingsTableResponseDTO(
                "TOURN-2", "Cup 2026", 10, standings);

        assertEquals(3, dto.getTotalTeams());
        assertEquals(10, dto.getTotalMatchesPlayed());
    }

    @Test
    void toRegisterMatchResultResponseDTO_LocalGana_OutcomeHome() {
        Partido partido = buildPartido("Alpha", "Beta", 3, 1);

        RegisterMatchResultResponseDTO dto = mapper.toRegisterMatchResultResponseDTO(partido);

        assertEquals("HOME", dto.getOutcome());
        assertEquals("Alpha", dto.getHomeTeam());
        assertEquals("Beta", dto.getAwayTeam());
        assertEquals(3, dto.getHomeGoals());
        assertEquals(1, dto.getAwayGoals());
        assertNotNull(dto.getMessage());
    }

    @Test
    void toRegisterMatchResultResponseDTO_VisitanteGana_OutcomeAway() {
        Partido partido = buildPartido("Alpha", "Beta", 0, 2);

        RegisterMatchResultResponseDTO dto = mapper.toRegisterMatchResultResponseDTO(partido);

        assertEquals("AWAY", dto.getOutcome());
    }

    @Test
    void toRegisterMatchResultResponseDTO_Empate_OutcomeDraw() {
        Partido partido = buildPartido("Alpha", "Beta", 1, 1);

        RegisterMatchResultResponseDTO dto = mapper.toRegisterMatchResultResponseDTO(partido);

        assertEquals("DRAW", dto.getOutcome());
    }

    @Test
    void toRegisterMatchResultResponseDTO_SinEquipos_RetornaUnknown() {
        Partido partido = new Partido();
        partido.setId(1L);
        partido.setGolesLocal(0);
        partido.setGolesVisitante(0);

        RegisterMatchResultResponseDTO dto = mapper.toRegisterMatchResultResponseDTO(partido);

        assertEquals("Unknown", dto.getHomeTeam());
        assertEquals("Unknown", dto.getAwayTeam());
    }

    @Test
    void toRegisterMatchResultResponseDTO_SoloLocalNull_AwayNormal() {
        Team visitante = new Team();
        visitante.setNombre("Beta");

        Partido partido = new Partido();
        partido.setId(1L);
        partido.setTeamLocal(null);
        partido.setTeamVisitante(visitante);
        partido.setGolesLocal(0);
        partido.setGolesVisitante(1);

        RegisterMatchResultResponseDTO dto = mapper.toRegisterMatchResultResponseDTO(partido);

        assertEquals("Unknown", dto.getHomeTeam());
        assertEquals("Beta", dto.getAwayTeam());
    }

    @Test
    void toRegisterMatchResultResponseDTO_SoloVisitanteNull_HomeNormal() {
        Team local = new Team();
        local.setNombre("Alpha");

        Partido partido = new Partido();
        partido.setId(1L);
        partido.setTeamLocal(local);
        partido.setTeamVisitante(null);
        partido.setGolesLocal(2);
        partido.setGolesVisitante(0);

        RegisterMatchResultResponseDTO dto = mapper.toRegisterMatchResultResponseDTO(partido);

        assertEquals("Alpha", dto.getHomeTeam());
        assertEquals("Unknown", dto.getAwayTeam());
    }

    @Test
    void isMatchCountable_Finalizado_RetornaTrue() {
        Partido p = new Partido();
        p.setEstado(MatchStatus.FINALIZADO);
        assertTrue(mapper.isMatchCountable(p));
    }

    @Test
    void isMatchCountable_EnJuego_RetornaTrue() {
        Partido p = new Partido();
        p.setEstado(MatchStatus.EN_JUEGO);
        assertTrue(mapper.isMatchCountable(p));
    }

    @Test
    void isMatchCountable_Programado_RetornaFalse() {
        Partido p = new Partido();
        p.setEstado(MatchStatus.PROGRAMADO);
        assertFalse(mapper.isMatchCountable(p));
    }

    @Test
    void isMatchCountable_Cancelado_RetornaFalse() {
        Partido p = new Partido();
        p.setEstado(MatchStatus.CANCELADO);
        assertFalse(mapper.isMatchCountable(p));
    }

    private Partido buildPartido(String localNombre, String visitanteNombre, int golesLocal, int golesVisitante) {
        Team local = new Team();
        local.setNombre(localNombre);
        Team visitante = new Team();
        visitante.setNombre(visitanteNombre);

        Partido partido = new Partido();
        partido.setId(1L);
        partido.setTeamLocal(local);
        partido.setTeamVisitante(visitante);
        partido.setGolesLocal(golesLocal);
        partido.setGolesVisitante(golesVisitante);
        return partido;
    }
}