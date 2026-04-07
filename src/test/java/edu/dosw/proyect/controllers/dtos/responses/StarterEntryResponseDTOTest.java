package edu.dosw.proyect.controllers.dtos.responses;

import edu.dosw.proyect.controllers.dtos.response.StarterEntryResponseDTO;
import edu.dosw.proyect.core.models.enums.FieldPosition;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StarterEntryResponseDTOTest {

    @Test
    void starterEntryResponseDTO_Builder_ConstruyeCorrectamente() {
        StarterEntryResponseDTO dto = StarterEntryResponseDTO.builder()
                .playerId(1L)
                .playerName("Juan Perez")
                .fieldPosition(FieldPosition.FORWARD)
                .build();

        assertEquals(1L, dto.getPlayerId());
        assertEquals("Juan Perez", dto.getPlayerName());
        assertEquals(FieldPosition.FORWARD, dto.getFieldPosition());
    }

    @Test
    void starterEntryResponseDTO_NoArgsConstructor_CreaVacio() {
        StarterEntryResponseDTO dto = new StarterEntryResponseDTO();
        assertNull(dto.getPlayerId());
        assertNull(dto.getPlayerName());
        assertNull(dto.getFieldPosition());
    }

    @Test
    void starterEntryResponseDTO_Setters_FuncionanCorrectamente() {
        StarterEntryResponseDTO dto = new StarterEntryResponseDTO();
        dto.setPlayerId(2L);
        dto.setPlayerName("Maria");
        dto.setFieldPosition(FieldPosition.GOALKEEPER);

        assertEquals(2L, dto.getPlayerId());
        assertEquals("Maria", dto.getPlayerName());
        assertEquals(FieldPosition.GOALKEEPER, dto.getFieldPosition());
    }
}