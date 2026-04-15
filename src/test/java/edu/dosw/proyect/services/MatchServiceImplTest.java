package edu.dosw.proyect.services;

import edu.dosw.proyect.controllers.dtos.request.MatchFilterRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.MatchResponseDTO;
import edu.dosw.proyect.controllers.mappers.PartidoMapper;
import edu.dosw.proyect.core.exceptions.ResourceNotFoundException;
import edu.dosw.proyect.core.models.Team;
import edu.dosw.proyect.core.models.Partido;
import edu.dosw.proyect.core.models.enums.MatchStatus;
import edu.dosw.proyect.core.services.impl.MatchServiceImpl;
import edu.dosw.proyect.persistence.entity.MatchEntity;
import edu.dosw.proyect.persistence.mapper.MatchPersistenceMapper;
import edu.dosw.proyect.persistence.repository.MatchRepository;
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
class MatchServiceImplTest {

    @Mock
    private MatchRepository matchRepository;

    @Mock
    private PartidoMapper partidoMapper;

    @Mock
    private MatchPersistenceMapper matchPersistenceMapper;

    @InjectMocks
    private MatchServiceImpl partidoService;

    private MatchEntity buildEntity(Long id) {
        MatchEntity e = new MatchEntity();
        e.setId(id);
        e.setEstado(MatchStatus.PROGRAMADO);
        e.setFechaHora(LocalDateTime.now());
        return e;
    }

    private Partido buildDomain(Long id) {
        Team local = new Team();
        local.setNombre("Alpha");
        Team visitante = new Team();
        visitante.setNombre("Beta");

        Partido p = new Partido();
        p.setId(id);
        p.setTeamLocal(local);
        p.setTeamVisitante(visitante);
        p.setEstado(MatchStatus.PROGRAMADO);
        p.setFechaHora(LocalDateTime.now());
        return p;
    }

    @Test
    void consultarPartidos_PorFecha_RetornaLista() {
        MatchFilterRequestDTO filtro = new MatchFilterRequestDTO();
        filtro.setFecha(java.time.LocalDate.now());

        MatchEntity entity = buildEntity(1L);
        Partido domain = buildDomain(1L);
        MatchResponseDTO dto = MatchResponseDTO.builder().id(1L).build();

        when(matchRepository.findByFiltros(any(), any()))
                .thenReturn(List.of(entity));
        when(matchPersistenceMapper.toDomain(entity)).thenReturn(domain);
        when(partidoMapper.toResponseDTOList(any())).thenReturn(List.of(dto));

        List<MatchResponseDTO> result = partidoService.consultarPartidos(filtro);

        assertEquals(1, result.size());
    }

    @Test
    void consultarPartidos_SinResultados_LanzaNotFound() {
        MatchFilterRequestDTO filtro = new MatchFilterRequestDTO();

        when(matchRepository.findAll()).thenReturn(List.of());

        assertThrows(ResourceNotFoundException.class,
                () -> partidoService.consultarPartidos(filtro));
    }

    @Test
    void consultarPartidoPorId_HappyPath_RetornaDTO() {
        MatchEntity entity = buildEntity(1L);
        Partido domain = buildDomain(1L);
        MatchResponseDTO dto = MatchResponseDTO.builder().id(1L).build();

        when(matchRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(matchPersistenceMapper.toDomain(entity)).thenReturn(domain);
        when(partidoMapper.toResponseDTO(domain)).thenReturn(dto);

        MatchResponseDTO result = partidoService.consultarPartidoPorId(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void consultarPartidoPorId_NoExiste_LanzaNotFound() {
        when(matchRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,
                () -> partidoService.consultarPartidoPorId(99L));
    }

    @Test
    void consultarPartidos_PorEquipo_RetornaLista() {
        MatchFilterRequestDTO filtro = new MatchFilterRequestDTO();
        filtro.setNombreEquipo("Alpha");

        MatchEntity entity = buildEntity(1L);
        Partido domain = buildDomain(1L);
        MatchResponseDTO dto = MatchResponseDTO.builder().id(1L).build();

        when(matchRepository.findByNombreEquipo("Alpha")).thenReturn(List.of(entity));
        when(matchPersistenceMapper.toDomain(entity)).thenReturn(domain);
        when(partidoMapper.toResponseDTOList(any())).thenReturn(List.of(dto));

        List<MatchResponseDTO> result = partidoService.consultarPartidos(filtro);
        assertEquals(1, result.size());
    }

    @Test
    void consultarPartidos_PorTorneo_RetornaLista() {
        MatchFilterRequestDTO filtro = new MatchFilterRequestDTO();
        filtro.setTournamentId("TOURN-1");

        MatchEntity entity = buildEntity(1L);
        Partido domain = buildDomain(1L);
        MatchResponseDTO dto = MatchResponseDTO.builder().id(1L).build();

        when(matchRepository.findByTorneo_TournId("TOURN-1")).thenReturn(List.of(entity));
        when(matchPersistenceMapper.toDomain(entity)).thenReturn(domain);
        when(partidoMapper.toResponseDTOList(any())).thenReturn(List.of(dto));

        List<MatchResponseDTO> result = partidoService.consultarPartidos(filtro);
        assertEquals(1, result.size());
    }

    @Test
    void consultarPartidos_ConFechaHoraNula_OrdenaCorrectamente() {
        MatchFilterRequestDTO filtro = new MatchFilterRequestDTO();
        filtro.setTournamentId("TOURN-1");

        MatchEntity entity1 = buildEntity(1L);
        MatchEntity entity2 = buildEntity(2L);

        Partido p1 = buildDomain(1L);
        p1.setFechaHora(null);

        Partido p2 = buildDomain(2L);
        p2.setFechaHora(LocalDateTime.now().plusDays(1));

        MatchResponseDTO dto = MatchResponseDTO.builder().id(1L).build();

        when(matchRepository.findByTorneo_TournId("TOURN-1"))
                .thenReturn(List.of(entity1, entity2));
        when(matchPersistenceMapper.toDomain(entity1)).thenReturn(p1);
        when(matchPersistenceMapper.toDomain(entity2)).thenReturn(p2);
        when(partidoMapper.toResponseDTOList(any())).thenReturn(List.of(dto));

        List<MatchResponseDTO> result = partidoService.consultarPartidos(filtro);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void consultarPartidos_SegundoConFechaHoraNula_OrdenaCorrectamente() {
        MatchFilterRequestDTO filtro = new MatchFilterRequestDTO();
        filtro.setTournamentId("TOURN-1");

        MatchEntity entity1 = buildEntity(1L);
        MatchEntity entity2 = buildEntity(2L);

        Partido p1 = buildDomain(1L);
        p1.setFechaHora(LocalDateTime.now());

        Partido p2 = buildDomain(2L);
        p2.setFechaHora(null);

        MatchResponseDTO dto = MatchResponseDTO.builder().id(1L).build();

        when(matchRepository.findByTorneo_TournId("TOURN-1"))
                .thenReturn(List.of(entity1, entity2));
        when(matchPersistenceMapper.toDomain(entity1)).thenReturn(p1);
        when(matchPersistenceMapper.toDomain(entity2)).thenReturn(p2);
        when(partidoMapper.toResponseDTOList(any())).thenReturn(List.of(dto));

        List<MatchResponseDTO> result = partidoService.consultarPartidos(filtro);

        assertNotNull(result);
    }
}