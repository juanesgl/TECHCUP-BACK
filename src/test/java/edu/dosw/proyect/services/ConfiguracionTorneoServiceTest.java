package edu.dosw.proyect.services;

import edu.dosw.proyect.controllers.dtos.request.CanchaDTO;
import edu.dosw.proyect.controllers.dtos.request.ConfiguracionTorneoRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.ConfiguracionTorneoResponseDTO;
import edu.dosw.proyect.core.exceptions.BusinessRuleException;
import edu.dosw.proyect.core.models.Tournament;
import edu.dosw.proyect.core.models.enums.TournamentsStatus;
import edu.dosw.proyect.core.services.ConfiguracionTorneoService;
import edu.dosw.proyect.core.services.TournamentService;
import edu.dosw.proyect.persistence.entity.TournamentEntity;
import edu.dosw.proyect.persistence.mapper.TournamentPersistenceMapper;
import edu.dosw.proyect.persistence.repository.CanchaRepository;
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
class ConfiguracionTorneoServiceTest {

    @Mock private TournamentService tournamentService;
    @Mock private TournamentRepository tournamentRepository;
    @Mock private TournamentPersistenceMapper tournamentMapper;
    @Mock private CanchaRepository canchaRepository;

    @InjectMocks
    private ConfiguracionTorneoService configuracionService;

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

    private ConfiguracionTorneoRequestDTO buildRequest() {
        CanchaDTO cancha = new CanchaDTO("Cancha 1", "Calle 100");
        return ConfiguracionTorneoRequestDTO.builder()
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
        ConfiguracionTorneoRequestDTO request = buildRequest();

        when(tournamentService.getTournamentById("TOURN-1")).thenReturn(tournament);
        when(tournamentRepository.findByTournId("TOURN-1"))
                .thenReturn(Optional.of(entity));
        when(canchaRepository.save(any())).thenReturn(null);

        ConfiguracionTorneoResponseDTO response =
                configuracionService.configurarTorneo("TOURN-1", request);

        assertNotNull(response);
        assertEquals("TOURN-1", response.getTournId());
    }

    @Test
    void configurarTorneo_EstadoInvalido_LanzaException() {
        Tournament tournament = buildTournament(TournamentsStatus.FINISHED);
        ConfiguracionTorneoRequestDTO request = buildRequest();

        when(tournamentService.getTournamentById("TOURN-1")).thenReturn(tournament);

        assertThrows(BusinessRuleException.class,
                () -> configuracionService.configurarTorneo("TOURN-1", request));
    }

    @Test
    void configurarTorneo_OrganizadorDiferente_LanzaException() {
        Tournament tournament = buildTournament(TournamentsStatus.DRAFT);
        tournament.setOrganizerId(99L);
        ConfiguracionTorneoRequestDTO request = buildRequest();

        when(tournamentService.getTournamentById("TOURN-1")).thenReturn(tournament);

        assertThrows(BusinessRuleException.class,
                () -> configuracionService.configurarTorneo("TOURN-1", request));
    }

    @Test
    void configurarTorneo_FechaCierreInvalida_LanzaException() {
        Tournament tournament = buildTournament(TournamentsStatus.DRAFT);
        tournament.setEndDate(LocalDate.now().minusDays(1));

        ConfiguracionTorneoRequestDTO request = ConfiguracionTorneoRequestDTO.builder()
                .organizerId(1L)
                .registrationCloseDate(LocalDate.now().plusMonths(1))
                .canchas(List.of())
                .build();

        when(tournamentService.getTournamentById("TOURN-1")).thenReturn(tournament);

        assertThrows(BusinessRuleException.class,
                () -> configuracionService.configurarTorneo("TOURN-1", request));
    }
}