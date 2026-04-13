package edu.dosw.proyect.services;

import edu.dosw.proyect.controllers.dtos.request.RegisterMatchResultRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.RegisterMatchResultResponseDTO;
import edu.dosw.proyect.controllers.dtos.response.StandingsTableResponseDTO;
import edu.dosw.proyect.controllers.mappers.StandingsTableMapper;
import edu.dosw.proyect.core.exceptions.BusinessRuleException;
import edu.dosw.proyect.core.exceptions.ResourceNotFoundException;
import edu.dosw.proyect.core.models.Equipo;
import edu.dosw.proyect.core.models.Partido;
import edu.dosw.proyect.core.models.enums.MatchStatus;
import edu.dosw.proyect.core.services.impl.StandingsTableServiceImpl;
import edu.dosw.proyect.persistence.entity.EquipoEntity;
import edu.dosw.proyect.persistence.entity.PartidoEntity;
import edu.dosw.proyect.persistence.entity.TournamentEntity;
import edu.dosw.proyect.persistence.mapper.PartidoPersistenceMapper;
import edu.dosw.proyect.persistence.repository.EstadisticaEquipoRepository;
import edu.dosw.proyect.persistence.repository.PartidoRepository;
import edu.dosw.proyect.persistence.repository.TournamentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StandinggsTableServiceImplTest {

    @Mock
    private PartidoRepository matchRepository;

    @Mock
    private EstadisticaEquipoRepository statsRepository;

    @Mock
    private TournamentRepository tournamentRepository;

    @Mock
    private StandingsTableMapper standingsMapper;

    @Mock
    private PartidoPersistenceMapper partidoMapper;

    @InjectMocks
    private StandingsTableServiceImpl standingsTableService;

    private EquipoEntity buildEquipoEntity(Long id, String nombre) {
        EquipoEntity e = new EquipoEntity();
        e.setId(id);
        e.setNombre(nombre);
        return e;
    }

    private PartidoEntity buildPartidoEntity(MatchStatus estado) {
        PartidoEntity e = new PartidoEntity();
        e.setId(1L);
        e.setEstado(estado);
        e.setEquipoLocal(buildEquipoEntity(1L, "Alpha"));
        e.setEquipoVisitante(buildEquipoEntity(2L, "Beta"));
        TournamentEntity torneo = new TournamentEntity();
        torneo.setId(1L);
        e.setTorneo(torneo);
        return e;
    }

    private Partido buildPartidoDomain(MatchStatus estado) {
        Equipo local = new Equipo();
        local.setId(1L);
        local.setNombre("Alpha");
        Equipo visitante = new Equipo();
        visitante.setId(2L);
        visitante.setNombre("Beta");
        Partido p = new Partido();
        p.setId(1L);
        p.setEstado(estado);
        p.setEquipoLocal(local);
        p.setEquipoVisitante(visitante);
        return p;
    }

    @Test
    void registerResult_HappyPath_LocalGana() {
        PartidoEntity entity = buildPartidoEntity(MatchStatus.PROGRAMADO);
        Partido domain = buildPartidoDomain(MatchStatus.PROGRAMADO);
        var request = new RegisterMatchResultRequestDTO(3, 1);

        when(matchRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(partidoMapper.toDomain(entity)).thenReturn(domain);
        when(statsRepository.findByEquipoIdAndTorneoId(any(), any()))
                .thenReturn(Optional.empty());
        when(matchRepository.save(any())).thenReturn(entity);
        when(partidoMapper.toDomain(entity)).thenReturn(domain);
        when(standingsMapper.toRegisterMatchResultResponseDTO(any()))
                .thenReturn(new RegisterMatchResultResponseDTO());

        RegisterMatchResultResponseDTO result =
                standingsTableService.registerResult(1L, request);

        assertNotNull(result);
        verify(matchRepository, times(1)).save(entity);
    }

    @Test
    void registerResult_PartidoCancelado_LanzaException() {
        PartidoEntity entity = buildPartidoEntity(MatchStatus.CANCELADO);
        Partido domain = buildPartidoDomain(MatchStatus.CANCELADO);
        var request = new RegisterMatchResultRequestDTO(1, 0);

        when(matchRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(partidoMapper.toDomain(entity)).thenReturn(domain);

        assertThrows(BusinessRuleException.class,
                () -> standingsTableService.registerResult(1L, request));
    }

    @Test
    void registerResult_PartidoFinalizado_LanzaException() {
        PartidoEntity entity = buildPartidoEntity(MatchStatus.FINALIZADO);
        Partido domain = buildPartidoDomain(MatchStatus.FINALIZADO);
        var request = new RegisterMatchResultRequestDTO(2, 0);

        when(matchRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(partidoMapper.toDomain(entity)).thenReturn(domain);

        assertThrows(BusinessRuleException.class,
                () -> standingsTableService.registerResult(1L, request));
    }

    @Test
    void registerResult_PartidoNoEncontrado_LanzaException() {
        when(matchRepository.findById(999L)).thenReturn(Optional.empty());
        var request = new RegisterMatchResultRequestDTO(1, 0);
        assertThrows(ResourceNotFoundException.class,
                () -> standingsTableService.registerResult(999L, request));
    }

    @Test
    void getStandings_HappyPath_RetornaTabla() {
        TournamentEntity torneo = new TournamentEntity();
        torneo.setId(1L);
        torneo.setName("TechCup");

        when(tournamentRepository.findByTournId("TOURN-1"))
                .thenReturn(Optional.of(torneo));
        when(statsRepository.findByTorneoIdOrderByPuntosDesc(1L))
                .thenReturn(List.of());
        when(standingsMapper.toStandingsTableResponseDTO(any(), any(), anyInt(), any()))
                .thenReturn(new StandingsTableResponseDTO());

        StandingsTableResponseDTO result =
                standingsTableService.getStandings("TOURN-1");

        assertNotNull(result);
    }

    @Test
    void getStandings_TorneoNoEncontrado_LanzaException() {
        when(tournamentRepository.findByTournId("TOURN-999"))
                .thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,
                () -> standingsTableService.getStandings("TOURN-999"));
    }
}