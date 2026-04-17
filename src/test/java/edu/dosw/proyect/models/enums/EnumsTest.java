package edu.dosw.proyect.models.enums;

import edu.dosw.proyect.core.models.enums.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EnumsTest {

    // ─── EstadoInvitacion ─────────────────────────────────

    @Test
    void estadoInvitacion_ValoresExisten() {
        assertNotNull(InvitationStatus.PENDIENTE);
        assertNotNull(InvitationStatus.ACEPTADA);
        assertNotNull(InvitationStatus.RECHAZADA);
        assertEquals(3, InvitationStatus.values().length);
    }

    // ─── MatchStatus ──────────────────────────────────────

    @Test
    void estadoPartido_ValoresExisten() {
        assertNotNull(MatchStatus.PROGRAMADO);
        assertNotNull(MatchStatus.EN_JUEGO);
        assertNotNull(MatchStatus.FINALIZADO);
        assertNotNull(MatchStatus.CANCELADO);
        assertEquals(4, MatchStatus.values().length);
    }

    // ─── FieldPosition ────────────────────────────────────

    @Test
    void fieldPosition_ValoresExisten() {
        assertNotNull(FieldPosition.GOALKEEPER);
        assertNotNull(FieldPosition.DEFENDER);
        assertNotNull(FieldPosition.MIDFIELDER);
        assertNotNull(FieldPosition.FORWARD);
        assertEquals(4, FieldPosition.values().length);
    }

    // ─── Gender ───────────────────────────────────────────

    @Test
    void gender_ValoresExisten() {
        assertNotNull(Gender.MASCULINO);
        assertNotNull(Gender.FEMENINO);
        assertNotNull(Gender.OTRO);
        assertEquals(3, Gender.values().length);
    }

    // ─── LineupStatus ─────────────────────────────────────

    @Test
    void lineupStatus_ValoresExisten() {
        assertNotNull(LineupStatus.DRAFT);
        assertNotNull(LineupStatus.SAVED);
        assertNotNull(LineupStatus.LOCKED);
        assertEquals(3, LineupStatus.values().length);
    }


    // ─── RespuestaInvitacion ──────────────────────────────

    @Test
    void respuestaInvitacion_ValoresExisten() {
        assertNotNull(InvitationResponse.ACEPTAR);
        assertNotNull(InvitationResponse.RECHAZAR);
        assertEquals(2, InvitationResponse.values().length);
    }

    // ─── TipoEvento ───────────────────────────────────────

    @Test
    void tipoEvento_ValoresExisten() {
        assertNotNull(EventType.GOL);
        assertNotNull(EventType.TARJETA_AMARILLA);
        assertNotNull(EventType.TARJETA_ROJA);
        assertEquals(3, EventType.values().length);
    }

    // ─── UserRole ─────────────────────────────────────────

    @Test
    void userRole_ValoresExisten() {
        assertNotNull(UserRole.ORGANIZER);
        assertNotNull(UserRole.CAPTAIN);
        assertNotNull(UserRole.PLAYER);
        assertNotNull(UserRole.REFEREE);
        assertNotNull(UserRole.ADMINISTRATOR);
        assertEquals(5, UserRole.values().length);
    }

    @Test
    void userRole_GetDisplayName_RetornaNombre() {
        assertEquals("Organizador", UserRole.ORGANIZER.getDisplayName());
        assertEquals("Jugador", UserRole.PLAYER.getDisplayName());
        assertEquals("Administrador", UserRole.ADMINISTRATOR.getDisplayName());
    }

    @Test
    void userRole_HasPermission_OrganizerPuedeCrearTorneo() {
        assertTrue(UserRole.ORGANIZER.hasPermission("CREATE_TOURNAMENT"));
        assertFalse(UserRole.PLAYER.hasPermission("CREATE_TOURNAMENT"));
    }

    @Test
    void userRole_HasPermission_CaptainPuedeCrearEquipo() {
        assertTrue(UserRole.CAPTAIN.hasPermission("CREATE_TEAM"));
        assertFalse(UserRole.REFEREE.hasPermission("CREATE_TEAM"));
    }

    @Test
    void userRole_GetPermissions_RetornaCopiaSinModificar() {
        var perms = UserRole.ORGANIZER.getPermissions();
        assertNotNull(perms);
        assertFalse(perms.isEmpty());
        assertTrue(perms.contains("CREATE_TOURNAMENT"));
    }

    @Test
    void tacticalFormation_ValoresExisten() {
        assertEquals(5, TacticalFormation.values().length);
        assertNotNull(TacticalFormation.F_1_2_3_1);
        assertNotNull(TacticalFormation.F_1_3_2_1);
        assertNotNull(TacticalFormation.F_1_2_2_2);
        assertNotNull(TacticalFormation.F_1_3_1_2);
        assertNotNull(TacticalFormation.F_1_2_1_3);
    }

    @Test
    void tacticalFormation_GetDisplayName_RetornaCorrectamente() {
        assertEquals("1-2-3-1", TacticalFormation.F_1_2_3_1.getDisplayName());
        assertEquals("1-3-2-1", TacticalFormation.F_1_3_2_1.getDisplayName());
        assertEquals("1-2-2-2", TacticalFormation.F_1_2_2_2.getDisplayName());
        assertEquals("1-3-1-2", TacticalFormation.F_1_3_1_2.getDisplayName());
        assertEquals("1-2-1-3", TacticalFormation.F_1_2_1_3.getDisplayName());
    }

    @Test
    void tacticalFormation_GetTotalStarters_RetornaSiete() {
        assertEquals(7, TacticalFormation.F_1_2_3_1.getTotalStarters());
        assertEquals(7, TacticalFormation.F_1_3_2_1.getTotalStarters());
        assertEquals(7, TacticalFormation.F_1_2_2_2.getTotalStarters());
        assertEquals(7, TacticalFormation.F_1_3_1_2.getTotalStarters());
        assertEquals(7, TacticalFormation.F_1_2_1_3.getTotalStarters());
    }

    @Test
    void tacticalFormation_F_1_2_3_1_PosicionesCorrectas() {
        assertEquals(1, TacticalFormation.F_1_2_3_1.getGoalkeepers());
        assertEquals(2, TacticalFormation.F_1_2_3_1.getDefenders());
        assertEquals(3, TacticalFormation.F_1_2_3_1.getMidfielders());
        assertEquals(1, TacticalFormation.F_1_2_3_1.getForwards());
    }

    @Test
    void tacticalFormation_F_1_3_2_1_PosicionesCorrectas() {
        assertEquals(1, TacticalFormation.F_1_3_2_1.getGoalkeepers());
        assertEquals(3, TacticalFormation.F_1_3_2_1.getDefenders());
        assertEquals(2, TacticalFormation.F_1_3_2_1.getMidfielders());
        assertEquals(1, TacticalFormation.F_1_3_2_1.getForwards());
    }

    @Test
    void tacticalFormation_F_1_2_1_3_PosicionesCorrectas() {
        assertEquals(1, TacticalFormation.F_1_2_1_3.getGoalkeepers());
        assertEquals(2, TacticalFormation.F_1_2_1_3.getDefenders());
        assertEquals(1, TacticalFormation.F_1_2_1_3.getMidfielders());
        assertEquals(3, TacticalFormation.F_1_2_1_3.getForwards());
    }
}