package edu.dosw.proyect.persistence.entitys;

import edu.dosw.proyect.persistence.entity.*;
import edu.dosw.proyect.core.models.enums.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;


class TournamentEntityTest {

    @Test
    void tournamentEntity_Builder_ConstruyeCorrectamente() {
        TournamentEntity t = TournamentEntity.builder()
                .id(1L).tournId("TOURN-1").name("TechCup")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusMonths(3))
                .maxTeams(8).costPerTeam(150000)
                .status(TournamentsStatus.DRAFT)
                .build();

        assertEquals("TOURN-1", t.getTournId());
        assertEquals("TechCup", t.getName());
        assertEquals(TournamentsStatus.DRAFT, t.getStatus());
        assertEquals(8, t.getMaxTeams());
    }

    @Test
    void tournamentEntity_Setters_FuncionanCorrectamente() {
        TournamentEntity t = new TournamentEntity();
        t.setName("TechCup 2026");
        t.setStatus(TournamentsStatus.IN_PROGRESS);
        t.setMaxTeams(10);

        assertEquals("TechCup 2026", t.getName());
        assertEquals(TournamentsStatus.IN_PROGRESS, t.getStatus());
    }
}
