package edu.dosw.proyect.controllers;

import edu.dosw.proyect.controllers.dtos.request.PartidoFiltroRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.PartidoResponseDTO;
import edu.dosw.proyect.core.services.PartidoService;
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
class JugadorPartidoControllerTest {

    @Mock
    private PartidoService partidoService;

    @InjectMocks
    private JugadorPartidoController jugadorPartidoController;

    @Test
    void consultarPartidos_HappyPath_RetornaOk() {
        PartidoFiltroRequestDTO filtro = new PartidoFiltroRequestDTO();
        List<PartidoResponseDTO> dtos = List.of(
                PartidoResponseDTO.builder().id(1L).equipoLocal("Alpha").build());

        when(partidoService.consultarPartidos(filtro)).thenReturn(dtos);

        ResponseEntity<List<PartidoResponseDTO>> result =
                jugadorPartidoController.consultarPartidos(filtro);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(1, result.getBody().size());
    }

    @Test
    void consultarPartidoPorId_HappyPath_RetornaOk() {
        PartidoResponseDTO dto = PartidoResponseDTO.builder()
                .id(1L).equipoLocal("Alpha").build();

        when(partidoService.consultarPartidoPorId(1L)).thenReturn(dto);

        ResponseEntity<PartidoResponseDTO> result =
                jugadorPartidoController.consultarPartidoPorId(1L);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(1L, result.getBody().getId());
    }
}