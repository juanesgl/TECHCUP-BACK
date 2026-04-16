package edu.dosw.proyect.models;

import edu.dosw.proyect.core.models.Cancha;
import edu.dosw.proyect.core.models.Tournament;
import edu.dosw.proyect.core.models.enums.TournamentsStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TournamentTest {

    @Test
    void tournament_Constructor_ConstruyeCorrectamente() {
        Tournament t = new Tournament(
                "TOURN-1",
                "TechCup 2026",
                LocalDate.now(),
                LocalDate.now().plusMonths(2),
                8,
                50000,
                TournamentsStatus.IN_PROGRESS,
                "Reglamento general"
        );

        assertEquals("TOURN-1", t.getTournId());
        assertEquals("TechCup 2026", t.getName());
        assertEquals(8, t.getMaxTeams());
        assertEquals(50000, t.getCostPerTeam());
        assertEquals(TournamentsStatus.IN_PROGRESS, t.getStatus());
        assertEquals("Reglamento general", t.getRegulation());
    }

    @Test
    void tournament_Builder_ConstruyeCorrectamente() {
        Tournament t = Tournament.builder()
                .id(1L)
                .tournId("TOURN-2")
                .name("TechCup 2027")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusMonths(3))
                .maxTeams(16)
                .costPerTeam(100000)
                .status(TournamentsStatus.DRAFT)
                .build();

        assertEquals(1L, t.getId());
        assertEquals("TOURN-2", t.getTournId());
        assertEquals(16, t.getMaxTeams());
        assertEquals(TournamentsStatus.DRAFT, t.getStatus());
    }

    @Test
    void tournament_NoArgsConstructor_CreaVacio() {
        Tournament t = new Tournament();
        assertNull(t.getId());
        assertNull(t.getName());
        assertEquals(0, t.getMaxTeams());
    }

    @Test
    void tournament_CamposTransient_FuncionanCorrectamente() {
        Tournament t = new Tournament();
        t.setRegulation("Reglamento nuevo");
        t.setOrganizerId(5L);
        t.setRegistrationCloseDate(LocalDate.now().plusDays(30));
        t.setSanctions("Sancion por falta");
        t.setCanchas(List.of(new Cancha()));

        assertEquals("Reglamento nuevo", t.getRegulation());
        assertEquals(5L, t.getOrganizerId());
        assertNotNull(t.getRegistrationCloseDate());
        assertEquals(1, t.getCanchas().size());
    }
}