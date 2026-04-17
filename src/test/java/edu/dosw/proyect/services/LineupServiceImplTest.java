package edu.dosw.proyect.services;

import edu.dosw.proyect.controllers.dtos.response.OpponentLineupResponseDTO;
import edu.dosw.proyect.controllers.mappers.LineupMapper;
import edu.dosw.proyect.core.exceptions.BusinessRuleException;
import edu.dosw.proyect.core.exceptions.ResourceNotFoundException;
import edu.dosw.proyect.core.models.Lineup;
import edu.dosw.proyect.core.models.Team;
import edu.dosw.proyect.core.models.Partido;
import edu.dosw.proyect.core.services.impl.LineupServiceImpl;
import edu.dosw.proyect.persistence.entity.LineupEntity;
import edu.dosw.proyect.persistence.entity.MatchEntity;
import edu.dosw.proyect.persistence.mapper.LineupPersistenceMapper;
import edu.dosw.proyect.persistence.mapper.MatchPersistenceMapper;
import edu.dosw.proyect.persistence.repository.LineupRepository;
import edu.dosw.proyect.persistence.repository.MatchRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LineupServiceImplTest {

    @Mock private MatchRepository matchRepository;
    @Mock private LineupRepository lineupRepository;
    @Mock private LineupMapper lineupMapper;
    @Mock private MatchPersistenceMapper partidoMapper;
    @Mock private LineupPersistenceMapper alineacionMapper2;

    @InjectMocks
    private LineupServiceImpl alineacionService;

    private Partido buildPartido(Long localId, Long visitanteId) {
        Team local = new Team();
        local.setId(localId);
        local.setNombre("Alpha");

        Team visitante = new Team();
        visitante.setId(visitanteId);
        visitante.setNombre("Beta");

        Partido p = new Partido();
        p.setId(1L);
        p.setTeamLocal(local);
        p.setTeamVisitante(visitante);
        return p;
    }

    @Test
    void consultarAlineacionRival_HappyPath_EquipoLocal() {
        MatchEntity entity = new MatchEntity();
        Partido partido = buildPartido(1L, 2L);
        LineupEntity lineupEntity = new LineupEntity();
        Lineup lineup = new Lineup();
        OpponentLineupResponseDTO dto = OpponentLineupResponseDTO.builder()
                .partidoId(1L).nombreEquipoRival("Beta").build();

        when(matchRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(partidoMapper.toDomain(entity)).thenReturn(partido);
        when(lineupRepository.findByPartidoIdAndEquipoId(1L, 2L))
                .thenReturn(Optional.of(lineupEntity));
        when(alineacionMapper2.toDomain(lineupEntity)).thenReturn(lineup);
        when(lineupMapper.toRivalResponseDTO(lineup, 1L)).thenReturn(dto);

        OpponentLineupResponseDTO result =
                alineacionService.consultarAlineacionRival(1L, 1L);

        assertNotNull(result);
        assertEquals("Beta", result.getNombreEquipoRival());
    }

    @Test
    void consultarAlineacionRival_HappyPath_EquipoVisitante() {
        MatchEntity entity = new MatchEntity();
        Partido partido = buildPartido(1L, 2L);
        LineupEntity lineupEntity = new LineupEntity();
        Lineup lineup = new Lineup();
        OpponentLineupResponseDTO dto = OpponentLineupResponseDTO.builder()
                .partidoId(1L).nombreEquipoRival("Alpha").build();

        when(matchRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(partidoMapper.toDomain(entity)).thenReturn(partido);
        when(lineupRepository.findByPartidoIdAndEquipoId(1L, 1L))
                .thenReturn(Optional.of(lineupEntity));
        when(alineacionMapper2.toDomain(lineupEntity)).thenReturn(lineup);
        when(lineupMapper.toRivalResponseDTO(lineup, 1L)).thenReturn(dto);

        OpponentLineupResponseDTO result =
                alineacionService.consultarAlineacionRival(1L, 2L);

        assertNotNull(result);
    }

    @Test
    void consultarAlineacionRival_PartidoNoEncontrado_LanzaException() {
        when(matchRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,
                () -> alineacionService.consultarAlineacionRival(99L, 1L));
    }

    @Test
    void consultarAlineacionRival_EquipoNoParticipa_LanzaException() {
        MatchEntity entity = new MatchEntity();
        Partido partido = buildPartido(1L, 2L);

        when(matchRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(partidoMapper.toDomain(entity)).thenReturn(partido);

        assertThrows(BusinessRuleException.class,
                () -> alineacionService.consultarAlineacionRival(1L, 99L));
    }

    @Test
    void consultarAlineacionRival_AlineacionNoRegistrada_LanzaException() {
        MatchEntity entity = new MatchEntity();
        Partido partido = buildPartido(1L, 2L);

        when(matchRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(partidoMapper.toDomain(entity)).thenReturn(partido);
        when(lineupRepository.findByPartidoIdAndEquipoId(1L, 2L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> alineacionService.consultarAlineacionRival(1L, 1L));
    }
}