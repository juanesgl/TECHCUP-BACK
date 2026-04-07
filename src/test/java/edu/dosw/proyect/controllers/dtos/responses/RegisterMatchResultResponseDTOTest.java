package edu.dosw.proyect.controllers.dtos.responses;

import edu.dosw.proyect.controllers.dtos.response.RegisterMatchResultResponseDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RegisterMatchResultResponseDTOTest {

    @Test
    void registerMatchResultResponseDTO_Builder_ConstruyeCorrectamente() {
        RegisterMatchResultResponseDTO dto = RegisterMatchResultResponseDTO.builder()
                .matchId(1L)
                .homeTeam("Alpha")
                .awayTeam("Beta")
                .homeGoals(3)
                .awayGoals(1)
                .outcome("HOME_WIN")
                .message("Resultado registrado exitosamente")
                .build();

        assertEquals(1L, dto.getMatchId());
        assertEquals("Alpha", dto.getHomeTeam());
        assertEquals("Beta", dto.getAwayTeam());
        assertEquals(3, dto.getHomeGoals());
        assertEquals(1, dto.getAwayGoals());
        assertEquals("HOME_WIN", dto.getOutcome());
        assertEquals("Resultado registrado exitosamente", dto.getMessage());
    }

    @Test
    void registerMatchResultResponseDTO_NoArgsConstructor_CreaVacio() {
        RegisterMatchResultResponseDTO dto = new RegisterMatchResultResponseDTO();
        assertNull(dto.getMatchId());
        assertNull(dto.getHomeTeam());
        assertEquals(0, dto.getHomeGoals());
    }

    @Test
    void registerMatchResultResponseDTO_Setters_FuncionanCorrectamente() {
        RegisterMatchResultResponseDTO dto = new RegisterMatchResultResponseDTO();
        dto.setMatchId(2L);
        dto.setHomeTeam("Gamma");
        dto.setAwayGoals(2);
        dto.setOutcome("AWAY_WIN");

        assertEquals(2L, dto.getMatchId());
        assertEquals("Gamma", dto.getHomeTeam());
        assertEquals(2, dto.getAwayGoals());
        assertEquals("AWAY_WIN", dto.getOutcome());
    }
}