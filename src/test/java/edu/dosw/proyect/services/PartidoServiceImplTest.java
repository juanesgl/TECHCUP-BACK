package edu.dosw.proyect.services;

import edu.dosw.proyect.controllers.dtos.request.PartidoFiltroRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.PartidoResponseDTO;
import edu.dosw.proyect.controllers.mappers.PartidoMapper;
import edu.dosw.proyect.core.exceptions.ResourceNotFoundException;
import edu.dosw.proyect.core.models.Equipo;
import edu.dosw.proyect.core.models.Partido;
import edu.dosw.proyect.core.models.enums.MatchStatus;
import edu.dosw.proyect.core.services.impl.PartidoServiceImpl;
import edu.dosw.proyect.persistence.entity.PartidoEntity;
import edu.dosw.proyect.persistence.mapper.PartidoPersistenceMapper;
import edu.dosw.proyect.persistence.repository.PartidoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
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

    @Mock
    private PartidoPersistenceMapper partidoPersistenceMapper;

    @InjectMocks
    private PartidoServiceImpl partidoService;

    private PartidoEntity buildEntity(Long id) {
        PartidoEntity e = new PartidoEntity();
        e.setId(id);
        e.setEstado(MatchStatus.PROGRAMADO);
        e.setFechaHora(LocalDateTime.now());
        return e;
    }

    private Partido buildDomain(Long id) {
        Equipo local = new Equipo();
        local.setNombre("Alpha");
        Equipo visitante = new Equipo();
        visitante.setNombre("Beta");

        Partido p = new Partido();
        p.setId(id);
        p.setEquipoLocal(local);
        p.setEquipoVisitante(visitante);
        p.setEstado(MatchStatus.PROGRAMADO);
        p.setFechaHora(LocalDateTime.now());
        return p;
    }

    @Test
    void consultarPartidos_PorFecha_RetornaLista() {
        PartidoFiltroRequestDTO filtro = new PartidoFiltroRequestDTO();
        filtro.setFecha(java.time.LocalDate.now());

        PartidoEntity entity = buildEntity(1L);
        Partido domain = buildDomain(1L);
        PartidoResponseDTO dto = PartidoResponseDTO.builder().id(1L).build();

        when(partidoRepository.findByFiltros(any(), any()))
                .thenReturn(List.of(entity));
        when(partidoPersistenceMapper.toDomain(entity)).thenReturn(domain);
        when(partidoMapper.toResponseDTOList(any())).thenReturn(List.of(dto));

        List<PartidoResponseDTO> result = partidoService.consultarPartidos(filtro);

        assertEquals(1, result.size());
    }

    @Test
    void consultarPartidos_SinResultados_LanzaNotFound() {
        PartidoFiltroRequestDTO filtro = new PartidoFiltroRequestDTO();

        when(partidoRepository.findAll()).thenReturn(List.of());

        assertThrows(ResourceNotFoundException.class,
                () -> partidoService.consultarPartidos(filtro));
    }

    @Test
    void consultarPartidoPorId_HappyPath_RetornaDTO() {
        PartidoEntity entity = buildEntity(1L);
        Partido domain = buildDomain(1L);
        PartidoResponseDTO dto = PartidoResponseDTO.builder().id(1L).build();

        when(partidoRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(partidoPersistenceMapper.toDomain(entity)).thenReturn(domain);
        when(partidoMapper.toResponseDTO(domain)).thenReturn(dto);

        PartidoResponseDTO result = partidoService.consultarPartidoPorId(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void consultarPartidoPorId_NoExiste_LanzaNotFound() {
        when(partidoRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,
                () -> partidoService.consultarPartidoPorId(99L));
    }

    @Test
    void consultarPartidos_PorEquipo_RetornaLista() {
        PartidoFiltroRequestDTO filtro = new PartidoFiltroRequestDTO();
        filtro.setNombreEquipo("Alpha");

        PartidoEntity entity = buildEntity(1L);
        Partido domain = buildDomain(1L);
        PartidoResponseDTO dto = PartidoResponseDTO.builder().id(1L).build();

        when(partidoRepository.findByNombreEquipo("Alpha")).thenReturn(List.of(entity));
        when(partidoPersistenceMapper.toDomain(entity)).thenReturn(domain);
        when(partidoMapper.toResponseDTOList(any())).thenReturn(List.of(dto));

        List<PartidoResponseDTO> result = partidoService.consultarPartidos(filtro);
        assertEquals(1, result.size());
    }

    @Test
    void consultarPartidos_PorTorneo_RetornaLista() {
        PartidoFiltroRequestDTO filtro = new PartidoFiltroRequestDTO();
        filtro.setTournamentId("TOURN-1");

        PartidoEntity entity = buildEntity(1L);
        Partido domain = buildDomain(1L);
        PartidoResponseDTO dto = PartidoResponseDTO.builder().id(1L).build();

        when(partidoRepository.findByTorneo_TournId("TOURN-1")).thenReturn(List.of(entity));
        when(partidoPersistenceMapper.toDomain(entity)).thenReturn(domain);
        when(partidoMapper.toResponseDTOList(any())).thenReturn(List.of(dto));

        List<PartidoResponseDTO> result = partidoService.consultarPartidos(filtro);
        assertEquals(1, result.size());
    }
}