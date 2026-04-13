package edu.dosw.proyect.services;

import edu.dosw.proyect.controllers.dtos.response.BracketMatchDTO;
import edu.dosw.proyect.controllers.dtos.response.TournamentBracketResponseDTO;
import edu.dosw.proyect.core.exceptions.BusinessRuleException;
import edu.dosw.proyect.core.exceptions.ResourceNotFoundException;
import edu.dosw.proyect.core.models.enums.MatchStatus;
import edu.dosw.proyect.core.services.impl.KnockoutBracketServiceImpl;
import edu.dosw.proyect.persistence.entity.*;
import edu.dosw.proyect.persistence.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KnockoutBracketServiceImplTest {

    @Mock private TournamentRepository tournamentRepository;
    @Mock private EquipoRepository teamRepository;
    @Mock private LlaveEliminatoriaRepository bracketRepository;
    @Mock private PartidoRepository matchRepository;

    @InjectMocks
    private KnockoutBracketServiceImpl knockoutBracketService;

    @Test
    void generateBracket_TournamentNotFound() {
        when(tournamentRepository.findByTournId(anyString())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> 
            knockoutBracketService.generateBracket("invalid-id")
        );
    }

    @Test
    void generateBracket_BracketAlreadyExists() {
        TournamentEntity tournament = new TournamentEntity();
        tournament.setId(100L);
        when(tournamentRepository.findByTournId("tourn-id")).thenReturn(Optional.of(tournament));
        when(bracketRepository.findByTorneoId(100L)).thenReturn(List.of(new LlaveEliminatoriaEntity()));

        assertThrows(BusinessRuleException.class, () -> 
            knockoutBracketService.generateBracket("tourn-id")
        );
    }

    @Test
    void generateBracket_NotEnoughTeams() {
        TournamentEntity tournament = new TournamentEntity();
        tournament.setId(100L);
        when(tournamentRepository.findByTournId("tourn-id")).thenReturn(Optional.of(tournament));
        when(bracketRepository.findByTorneoId(100L)).thenReturn(new ArrayList<>());
        
        EquipoEntity t1 = new EquipoEntity(); t1.setEstadoInscripcion("APROBADO");
        when(teamRepository.findByTorneoId(100L)).thenReturn(List.of(t1));

        assertThrows(BusinessRuleException.class, () -> 
            knockoutBracketService.generateBracket("tourn-id")
        );
    }

    @Test
    void generateBracket_Success_8Teams() {
        TournamentEntity tournament = new TournamentEntity();
        tournament.setId(100L);
        tournament.setTournId("tourn-id");
        
        when(tournamentRepository.findByTournId("tourn-id")).thenReturn(Optional.of(tournament));
        when(bracketRepository.findByTorneoId(100L)).thenReturn(new ArrayList<>());

        List<EquipoEntity> teams = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            EquipoEntity t = new EquipoEntity();
            t.setId((long) i);
            t.setEstadoInscripcion("APROBADO");
            teams.add(t);
        }
        when(teamRepository.findByTorneoId(100L)).thenReturn(teams);

        TournamentBracketResponseDTO response = knockoutBracketService.generateBracket("tourn-id");

        verify(bracketRepository).saveAll(anyList());
        assertNotNull(response);
        assertEquals("tourn-id", response.getTournamentId());
    }

    @Test
    void generateBracket_Success_4Teams() {
        TournamentEntity tournament = new TournamentEntity();
        tournament.setId(100L);
        tournament.setTournId("tourn-id");
        
        when(tournamentRepository.findByTournId("tourn-id")).thenReturn(Optional.of(tournament));
        when(bracketRepository.findByTorneoId(100L)).thenReturn(new ArrayList<>());

        List<EquipoEntity> teams = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            EquipoEntity t = new EquipoEntity();
            t.setId((long) i);
            t.setEstadoInscripcion("APROBADO");
            teams.add(t);
        }
        when(teamRepository.findByTorneoId(100L)).thenReturn(teams);

        TournamentBracketResponseDTO response = knockoutBracketService.generateBracket("tourn-id");

        verify(bracketRepository).saveAll(anyList());
        assertNotNull(response);
    }

    @Test
    void getBracket_TournamentNotFound() {
        when(tournamentRepository.findByTournId(anyString())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> 
            knockoutBracketService.getBracket("invalid-id")
        );
    }

    @Test
    void getBracket_BracketNotGenerated() {
        TournamentEntity tournament = new TournamentEntity();
        tournament.setId(100L);
        when(tournamentRepository.findByTournId("tourn-id")).thenReturn(Optional.of(tournament));
        when(bracketRepository.findByTorneoId(100L)).thenReturn(new ArrayList<>());

        assertThrows(ResourceNotFoundException.class, () -> 
            knockoutBracketService.getBracket("tourn-id")
        );
    }

    @Test
    void getBracket_Success() {
        TournamentEntity tournament = new TournamentEntity();
        tournament.setId(100L);
        tournament.setTournId("tourn-id");
        when(tournamentRepository.findByTournId("tourn-id")).thenReturn(Optional.of(tournament));

        LlaveEliminatoriaEntity key = new LlaveEliminatoriaEntity();
        key.setFase("CUARTOS");
        key.setNumeroLlave(1);
        when(bracketRepository.findByTorneoId(100L)).thenReturn(List.of(key));

        TournamentBracketResponseDTO response = knockoutBracketService.getBracket("tourn-id");

        assertNotNull(response);
        assertNotNull(response.getQuarterFinals());
    }

    @Test
    void advanceBracket_MatchNotFound() {
        when(matchRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> knockoutBracketService.advanceBracket(1L));
    }

    @Test
    void advanceBracket_MatchNotFinalized() {
        PartidoEntity match = new PartidoEntity();
        match.setEstado(MatchStatus.PROGRAMADO);
        when(matchRepository.findById(1L)).thenReturn(Optional.of(match));
        
        knockoutBracketService.advanceBracket(1L);
        
        // Ensure standard logging behavior without exceptions and not calling bracketRepository.findByTorneoId
        verify(bracketRepository, never()).findByTorneoId(anyLong());
    }

    @Test
    void advanceBracket_Success_Promoted() {
        PartidoEntity match = new PartidoEntity();
        match.setId(1L);
        match.setEstado(MatchStatus.FINALIZADO);
        match.setGolesLocal(2);
        match.setGolesVisitante(1);
        
        EquipoEntity home = new EquipoEntity(); home.setId(10L); home.setNombre("A");
        EquipoEntity away = new EquipoEntity(); away.setId(20L); away.setNombre("B");
        match.setEquipoLocal(home);
        match.setEquipoVisitante(away);
        
        TournamentEntity tournament = new TournamentEntity();
        tournament.setId(100L);
        match.setTorneo(tournament);

        when(matchRepository.findById(1L)).thenReturn(Optional.of(match));

        LlaveEliminatoriaEntity currentKey = new LlaveEliminatoriaEntity();
        currentKey.setId(100L);
        currentKey.setPartido(match);
        currentKey.setFase("CUARTOS");
        currentKey.setNumeroLlave(1);

        LlaveEliminatoriaEntity targetKey = new LlaveEliminatoriaEntity();
        targetKey.setId(101L);
        targetKey.setFase("SEMIFINAL");
        targetKey.setNumeroLlave(1);

        when(bracketRepository.findByTorneoId(100L)).thenReturn(List.of(currentKey, targetKey));

        knockoutBracketService.advanceBracket(1L);

        assertEquals(home, currentKey.getGanador());
        verify(bracketRepository, times(2)).save(any(LlaveEliminatoriaEntity.class));
    }

    @Test
    void getPhase_TournamentNotFound() {
        when(tournamentRepository.findByTournId(anyString())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> knockoutBracketService.getPhase("inv", "CUARTOS"));
    }

    @Test
    void getPhase_Success() {
        TournamentEntity tournament = new TournamentEntity();
        tournament.setId(100L);
        when(tournamentRepository.findByTournId("tourn-id")).thenReturn(Optional.of(tournament));
        
        LlaveEliminatoriaEntity key = new LlaveEliminatoriaEntity();
        key.setFase("FINAL");
        key.setNumeroLlave(1);
        
        when(bracketRepository.findByTorneoId(100L)).thenReturn(List.of(key));
        
        List<BracketMatchDTO> phaseMatches = knockoutBracketService.getPhase("tourn-id", "FINAL");
        
        assertEquals(1, phaseMatches.size());
        assertEquals("FINAL", phaseMatches.get(0).getPhase());
    }
}
