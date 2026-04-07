package edu.dosw.proyect.controllers.dtos.request;

import edu.dosw.proyect.core.models.enums.FieldPosition;
import edu.dosw.proyect.core.models.enums.TacticalFormation;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class SaveLineupRequestTest {

    @Test
    void saveLineupRequestDTO_AllArgsConstructor_ConstruyeCorrectamente() {
        StarterEntryRequestDTO starter = new StarterEntryRequestDTO(
                1L, FieldPosition.GOALKEEPER);

        SaveLineupRequestDTO dto = new SaveLineupRequestDTO(
                10L, 5L,
                TacticalFormation.F_1_2_3_1,
                List.of(starter, starter, starter,
                        starter, starter, starter, starter),
                List.of(8L, 9L)
        );

        assertEquals(10L, dto.getTeamId());
        assertEquals(5L, dto.getMatchId());
        assertEquals(TacticalFormation.F_1_2_3_1, dto.getFormation());
        assertEquals(7, dto.getStarters().size());
        assertEquals(2, dto.getReserveIds().size());
    }

    @Test
    void saveLineupRequestDTO_NoArgsConstructor_CreaVacio() {
        SaveLineupRequestDTO dto = new SaveLineupRequestDTO();
        assertNull(dto.getTeamId());
        assertNull(dto.getMatchId());
        assertNull(dto.getFormation());
    }

    @Test
    void saveLineupRequestDTO_Setters_FuncionanCorrectamente() {
        SaveLineupRequestDTO dto = new SaveLineupRequestDTO();
        dto.setTeamId(3L);
        dto.setMatchId(7L);
        dto.setFormation(TacticalFormation.F_1_3_2_1);
        dto.setReserveIds(List.of(10L));

        assertEquals(3L, dto.getTeamId());
        assertEquals(7L, dto.getMatchId());
        assertEquals(TacticalFormation.F_1_3_2_1, dto.getFormation());
        assertEquals(1, dto.getReserveIds().size());
    }
}
