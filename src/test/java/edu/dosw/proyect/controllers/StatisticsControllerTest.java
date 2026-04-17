package edu.dosw.proyect.controllers;

import edu.dosw.proyect.controllers.dtos.response.TournamentStatisticsDTO;
import edu.dosw.proyect.core.services.EstadisticasService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StatisticsControllerTest {

    @Mock
    private EstadisticasService estadisticasService;

    @InjectMocks
    private StatisticsController statisticsController;

    @Test
    void obtenerEstadisticasTorneo_HappyPath_RetornaOk() {
        TournamentStatisticsDTO dto = TournamentStatisticsDTO.builder()
                .torneoId("TOURN-1")
                .totalPartidosJugados(5)
                .totalGolesAnotados(10)
                .tablaPosiciones(List.of())
                .tablaGoleadores(List.of())
                .tablaTarjetas(List.of())
                .build();

        when(estadisticasService.obtenerEstadisticasTorneo("TOURN-1"))
                .thenReturn(dto);

        ResponseEntity<TournamentStatisticsDTO> result =
                statisticsController.obtenerEstadisticasTorneo("TOURN-1");

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("TOURN-1", result.getBody().getTorneoId());
        verify(estadisticasService, times(1))
                .obtenerEstadisticasTorneo("TOURN-1");
    }
}