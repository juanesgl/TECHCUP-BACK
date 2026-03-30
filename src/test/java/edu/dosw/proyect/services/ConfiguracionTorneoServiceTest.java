package edu.dosw.proyect.services;

import edu.dosw.proyect.controllers.dtos.request.CanchaDTO;
import edu.dosw.proyect.controllers.dtos.request.ConfiguracionTorneoRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.ConfiguracionTorneoResponseDTO;
import edu.dosw.proyect.core.exceptions.BusinessRuleException;
import edu.dosw.proyect.core.models.Tournament;
import edu.dosw.proyect.core.models.enums.TournamentsStatus;
import edu.dosw.proyect.persistence.repository.CanchaRepository;
import edu.dosw.proyect.core.services.ConfiguracionTorneoService;
import edu.dosw.proyect.core.services.TournamentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConfiguracionTorneoServiceTest {

    @Mock
    private TournamentService tournamentService;

    @Mock
    private CanchaRepository canchaRepository;

    @InjectMocks
    private ConfiguracionTorneoService service;

    private Tournament tournament;
    private ConfiguracionTorneoRequestDTO requestDto;

    @BeforeEach
    void setUp() {
        tournament = new Tournament();
        tournament.setTournId("T1");
        tournament.setStatus(TournamentsStatus.DRAFT);
        tournament.setEndDate(LocalDate.now().plusDays(10));
        tournament.setOrganizerId(1L);

        List<CanchaDTO> canchas = new ArrayList<>();
        canchas.add(new CanchaDTO("Cancha 1", "Ubicacion 1"));

        requestDto = ConfiguracionTorneoRequestDTO.builder()
                .registrationCloseDate(LocalDate.now().plusDays(5))
                .canchas(canchas)
                .organizerId(1L)
                .importantDates("Enero 1")
                .matchSchedules("Fines de semana")
                .sanctions("Tarjeta roja 1 fecha")
                .regulation("Reglamento Oficial")
                .build();
    }

    @Test
    void configurarTorneo_Exito() {
        when(tournamentService.getTournamentById("T1")).thenReturn(tournament);
        when(canchaRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        ConfiguracionTorneoResponseDTO response = service.configurarTorneo("T1", requestDto);

        assertNotNull(response);
        assertEquals("Los parÃ¡metros fueron guardados de manera exitosa", response.getMessage());
        assertEquals("T1", response.getTournId());
        assertEquals(1, tournament.getCanchas().size());
        assertEquals("Reglamento Oficial", tournament.getRegulation());
        assertEquals("Enero 1", tournament.getImportantDates());
    }

    @Test
    void configurarTorneo_EstadoInvalido() {
        tournament.setStatus(TournamentsStatus.FINISHED);
        when(tournamentService.getTournamentById("T1")).thenReturn(tournament);

        BusinessRuleException ex = assertThrows(BusinessRuleException.class,
                () -> service.configurarTorneo("T1", requestDto));
        assertEquals("La configuraciÃ³n solo estÃ¡ permitida cuando el torneo estÃ¡ en estado Borrador o Activo.",
                ex.getMessage());
    }

    @Test
    void configurarTorneo_OrganizadorInvalido() {
        tournament.setOrganizerId(2L);
        when(tournamentService.getTournamentById("T1")).thenReturn(tournament);

        BusinessRuleException ex = assertThrows(BusinessRuleException.class,
                () -> service.configurarTorneo("T1", requestDto));
        assertEquals("No tiene permisos para configurar este torneo. Solo el organizador puede hacerlo.",
                ex.getMessage());
    }

    @Test
    void configurarTorneo_FechaInvalida() {
        requestDto.setRegistrationCloseDate(LocalDate.now().plusDays(15));
        when(tournamentService.getTournamentById("T1")).thenReturn(tournament);

        BusinessRuleException ex = assertThrows(BusinessRuleException.class,
                () -> service.configurarTorneo("T1", requestDto));
        assertEquals("La fecha de cierre de inscripciones debe ser anterior a la fecha de finalizaciÃ³n del torneo.",
                ex.getMessage());
    }
}

