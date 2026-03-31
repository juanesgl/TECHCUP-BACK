package edu.dosw.proyect.services;

import edu.dosw.proyect.controllers.dtos.RegisterMatchResultRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.RegisterMatchResultResponseDTO;
import edu.dosw.proyect.controllers.dtos.response.StandingsTableResponseDTO;
import edu.dosw.proyect.controllers.mappers.StandingsTableMapper;
import edu.dosw.proyect.core.exceptions.BusinessRuleException;
import edu.dosw.proyect.core.exceptions.ResourceNotFoundException;
import edu.dosw.proyect.core.models.*;
import edu.dosw.proyect.core.models.enums.MatchStatus;
import edu.dosw.proyect.core.services.impl.StandingsTableServiceImpl;
import edu.dosw.proyect.persistence.entity.PartidoEntity;
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
class StandingsTableServiceImplTest {

    @Mock
    private PartidoRepository matchRepository;

    @Mock
    private EstadisticaEquipoRepository statsRepository;

    @Mock
    private TournamentRepository tournamentRepository;

    @Mock
    private StandingsTableMapper standingsMapper;

    @InjectMocks
    private StandingsTableServiceImpl standingsTableService;

    private PartidoEntity buildPartido(Long id, MatchStatus estado) {
        PartidoEntity p = new PartidoEntity();
        p.setId(id);
        p.setEstado(estado);
        p.setGolesLocal(0);
        p.setGolesVisitante(0);
        return p;
    }

    @Test
    void registerResult_HappyPath() {
        PartidoEntity partido = buildPartido(1L, MatchStatus.PROGRAMADO);

        when(matchRepository.findById(1L)).thenReturn(Optional.of(partido));
        when(statsRepository.findByEquipoIdAndTorneoId(any(), any()))
                .thenReturn(Optional.empty());
        when(standingsMapper.toRegisterMatchResultResponseDTO(any()))
                .thenReturn(new RegisterMatchResultResponseDTO());

        RegisterMatchResultResponseDTO result =
                standingsTableService.registerResult(1L, new RegisterMatchResultRequestDTO(3,1));

        assertNotNull(result);
        verify(matchRepository).save(partido);
    }

    @Test
    void registerResult_NotFound() {
        when(matchRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> standingsTableService.registerResult(1L, new RegisterMatchResultRequestDTO(1,0)));
    }

    @Test
    void registerResult_Cancelado() {
        PartidoEntity partido = buildPartido(1L, MatchStatus.CANCELADO);

        when(matchRepository.findById(1L)).thenReturn(Optional.of(partido));

        assertThrows(BusinessRuleException.class,
                () -> standingsTableService.registerResult(1L, new RegisterMatchResultRequestDTO(1,0)));
    }

    @Test
    void getStandings_Success() {
        Tournament torneo = new Tournament();
        torneo.setId(1L);
        torneo.setTournId("T1");

        when(tournamentRepository.findByTournId("T1")).thenReturn(Optional.of(torneo));
        when(statsRepository.findByTorneoIdOrderByPuntosDesc(1L)).thenReturn(List.of());
        when(standingsMapper.toStandingsTableResponseDTO(any(), any(), anyInt(), any()))
                .thenReturn(new StandingsTableResponseDTO());

        StandingsTableResponseDTO result = standingsTableService.getStandings("T1");

        assertNotNull(result);
    }
}