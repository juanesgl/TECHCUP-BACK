package edu.dosw.proyect.services;

import edu.dosw.proyect.controllers.dtos.request.PartidoFiltroRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.PartidoResponseDTO;
import edu.dosw.proyect.controllers.mappers.PartidoMapper;
import edu.dosw.proyect.core.exceptions.ResourceNotFoundException;
import edu.dosw.proyect.core.models.Equipo;
import edu.dosw.proyect.core.models.Partido;
import edu.dosw.proyect.core.repositories.PartidoRepository;
import edu.dosw.proyect.core.services.impl.PartidoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PartidoServiceImplTest {

    @Mock
    private PartidoRepository partidoRepository;
    @Mock
    private PartidoMapper partidoMapper;

    @InjectMocks
    private PartidoServiceImpl partidoService;

    private Partido partido;
    private PartidoFiltroRequestDTO filtro;

    @BeforeEach
    void setUp() {
        partido = new Partido();
        partido.setId(1L);
        partido.setFechaHora(LocalDateTime.now());
        Equipo local = new Equipo();
        local.setNombre("Local");
        Equipo visitante = new Equipo();
        visitante.setNombre("Visitante");
        partido.setEquipoLocal(local);
        partido.setEquipoVisitante(visitante);

        filtro = new PartidoFiltroRequestDTO();
    }

    @Test
    void consultarPartidos_All_Success() {
        List<Partido> partidos = new ArrayList<>(List.of(partido));
        when(partidoRepository.findAll()).thenReturn(partidos);
        when(partidoMapper.toResponseDTOList(anyList())).thenReturn(List.of(new PartidoResponseDTO()));

        List<PartidoResponseDTO> result = partidoService.consultarPartidos(filtro);

        assertFalse(result.isEmpty());
        verify(partidoRepository).findAll();
    }

    @Test
    void consultarPartidos_ByFecha_Success() {
        LocalDateTime now = LocalDateTime.now();
        filtro.setFecha(now.toLocalDate());
        when(partidoRepository.findByFiltros(eq(now.toLocalDate()), isNull()))
                .thenReturn(new ArrayList<>(List.of(partido)));
        when(partidoMapper.toResponseDTOList(anyList())).thenReturn(List.of(new PartidoResponseDTO()));

        List<PartidoResponseDTO> result = partidoService.consultarPartidos(filtro);

        assertFalse(result.isEmpty());
        verify(partidoRepository).findByFiltros(any(), isNull());
    }

    @Test
    void consultarPartidos_ByNombreEquipo_Success() {
        filtro.setNombreEquipo("Tech");
        when(partidoRepository.findByNombreEquipo("Tech")).thenReturn(new ArrayList<>(List.of(partido)));
        when(partidoMapper.toResponseDTOList(anyList())).thenReturn(List.of(new PartidoResponseDTO()));

        List<PartidoResponseDTO> result = partidoService.consultarPartidos(filtro);

        assertFalse(result.isEmpty());
        verify(partidoRepository).findByNombreEquipo("Tech");
    }

    @Test
    void consultarPartidos_ByTournamentId_Success() {
        filtro.setTournamentId("T1");
        when(partidoRepository.findByTorneo_TournId("T1")).thenReturn(new ArrayList<>(List.of(partido)));
        when(partidoMapper.toResponseDTOList(anyList())).thenReturn(List.of(new PartidoResponseDTO()));

        List<PartidoResponseDTO> result = partidoService.consultarPartidos(filtro);

        assertFalse(result.isEmpty());
        verify(partidoRepository).findByTorneo_TournId("T1");
    }

    @Test
    void consultarPartidos_Empty_ThrowsException() {
        when(partidoRepository.findAll()).thenReturn(Collections.emptyList());

        assertThrows(ResourceNotFoundException.class, () -> partidoService.consultarPartidos(filtro));
    }

    @Test
    void consultarPartidos_Sorting_Success() {
        Partido p1 = new Partido();
        p1.setFechaHora(LocalDateTime.now().plusDays(1));
        Partido p2 = new Partido();
        p2.setFechaHora(LocalDateTime.now());

        List<Partido> partidos = new ArrayList<>(List.of(p1, p2));
        when(partidoRepository.findAll()).thenReturn(partidos);

        partidoService.consultarPartidos(filtro);

        assertEquals(p2, partidos.get(0));
        assertEquals(p1, partidos.get(1));
    }

    @Test
    void consultarPartidoPorId_Success() {
        when(partidoRepository.findById(1L)).thenReturn(Optional.of(partido));
        when(partidoMapper.toResponseDTO(partido)).thenReturn(new PartidoResponseDTO());

        PartidoResponseDTO result = partidoService.consultarPartidoPorId(1L);

        assertNotNull(result);
    }

    @Test
    void consultarPartidoPorId_NotFound_ThrowsException() {
        when(partidoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> partidoService.consultarPartidoPorId(1L));
    }
}
