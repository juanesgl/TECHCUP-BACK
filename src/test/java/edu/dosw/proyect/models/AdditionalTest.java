package edu.dosw.proyect.models;

import edu.dosw.proyect.core.models.*;
import edu.dosw.proyect.core.models.enums.FieldPosition;
import edu.dosw.proyect.core.models.enums.LineupStatus;
import edu.dosw.proyect.core.models.enums.TacticalFormation;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AdditionalTest {


    @Test
    void admin_Constructor_ConstruyeCorrectamente() {
        Admin admin = new Admin(
                "Admin Test", "admin@mail.com", "pass", null);

        assertEquals("Admin Test", admin.getName());
        assertEquals("admin@mail.com", admin.getEmail());
        assertEquals("ADMINISTRATOR", admin.getRole());
    }

    @Test
    void admin_NoArgsConstructor_CreaVacio() {
        Admin admin = new Admin();
        assertNull(admin.getName());
    }


    @Test
    void graduate_Constructor_ConstruyeCorrectamente() {
        Graduate g = new Graduate(
                "Grad Test", "grad@gmail.com", "pass", null);

        assertEquals("Grad Test", g.getName());
        assertEquals("PLAYER", g.getRole());
    }

    @Test
    void graduate_NoArgsConstructor_CreaVacio() {
        Graduate g = new Graduate();
        assertNull(g.getName());
    }


    @Test
    void organizer_Constructor_ConstruyeCorrectamente() {
        Organizer o = new Organizer(
                "Org Test", "org@mail.com", "pass");

        assertEquals("Org Test", o.getName());
        assertEquals("ORGANIZER", o.getRole());
    }

    @Test
    void organizer_NoArgsConstructor_CreaVacio() {
        Organizer o = new Organizer();
        assertNull(o.getName());
    }


    @Test
    void starterEntry_Builder_ConstruyeCorrectamente() {
        StarterEntry entry = StarterEntry.builder()
                .playerId(1L)
                .playerName("Juan")
                .fieldPosition(FieldPosition.FORWARD)
                .build();

        assertEquals(1L, entry.getPlayerId());
        assertEquals("Juan", entry.getPlayerName());
        assertEquals(FieldPosition.FORWARD, entry.getFieldPosition());
    }

    @Test
    void starterEntry_NoArgsConstructor_CreaVacio() {
        StarterEntry entry = new StarterEntry();
        assertNull(entry.getPlayerId());
        assertNull(entry.getPlayerName());
    }


    @Test
    void teamLineup_Builder_ConstruyeCorrectamente() {
        StarterEntry starter = StarterEntry.builder()
                .playerId(1L)
                .playerName("Juan")
                .fieldPosition(FieldPosition.GOALKEEPER)
                .build();

        TeamLineup lineup = TeamLineup.builder()
                .id(1L)
                .teamId(10L)
                .teamName("Alpha FC")
                .matchId(5L)
                .captainId(2L)
                .formation(TacticalFormation.F_1_2_3_1)
                .status(LineupStatus.DRAFT)
                .createdAt(LocalDateTime.now())
                .starters(List.of(starter))
                .reserveIds(List.of(3L, 4L))
                .build();

        assertEquals(1L, lineup.getId());
        assertEquals("Alpha FC", lineup.getTeamName());
        assertEquals(TacticalFormation.F_1_2_3_1, lineup.getFormation());
        assertEquals(LineupStatus.DRAFT, lineup.getStatus());
        assertEquals(1, lineup.getStarters().size());
        assertEquals(2, lineup.getReserveIds().size());
    }

    @Test
    void teamLineup_NoArgsConstructor_ListasInicializadas() {
        TeamLineup lineup = new TeamLineup();
        assertNull(lineup.getId());
        assertNotNull(lineup.getStarters());
        assertNotNull(lineup.getReserveIds());
        assertTrue(lineup.getStarters().isEmpty());
    }
}