package edu.dosw.proyect.services;

import edu.dosw.proyect.controllers.dtos.request.SoccerFieldDTO;
import edu.dosw.proyect.controllers.dtos.request.TournamentConfigurationRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.TournamentConfigurationResponseDTO;
import edu.dosw.proyect.core.exceptions.BusinessRuleException;
import edu.dosw.proyect.core.models.Tournament;
import edu.dosw.proyect.core.models.enums.TournamentsStatus;
import edu.dosw.proyect.core.services.TournamentConfigurationService;
import edu.dosw.proyect.core.services.TournamentService;
import edu.dosw.proyect.persistence.entity.TournamentEntity;
import edu.dosw.proyect.persistence.mapper.TournamentPersistenceMapper;
import edu.dosw.proyect.persistence.repository.SoccerFieldRepository;
import edu.dosw.proyect.persistence.repository.TournamentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TournamentConfigurationServiceTest {

    @Mock private TournamentService tournamentService;
    @Mock private TournamentRepository tournamentRepository;
    @Mock private TournamentPersistenceMapper tournamentMapper;
    @Mock private SoccerFieldRepository soccerFieldRepository;

    @InjectMocks
    private TournamentConfigurationService configuracionService;

    private Tournament buildTournament(TournamentsStatus status) {
        Tournament t = new Tournament();
        t.setId(1L);
        t.setTournId("TOURN-1");
        t.setName("TechCup");
        t.setStatus(status);
        t.setEndDate(LocalDate.now().plusMonths(3));
        t.setOrganizerId(1L);
        return t;
    }

    private TournamentConfigurationRequestDTO buildRequest() {
        SoccerFieldDTO cancha = new SoccerFieldDTO("Cancha 1", "Calle 100");
        return TournamentConfigurationRequestDTO.builder()
                .organizerId(1L)
                .registrationCloseDate(LocalDate.now().plusMonths(1))
                .canchas(List.of(cancha))
                .build();
    }

    @Test
    void configurarTorneo_HappyPath_RetornaResponse() {
        Tournament tournament = buildTournament(TournamentsStatus.DRAFT);
        TournamentEntity entity = new TournamentEntity();
        entity.setId(1L);
        TournamentConfigurationRequestDTO request = buildRequest();

        when(tournamentService.getTournamentById("TOURN-1")).thenReturn(tournament);
        when(tournamentRepository.findByTournId("TOURN-1"))
                .thenReturn(Optional.of(entity));
        when(soccerFieldRepository.save(any())).thenReturn(null);

        TournamentConfigurationResponseDTO response =
                configuracionService.configurarTorneo("TOURN-1", request);

        assertNotNull(response);
        assertEquals("TOURN-1", response.getTournId());
    }

    @Test
    void configurarTorneo_EstadoInvalido_LanzaException() {
        Tournament tournament = buildTournament(TournamentsStatus.FINISHED);
        TournamentConfigurationRequestDTO request = buildRequest();

        when(tournamentService.getTournamentById("TOURN-1")).thenReturn(tournament);

        assertThrows(BusinessRuleException.class,
                () -> configuracionService.configurarTorneo("TOURN-1", request));
    }

    @Test
    void configurarTorneo_OrganizadorDiferente_LanzaException() {
        Tournament tournament = buildTournament(TournamentsStatus.DRAFT);
        tournament.setOrganizerId(99L);
        TournamentConfigurationRequestDTO request = buildRequest();

        when(tournamentService.getTournamentById("TOURN-1")).thenReturn(tournament);

        assertThrows(BusinessRuleException.class,
                () -> configuracionService.configurarTorneo("TOURN-1", request));
    }

    @Test
    void configurarTorneo_FechaCierreInvalida_LanzaException() {
        Tournament tournament = buildTournament(TournamentsStatus.DRAFT);
        tournament.setEndDate(LocalDate.now().minusDays(1));

        TournamentConfigurationRequestDTO request = TournamentConfigurationRequestDTO.builder()
                .organizerId(1L)
                .registrationCloseDate(LocalDate.now().plusMonths(1))
                .canchas(List.of())
                .build();

        when(tournamentService.getTournamentById("TOURN-1")).thenReturn(tournament);

        assertThrows(BusinessRuleException.class,
                () -> configuracionService.configurarTorneo("TOURN-1", request));
    }
}