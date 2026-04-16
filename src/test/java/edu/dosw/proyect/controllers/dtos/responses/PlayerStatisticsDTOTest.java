package edu.dosw.proyect.controllers.dtos.responses;

import edu.dosw.proyect.controllers.dtos.response.PlayerStatisticsDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerStatisticsDTOTest {

    @Test
    void estadisticasJugadorDTO_Builder_ConstruyeCorrectamente() {
        PlayerStatisticsDTO dto = PlayerStatisticsDTO.builder()
                .jugadorId(1L)
                .nombreJugador("Juan Perez")
                .nombreEquipo("Alpha FC")
                .goles(5)
                .tarjetasAmarillas(2)
                .tarjetasRojas(0)
                .build();

        assertEquals(1L, dto.getJugadorId());
        assertEquals("Juan Perez", dto.getNombreJugador());
        assertEquals("Alpha FC", dto.getNombreEquipo());
        assertEquals(5, dto.getGoles());
        assertEquals(2, dto.getTarjetasAmarillas());
        assertEquals(0, dto.getTarjetasRojas());
    }

    @Test
    void estadisticasJugadorDTO_NoArgsConstructor_CreaVacio() {
        PlayerStatisticsDTO dto = new PlayerStatisticsDTO();
        assertNull(dto.getJugadorId());
        assertNull(dto.getNombreJugador());
        assertEquals(0, dto.getGoles());
    }

    @Test
    void estadisticasJugadorDTO_AllArgsConstructor_ConstruyeCorrectamente() {
        PlayerStatisticsDTO dto = new PlayerStatisticsDTO(
                1L, "Carlos", "Beta FC", 3, 1, 0);

        assertEquals(1L, dto.getJugadorId());
        assertEquals("Carlos", dto.getNombreJugador());
        assertEquals(3, dto.getGoles());
        assertEquals(1, dto.getTarjetasAmarillas());
        assertEquals(0, dto.getTarjetasRojas());
    }

    @Test
    void estadisticasJugadorDTO_Setters_FuncionanCorrectamente() {
        PlayerStatisticsDTO dto = new PlayerStatisticsDTO();
        dto.setJugadorId(2L);
        dto.setNombreJugador("Maria");
        dto.setNombreEquipo("Gamma FC");
        dto.setGoles(7);
        dto.setTarjetasAmarillas(3);
        dto.setTarjetasRojas(1);

        assertEquals(2L, dto.getJugadorId());
        assertEquals("Maria", dto.getNombreJugador());
        assertEquals(7, dto.getGoles());
    }
}