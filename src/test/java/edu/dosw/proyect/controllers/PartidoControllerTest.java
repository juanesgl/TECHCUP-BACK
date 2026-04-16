package edu.dosw.proyect.controllers;

import edu.dosw.proyect.controllers.dtos.request.PartidoFiltroRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.PartidoResponseDTO;
import edu.dosw.proyect.core.exceptions.ResourceNotFoundException;
import edu.dosw.proyect.core.services.PartidoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PartidoControllerTest {

    @Mock
    private PartidoService partidoService;

    @InjectMocks
    private PartidoController partidoController;

    private PartidoResponseDTO buildDTO(Long id, String local, String visitante) {
        return PartidoResponseDTO.builder()
                .id(id)
                .equipoLocal(local)
                .equipoVisitante(visitante)
                .fecha(LocalDate.now().plusDays(3))
                .hora(LocalTime.of(10, 0))
                .cancha("Cancha Principal")
                .arbitro("Arbitro Test")
                .estado("PROGRAMADO")
                .build();
    }


    @Test
    void consultarPartidos_HappyPath_RetornaLista() {
        List<PartidoResponseDTO> dtos = List.of(
                buildDTO(1L, "Alpha", "Beta"),
                buildDTO(2L, "Gamma", "Delta")
        );
        PartidoFiltroRequestDTO filtro = new PartidoFiltroRequestDTO();

        when(partidoService.consultarPartidos(filtro)).thenReturn(dtos);

        ResponseEntity<List<PartidoResponseDTO>> response =
                partidoController.consultarPartidos(filtro);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        verify(partidoService, times(1)).consultarPartidos(filtro);
    }

    @Test
    void consultarPartidos_HappyPath_FiltroCancha() {
        PartidoFiltroRequestDTO filtro = new PartidoFiltroRequestDTO();
        filtro.setCancha("Cancha Principal");

        List<PartidoResponseDTO> dtos = List.of(buildDTO(1L, "Alpha", "Beta"));
        when(partidoService.consultarPartidos(filtro)).thenReturn(dtos);

        ResponseEntity<List<PartidoResponseDTO>> response =
                partidoController.consultarPartidos(filtro);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void consultarPartidoPorId_HappyPath_RetornaDetalle() {
        PartidoResponseDTO dto = buildDTO(1L, "Alpha", "Beta");
        when(partidoService.consultarPartidoPorId(1L)).thenReturn(dto);

        ResponseEntity<PartidoResponseDTO> response =
                partidoController.consultarPartidoPorId(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        verify(partidoService, times(1)).consultarPartidoPorId(1L);
    }


    @Test
    void consultarPartidos_Error_SinResultados() {
        PartidoFiltroRequestDTO filtro = new PartidoFiltroRequestDTO();
        filtro.setCancha("Cancha Inexistente");

        when(partidoService.consultarPartidos(filtro))
                .thenThrow(new ResourceNotFoundException("No hay partidos"));

        assertThrows(ResourceNotFoundException.class,
                () -> partidoController.consultarPartidos(filtro));
    }

    @Test
    void consultarPartidoPorId_Error_NoEncontrado() {
        when(partidoService.consultarPartidoPorId(999L))
                .thenThrow(new ResourceNotFoundException("Partido no encontrado"));

        assertThrows(ResourceNotFoundException.class,
                () -> partidoController.consultarPartidoPorId(999L));
    }
}