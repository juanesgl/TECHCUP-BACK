package edu.dosw.proyect.controllers.dtos.responses;

import edu.dosw.proyect.controllers.dtos.response.StarterEntryResponseDTO;
import edu.dosw.proyect.controllers.dtos.response.TeamLineupResponseDTO;
import edu.dosw.proyect.core.models.enums.FieldPosition;
import edu.dosw.proyect.core.models.enums.LineupStatus;
import edu.dosw.proyect.core.models.enums.TacticalFormation;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TeamLineupResponseTest {

    @Test
    void teamLineupResponseDTO_Builder_ConstruyeCorrectamente() {
        StarterEntryResponseDTO starter = StarterEntryResponseDTO.builder()
                .playerId(1L)
                .playerName("Juan")
                .fieldPosition(FieldPosition.FORWARD)
                .build();

        TeamLineupResponseDTO dto = TeamLineupResponseDTO.builder()
                .lineupId(1L)
                .teamId(10L)
                .teamName("Alpha FC")
                .matchId(5L)
                .formation(TacticalFormation.F_1_2_3_1)
                .formationDisplay("1-2-3-1")
                .status(LineupStatus.SAVED)
                .starters(List.of(starter))
                .reserveIds(List.of(8L, 9L))
                .savedAt(LocalDateTime.now())
                .message("Alineacion guardada")
                .build();

        assertEquals(1L, dto.getLineupId());
        assertEquals(10L, dto.getTeamId());
        assertEquals("Alpha FC", dto.getTeamName());
        assertEquals(TacticalFormation.F_1_2_3_1, dto.getFormation());
        assertEquals("1-2-3-1", dto.getFormationDisplay());
        assertEquals(LineupStatus.SAVED, dto.getStatus());
        assertEquals(1, dto.getStarters().size());
        assertEquals(2, dto.getReserveIds().size());
        assertNotNull(dto.getSavedAt());
        assertEquals("Alineacion guardada", dto.getMessage());
    }

    @Test
    void teamLineupResponseDTO_NoArgsConstructor_CreaVacio() {
        TeamLineupResponseDTO dto = new TeamLineupResponseDTO();
        assertNull(dto.getLineupId());
        assertNull(dto.getFormation());
        assertNull(dto.getStatus());
    }

    @Test
    void teamLineupResponseDTO_Setters_FuncionanCorrectamente() {
        TeamLineupResponseDTO dto = new TeamLineupResponseDTO();
        dto.setLineupId(2L);
        dto.setTeamName("Beta FC");
        dto.setStatus(LineupStatus.LOCKED);
        dto.setMessage("Alineacion bloqueada");

        assertEquals(2L, dto.getLineupId());
        assertEquals("Beta FC", dto.getTeamName());
        assertEquals(LineupStatus.LOCKED, dto.getStatus());
        assertEquals("Alineacion bloqueada", dto.getMessage());
    }
}
