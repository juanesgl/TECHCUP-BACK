package edu.dosw.proyect.controllers.mappers;

import edu.dosw.proyect.controllers.dtos.request.SaveLineupRequestDTO;
import edu.dosw.proyect.controllers.dtos.request.StarterEntryRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.StarterEntryResponseDTO;
import edu.dosw.proyect.controllers.dtos.response.TeamLineupResponseDTO;
import edu.dosw.proyect.core.models.StarterEntry;
import edu.dosw.proyect.core.models.TeamLineup;
import edu.dosw.proyect.core.models.User;
import edu.dosw.proyect.core.models.enums.FieldPosition;
import edu.dosw.proyect.core.models.enums.LineupStatus;
import edu.dosw.proyect.core.models.enums.TacticalFormation;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TeamLineupMapperTest {

    private final TeamLineupMapper mapper = Mappers.getMapper(TeamLineupMapper.class);

    private SaveLineupRequestDTO buildRequest() {
        StarterEntryRequestDTO starter = new StarterEntryRequestDTO(1L, FieldPosition.FORWARD);
        SaveLineupRequestDTO request = new SaveLineupRequestDTO();
        request.setTeamId(10L);
        request.setMatchId(5L);
        request.setFormation(TacticalFormation.F_1_2_3_1);
        request.setStarters(List.of(starter, starter, starter, starter, starter, starter, starter));
        request.setReserveIds(List.of(8L, 9L));
        return request;
    }

    private Map<Long, User> buildPlayerMap() {
        User user = User.builder().id(1L).name("Juan Test").email("juan@mail.com").password("pass").role("PLAYER").build();
        return Map.of(1L, user);
    }

    @Test
    void toNewTeamLineup_HappyPath_ConstruyeCorrectamente() {
        SaveLineupRequestDTO request = buildRequest();
        TeamLineup lineup = mapper.toNewTeamLineup(request, 2L, buildPlayerMap());

        assertNotNull(lineup);
        assertEquals(10L, lineup.getTeamId());
        assertEquals(5L, lineup.getMatchId());
        assertEquals(2L, lineup.getCaptainId());
        assertEquals(TacticalFormation.F_1_2_3_1, lineup.getFormation());
        assertEquals(LineupStatus.SAVED, lineup.getStatus());
        assertEquals(7, lineup.getStarters().size());
        assertEquals(2, lineup.getReserveIds().size());
        assertNotNull(lineup.getCreatedAt());
        assertNotNull(lineup.getUpdatedAt());
    }

    @Test
    void toNewTeamLineup_SinReservas_RetornaListaVacia() {
        SaveLineupRequestDTO request = buildRequest();
        request.setReserveIds(null);
        TeamLineup lineup = mapper.toNewTeamLineup(request, 2L, buildPlayerMap());

        assertNotNull(lineup.getReserveIds());
        assertTrue(lineup.getReserveIds().isEmpty());
    }

    @Test
    void toNewTeamLineup_JugadorEnMapa_AsignaNombreReal() {
        SaveLineupRequestDTO request = buildRequest();
        TeamLineup lineup = mapper.toNewTeamLineup(request, 2L, buildPlayerMap());

        assertEquals("Juan Test", lineup.getStarters().get(0).getPlayerName());
    }

    @Test
    void toNewTeamLineup_JugadorFueraDelMapa_AsignaUnknown() {
        SaveLineupRequestDTO request = buildRequest();
        TeamLineup lineup = mapper.toNewTeamLineup(request, 2L, Map.of());

        assertEquals("Unknown", lineup.getStarters().get(0).getPlayerName());
    }

    @Test
    void applyUpdate_ActualizaLineupCorrectamente() {
        TeamLineup existing = TeamLineup.builder()
                .teamId(10L).matchId(5L)
                .formation(TacticalFormation.F_1_3_2_1)
                .status(LineupStatus.SAVED)
                .starters(List.of()).reserveIds(List.of())
                .build();

        mapper.applyUpdate(existing, buildRequest(), buildPlayerMap());

        assertEquals(TacticalFormation.F_1_2_3_1, existing.getFormation());
        assertEquals(LineupStatus.SAVED, existing.getStatus());
        assertEquals(7, existing.getStarters().size());
        assertEquals(2, existing.getReserveIds().size());
        assertNotNull(existing.getUpdatedAt());
    }

    @Test
    void applyUpdate_SinReservas_RetornaListaVacia() {
        TeamLineup existing = TeamLineup.builder()
                .teamId(10L).matchId(5L)
                .formation(TacticalFormation.F_1_3_2_1)
                .status(LineupStatus.SAVED)
                .starters(List.of()).reserveIds(List.of(99L))
                .build();

        SaveLineupRequestDTO request = buildRequest();
        request.setReserveIds(null);
        mapper.applyUpdate(existing, request, buildPlayerMap());

        assertNotNull(existing.getReserveIds());
        assertTrue(existing.getReserveIds().isEmpty());
    }

    @Test
    void toResponseDTO_HappyPath_MapeaCorrectamente() {
        StarterEntry starter = StarterEntry.builder()
                .playerId(1L).playerName("Juan").fieldPosition(FieldPosition.FORWARD).build();

        TeamLineup lineup = TeamLineup.builder()
                .id(1L).teamId(10L).teamName("Alpha FC").matchId(5L)
                .formation(TacticalFormation.F_1_2_3_1)
                .status(LineupStatus.SAVED)
                .starters(List.of(starter)).reserveIds(List.of(8L))
                .updatedAt(LocalDateTime.now())
                .build();

        TeamLineupResponseDTO dto = mapper.toResponseDTO(lineup, "Alineacion guardada");

        assertNotNull(dto);
        assertEquals(1L, dto.getLineupId());
        assertEquals(10L, dto.getTeamId());
        assertEquals("Alpha FC", dto.getTeamName());
        assertEquals(TacticalFormation.F_1_2_3_1, dto.getFormation());
        assertNotNull(dto.getFormationDisplay());
        assertEquals(LineupStatus.SAVED, dto.getStatus());
        assertEquals(1, dto.getStarters().size());
        assertEquals("Alineacion guardada", dto.getMessage());
        assertNotNull(dto.getSavedAt());
    }

    @Test
    void toResponseDTO_FormationNull_FormationDisplayNull() {
        TeamLineup lineup = TeamLineup.builder()
                .id(2L).teamId(10L).matchId(5L)
                .formation(null)
                .status(LineupStatus.SAVED)
                .starters(List.of()).reserveIds(List.of())
                .build();

        TeamLineupResponseDTO dto = mapper.toResponseDTO(lineup, "msg");

        assertNull(dto.getFormation());
        assertNull(dto.getFormationDisplay());
    }

    @Test
    void toStarterEntry_MapeaPlayerId() {
        StarterEntryRequestDTO req = new StarterEntryRequestDTO(5L, FieldPosition.GOALKEEPER);
        StarterEntry entry = mapper.toStarterEntry(req);

        assertEquals(5L, entry.getPlayerId());
        assertEquals(FieldPosition.GOALKEEPER, entry.getFieldPosition());
        assertNull(entry.getPlayerName());
    }

    @Test
    void toStarterEntryResponseDTO_MapeaCorrectamente() {
        StarterEntry entry = StarterEntry.builder()
                .playerId(3L).playerName("Carlos").fieldPosition(FieldPosition.MIDFIELDER).build();

        StarterEntryResponseDTO dto = mapper.toStarterEntryResponseDTO(entry);

        assertEquals(3L, dto.getPlayerId());
        assertEquals("Carlos", dto.getPlayerName());
        assertEquals(FieldPosition.MIDFIELDER, dto.getFieldPosition());
    }
}