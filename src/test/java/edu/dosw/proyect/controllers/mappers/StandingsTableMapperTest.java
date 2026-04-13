package edu.dosw.proyect.controllers.mappers;

import edu.dosw.proyect.controllers.dtos.response.RegisterMatchResultResponseDTO;
import edu.dosw.proyect.controllers.dtos.response.StandingsTableResponseDTO;
import edu.dosw.proyect.controllers.dtos.response.TeamStandingDTO;
import edu.dosw.proyect.core.models.Equipo;
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
        TeamStandingDTO dto = mapper.toTeamStandingDTO(
                1, 10L, "Alpha FC",
                10, 6, 2, 2, 20, 10);

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
        TeamStandingDTO dto = mapper.toTeamStandingDTO(
                2, 2L, "Beta FC",
                5, 0, 5, 0, 5, 5);

        assertEquals(5, dto.getPoints());
        assertEquals(0, dto.getGoalDifference());
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
    void toRegisterMatchResultResponseDTO_LocalGana_OutcomeHome() {
        Equipo local = new Equipo();
        local.setNombre("Alpha");
        Equipo visitante = new Equipo();
        visitante.setNombre("Beta");

        Partido partido = new Partido();
        partido.setId(1L);
        partido.setEquipoLocal(local);
        partido.setEquipoVisitante(visitante);
        partido.setGolesLocal(3);
        partido.setGolesVisitante(1);

        RegisterMatchResultResponseDTO dto =
                mapper.toRegisterMatchResultResponseDTO(partido);

        assertEquals("HOME", dto.getOutcome());
        assertEquals("Alpha", dto.getHomeTeam());
        assertEquals("Beta", dto.getAwayTeam());
        assertEquals(3, dto.getHomeGoals());
        assertEquals(1, dto.getAwayGoals());
    }

    @Test
    void toRegisterMatchResultResponseDTO_VisitanteGana_OutcomeAway() {
        Equipo local = new Equipo();
        local.setNombre("Alpha");
        Equipo visitante = new Equipo();
        visitante.setNombre("Beta");

        Partido partido = new Partido();
        partido.setId(1L);
        partido.setEquipoLocal(local);
        partido.setEquipoVisitante(visitante);
        partido.setGolesLocal(0);
        partido.setGolesVisitante(2);

        RegisterMatchResultResponseDTO dto =
                mapper.toRegisterMatchResultResponseDTO(partido);

        assertEquals("AWAY", dto.getOutcome());
    }

    @Test
    void toRegisterMatchResultResponseDTO_Empate_OutcomeDraw() {
        Equipo local = new Equipo();
        local.setNombre("Alpha");
        Equipo visitante = new Equipo();
        visitante.setNombre("Beta");

        Partido partido = new Partido();
        partido.setId(1L);
        partido.setEquipoLocal(local);
        partido.setEquipoVisitante(visitante);
        partido.setGolesLocal(1);
        partido.setGolesVisitante(1);

        RegisterMatchResultResponseDTO dto =
                mapper.toRegisterMatchResultResponseDTO(partido);

        assertEquals("DRAW", dto.getOutcome());
    }

    @Test
    void toRegisterMatchResultResponseDTO_SinEquipos_RetornaUnknown() {
        Partido partido = new Partido();
        partido.setId(1L);
        partido.setGolesLocal(0);
        partido.setGolesVisitante(0);

        RegisterMatchResultResponseDTO dto =
                mapper.toRegisterMatchResultResponseDTO(partido);

        assertEquals("Unknown", dto.getHomeTeam());
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
}