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
class StandingTableServiceImplTest {

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

    private Equipo buildEquipo(Long id, String nombre) {
        Equipo e = new Equipo();
        e.setId(id);
        e.setNombre(nombre);
        return e;
    }

    private Tournament buildTournament(Long id, String tournId) {
        Tournament t = new Tournament();
        t.setId(id);
        t.setTournId(tournId);
        t.setName("TechCup 2026");
        return t;
    }

    private Partido buildPartido(Long id, Equipo local, Equipo visitante,
                                 MatchStatus estado, Tournament torneo) {
        Partido p = new Partido();
        p.setId(id);
        p.setEquipoLocal(local);
        p.setEquipoVisitante(visitante);
        p.setEstado(estado);
        p.setTorneo(torneo);
        p.setGolesLocal(0);
        p.setGolesVisitante(0);
        return p;
    }


    @Test
    void registerResult_HappyPath_LocalGana() {
        Equipo local = buildEquipo(1L, "Alpha");
        Equipo visitante = buildEquipo(2L, "Beta");
        Tournament torneo = buildTournament(1L, "TOURN-1");
        Partido partido = buildPartido(1L, local, visitante,
                MatchStatus.PROGRAMADO, torneo);

        RegisterMatchResultRequestDTO request =
                new RegisterMatchResultRequestDTO(3, 1);

        RegisterMatchResultResponseDTO expectedDTO =
                new RegisterMatchResultResponseDTO(1L, "Alpha", "Beta",
                        3, 1, "HOME_WIN", "Resultado registrado");

        when(matchRepository.findById(1L)).thenReturn(Optional.of(partido));
        when(statsRepository.findByEquipoIdAndTorneoId(any(), any()))
                .thenReturn(Optional.empty());
        when(standingsMapper.toRegisterMatchResultResponseDTO(any()))
                .thenReturn(expectedDTO);

        RegisterMatchResultResponseDTO result =
                standingsTableService.registerResult(1L, request);

        assertNotNull(result);
        assertEquals(MatchStatus.FINALIZADO, partido.getEstado());
        assertEquals(3, partido.getGolesLocal());
        assertEquals(1, partido.getGolesVisitante());
        verify(matchRepository, times(1)).save(partido);
        verify(statsRepository, times(2)).save(any());
    }

    @Test
    void registerResult_Empate_ActualizaEstadisticas() {
        Equipo local = buildEquipo(1L, "Alpha");
        Equipo visitante = buildEquipo(2L, "Beta");
        Tournament torneo = buildTournament(1L, "TOURN-1");
        Partido partido = buildPartido(1L, local, visitante,
                MatchStatus.PROGRAMADO, torneo);

        RegisterMatchResultRequestDTO request =
                new RegisterMatchResultRequestDTO(1, 1);

        when(matchRepository.findById(1L)).thenReturn(Optional.of(partido));
        when(statsRepository.findByEquipoIdAndTorneoId(any(), any()))
                .thenReturn(Optional.empty());
        when(standingsMapper.toRegisterMatchResultResponseDTO(any()))
                .thenReturn(new RegisterMatchResultResponseDTO());

        standingsTableService.registerResult(1L, request);

        assertEquals(MatchStatus.FINALIZADO, partido.getEstado());
        verify(statsRepository, times(2)).save(any());
    }

    @Test
    void registerResult_PartidoCancelado_LanzaBusinessRule() {
        Partido partido = new Partido();
        partido.setEstado(MatchStatus.CANCELADO);

        when(matchRepository.findById(1L)).thenReturn(Optional.of(partido));

        var request = new RegisterMatchResultRequestDTO(1, 0);
        assertThrows(BusinessRuleException.class,
                () -> standingsTableService.registerResult(1L, request));
        verify(matchRepository, never()).save(any());
    }

    @Test
    void registerResult_PartidoYaFinalizado_LanzaBusinessRule() {
        Partido partido = new Partido();
        partido.setEstado(MatchStatus.FINALIZADO);

        when(matchRepository.findById(1L)).thenReturn(Optional.of(partido));

        var request = new RegisterMatchResultRequestDTO(2, 0);
        assertThrows(BusinessRuleException.class,
                () -> standingsTableService.registerResult(1L, request));
        verify(matchRepository, never()).save(any());
    }

    @Test
    void registerResult_PartidoNoEncontrado_LanzaNotFound() {
        var request = new RegisterMatchResultRequestDTO(1, 0);
        when(matchRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> standingsTableService.registerResult(999L, request));
    }

    @Test
    void registerResult_SinTorneo_NoActualizaStats() {
        Equipo local = buildEquipo(1L, "Alpha");
        Equipo visitante = buildEquipo(2L, "Beta");
        Partido partido = buildPartido(1L, local, visitante,
                MatchStatus.PROGRAMADO, null);

        when(matchRepository.findById(1L)).thenReturn(Optional.of(partido));
        when(standingsMapper.toRegisterMatchResultResponseDTO(any()))
                .thenReturn(new RegisterMatchResultResponseDTO());

        standingsTableService.registerResult(1L,
                new RegisterMatchResultRequestDTO(1, 0));

        verify(statsRepository, never()).save(any());
    }


    @Test
    void getStandings_HappyPath_RetornaTabla() {
        Tournament torneo = buildTournament(1L, "TOURN-1");
        Equipo equipo = buildEquipo(1L, "Alpha");

        EstadisticaEquipo stats = new EstadisticaEquipo();
        stats.setEquipo(equipo);
        stats.setTorneo(torneo);
        stats.setPartidosJugados(2);
        stats.setPartidosGanados(1);
        stats.setPuntos(3);

        StandingsTableResponseDTO expectedDTO = StandingsTableResponseDTO.builder()
                .tournamentId("TOURN-1")
                .tournamentName("TechCup 2026")
                .totalTeams(1)
                .totalMatchesPlayed(1)
                .standings(List.of())
                .build();

        when(tournamentRepository.findByTournId("TOURN-1"))
                .thenReturn(Optional.of(torneo));
        when(statsRepository.findByTorneoIdOrderByPuntosDesc(1L))
                .thenReturn(List.of(stats));
        when(standingsMapper.toStandingsTableResponseDTO(any(), any(), anyInt(), any()))
                .thenReturn(expectedDTO);

        StandingsTableResponseDTO result =
                standingsTableService.getStandings("TOURN-1");

        assertNotNull(result);
        assertEquals("TOURN-1", result.getTournamentId());
        verify(standingsMapper, times(1))
                .toStandingsTableResponseDTO(any(), any(), anyInt(), any());
    }

    @Test
    void getStandings_TorneoNoEncontrado_LanzaNotFound() {
        when(tournamentRepository.findByTournId("TOURN-999"))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> standingsTableService.getStandings("TOURN-999"));
    }

    @Test
    void getStandings_SinEstadisticas_RetornaTablaVacia() {
        Tournament torneo = buildTournament(1L, "TOURN-1");

        when(tournamentRepository.findByTournId("TOURN-1"))
                .thenReturn(Optional.of(torneo));
        when(statsRepository.findByTorneoIdOrderByPuntosDesc(1L))
                .thenReturn(List.of());
        when(standingsMapper.toStandingsTableResponseDTO(any(), any(), anyInt(), any()))
                .thenReturn(new StandingsTableResponseDTO());

        StandingsTableResponseDTO result =
                standingsTableService.getStandings("TOURN-1");

        assertNotNull(result);
        verify(standingsMapper, times(1))
                .toStandingsTableResponseDTO(any(), any(), anyInt(), any());
    }
}