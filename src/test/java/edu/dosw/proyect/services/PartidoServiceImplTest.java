package edu.dosw.proyect.services;

import edu.dosw.proyect.controllers.dtos.request.PartidoFiltroRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.PartidoResponseDTO;
import edu.dosw.proyect.controllers.mappers.PartidoMapper;
import edu.dosw.proyect.core.exceptions.ResourceNotFoundException;
import edu.dosw.proyect.core.models.Partido;
import edu.dosw.proyect.core.models.enums.MatchStatus;
import edu.dosw.proyect.core.repositories.PartidoRepository;
import edu.dosw.proyect.core.services.impl.PartidoServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PartidoServiceImplTest {

    @Mock
    private PartidoRepository partidoRepository;

    @Mock
    private PartidoMapper partidoMapper;

    @InjectMocks
    private PartidoServiceImpl partidoService;

    private Partido buildPartido(Long id, String local, String visitante, String cancha) {
        return Partido.builder()
                .id(id)
                .nombreEquipoLocal(local)
                .nombreEquipoVisitante(visitante)
                .fecha(LocalDate.now().plusDays(3))
                .hora(LocalTime.of(10, 0))
                .cancha(cancha)
                .arbitro("Arbitro Test")
                .estado(MatchStatus.PROGRAMADO)
                .build();
    }

    private PartidoResponseDTO buildResponseDTO(Long id, String local, String visitante) {
        return PartidoResponseDTO.builder()
                .id(id)
                .equipoLocal(local)
                .equipoVisitante(visitante)
                .estado("PROGRAMADO")
                .build();
    }


    @Test
    void consultarPartidos_HappyPath_SinFiltros() {

        List<Partido> partidos = List.of(
                buildPartido(1L, "Alpha", "Beta", "Cancha Principal"),
                buildPartido(2L, "Gamma", "Delta", "Cancha Norte")
        );
        List<PartidoResponseDTO> dtos = List.of(
                buildResponseDTO(1L, "Alpha", "Beta"),
                buildResponseDTO(2L, "Gamma", "Delta")
        );

        when(partidoRepository.findAll()).thenReturn(partidos);
        when(partidoMapper.toResponseDTOList(anyList())).thenReturn(dtos);

        PartidoFiltroRequestDTO filtro = new PartidoFiltroRequestDTO();

        List<PartidoResponseDTO> resultado = partidoService.consultarPartidos(filtro);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(partidoRepository, times(1)).findAll();
    }

    @Test
    void consultarPartidos_HappyPath_FiltroCancha() {
        List<Partido> partidos = List.of(
                buildPartido(1L, "Alpha", "Beta", "Cancha Principal")
        );
        List<PartidoResponseDTO> dtos = List.of(
                buildResponseDTO(1L, "Alpha", "Beta")
        );

        when(partidoRepository.findByCancha("Cancha Principal")).thenReturn(partidos);
        when(partidoMapper.toResponseDTOList(anyList())).thenReturn(dtos);

        PartidoFiltroRequestDTO filtro = new PartidoFiltroRequestDTO();
        filtro.setCancha("Cancha Principal");

        List<PartidoResponseDTO> resultado = partidoService.consultarPartidos(filtro);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(partidoRepository, times(1)).findByCancha("Cancha Principal");
    }

    @Test
    void consultarPartidos_HappyPath_FiltroEquipo() {

        List<Partido> partidos = List.of(
                buildPartido(1L, "Alpha", "Beta", "Cancha Principal"),
                buildPartido(3L, "Alpha", "Gamma", "Cancha Principal")
        );
        List<PartidoResponseDTO> dtos = List.of(
                buildResponseDTO(1L, "Alpha", "Beta"),
                buildResponseDTO(3L, "Alpha", "Gamma")
        );

        when(partidoRepository.findByEquipo("Alpha")).thenReturn(partidos);
        when(partidoMapper.toResponseDTOList(anyList())).thenReturn(dtos);

        PartidoFiltroRequestDTO filtro = new PartidoFiltroRequestDTO();
        filtro.setNombreEquipo("Alpha");

        List<PartidoResponseDTO> resultado = partidoService.consultarPartidos(filtro);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(partidoRepository, times(1)).findByEquipo("Alpha");
    }

    @Test
    void consultarPartidos_HappyPath_FiltroFecha() {

        LocalDate fecha = LocalDate.now().plusDays(3);
        List<Partido> partidos = List.of(
                buildPartido(1L, "Alpha", "Beta", "Cancha Principal")
        );
        List<PartidoResponseDTO> dtos = List.of(
                buildResponseDTO(1L, "Alpha", "Beta")
        );

        when(partidoRepository.findByFecha(fecha)).thenReturn(partidos);
        when(partidoMapper.toResponseDTOList(anyList())).thenReturn(dtos);

        PartidoFiltroRequestDTO filtro = new PartidoFiltroRequestDTO();
        filtro.setFecha(fecha);
        List<PartidoResponseDTO> resultado = partidoService.consultarPartidos(filtro);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(partidoRepository, times(1)).findByFecha(fecha);
    }

    @Test
    void consultarPartidoPorId_HappyPath() {
        Partido partido = buildPartido(1L, "Alpha", "Beta", "Cancha Principal");
        PartidoResponseDTO dto = buildResponseDTO(1L, "Alpha", "Beta");

        when(partidoRepository.findById(1L)).thenReturn(Optional.of(partido));
        when(partidoMapper.toResponseDTO(partido)).thenReturn(dto);

        PartidoResponseDTO resultado = partidoService.consultarPartidoPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(partidoRepository, times(1)).findById(1L);
    }


    @Test
    void consultarPartidos_Error_SinResultados() {
        when(partidoRepository.findByCancha("Cancha Inexistente"))
                .thenReturn(List.of());

        PartidoFiltroRequestDTO filtro = new PartidoFiltroRequestDTO();
        filtro.setCancha("Cancha Inexistente");

        ResourceNotFoundException ex = assertThrows(
                ResourceNotFoundException.class,
                () -> partidoService.consultarPartidos(filtro)
        );

        assertTrue(ex.getMessage().contains("No hay partidos programados"));
        verify(partidoRepository, times(1)).findByCancha("Cancha Inexistente");
    }

    @Test
    void consultarPartidoPorId_Error_NoEncontrado() {

        when(partidoRepository.findById(999L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(
                ResourceNotFoundException.class,
                () -> partidoService.consultarPartidoPorId(999L)
        );

        assertTrue(ex.getMessage().contains("999"));
        verify(partidoRepository, times(1)).findById(999L);
    }
}