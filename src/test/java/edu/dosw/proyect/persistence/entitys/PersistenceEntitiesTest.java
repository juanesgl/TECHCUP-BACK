package edu.dosw.proyect.persistence.entitys;

import edu.dosw.proyect.core.models.enums.PaymentStatus;
import edu.dosw.proyect.core.models.enums.TacticalFormation;
import edu.dosw.proyect.core.models.enums.EventType;
import edu.dosw.proyect.persistence.entity.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PersistenceEntitiesTest {

    @Test
    void entities_EqualsHashCodeToString_FuncionanCorrectamente() {
        UserEntity u1 = UserEntity.builder().id(1L).name("Juan")
                .email("j@mail.com").password("p").role("PLAYER").build();
        UserEntity u2 = UserEntity.builder().id(1L).name("Juan")
                .email("j@mail.com").password("p").role("PLAYER").build();

        assertEquals(u1, u2);
        assertEquals(u1.hashCode(), u2.hashCode());
        assertNotNull(u1.toString());

        TournamentEntity t1 = new TournamentEntity();
        t1.setId(1L);
        t1.setName("TechCup");
        TournamentEntity t2 = new TournamentEntity();
        t2.setId(1L);
        t2.setName("TechCup");

        assertEquals(t1, t2);
        assertNotNull(t1.toString());

        PlayerEntity j1 = new PlayerEntity();
        j1.setId(1L);
        PlayerEntity j2 = new PlayerEntity();
        j2.setId(1L);

        assertEquals(j1, j2);
        assertNotNull(j1.toString());

        TeamEntity e1 = new TeamEntity();
        e1.setId(1L);
        e1.setNombre("Alpha");
        TeamEntity e2 = new TeamEntity();
        e2.setId(1L);
        e2.setNombre("Alpha");

        assertEquals(e1, e2);
        assertNotNull(e1.toString());

        MatchEntity p1 = new MatchEntity();
        p1.setId(1L);
        MatchEntity p2 = new MatchEntity();
        p2.setId(1L);

        assertEquals(p1, p2);
        assertNotNull(p1.toString());

        PaymentEntity pay1 = new PaymentEntity(1L, 1L, 1L, "url", PaymentStatus.PENDING);
        PaymentEntity pay2 = new PaymentEntity(1L, 1L, 1L, "url", PaymentStatus.PENDING);

        assertEquals(pay1, pay2);
        assertNotNull(pay1.toString());

        InvitationEntity inv1 = InvitationEntity.builder().id(1L).estado("PENDIENTE").build();
        InvitationEntity inv2 = InvitationEntity.builder().id(1L).estado("PENDIENTE").build();

        assertEquals(inv1, inv2);
        assertNotNull(inv1.toString());

        LineupEntity a1 = new LineupEntity();
        a1.setId(1L);
        LineupEntity a2 = new LineupEntity();
        a2.setId(1L);

        assertEquals(a1, a2);
        assertNotNull(a1.toString());

        SoccerFieldEntity c1 = new SoccerFieldEntity();
        c1.setId(1L);
        c1.setNombre("Cancha 1");
        SoccerFieldEntity c2 = new SoccerFieldEntity();
        c2.setId(1L);
        c2.setNombre("Cancha 1");

        assertEquals(c1, c2);
        assertNotNull(c1.toString());

        PaymentEntity pago1 = new PaymentEntity();
        pago1.setId(1L);
        PaymentEntity pago2 = new PaymentEntity();
        pago2.setId(1L);

        assertEquals(pago1, pago2);
        assertNotNull(pago1.toString());

        MatchEventEntity ep1 = new MatchEventEntity();
        ep1.setId(1L);
        MatchEventEntity ep2 = new MatchEventEntity();
        ep2.setId(1L);

        assertEquals(ep1, ep2);
        assertNotNull(ep1.toString());

        KnockoutBracketEntity l1 = new KnockoutBracketEntity();
        l1.setId(1L);
        KnockoutBracketEntity l2 = new KnockoutBracketEntity();
        l2.setId(1L);

        assertEquals(l1, l2);
        assertNotNull(l1.toString());

        // AlineacionJugadorEntity
        LineupPlayerEntity aj1 = new LineupPlayerEntity();
        aj1.setId(1L);
        aj1.setRol("TITULAR");
        aj1.setPosicionEnCancha("Delantero");
        aj1.setNumeroCamiseta(9);
        LineupPlayerEntity aj2 = new LineupPlayerEntity();
        aj2.setId(1L);
        aj2.setRol("TITULAR");
        aj2.setPosicionEnCancha("Delantero");
        aj2.setNumeroCamiseta(9);

        assertEquals(aj1, aj2);
        assertEquals(aj1.hashCode(), aj2.hashCode());
        assertNotNull(aj1.toString());

        // EquipoJugadorEntity
        TeamPlayerEntity ej1 = new TeamPlayerEntity();
        ej1.setId(1L);
        ej1.setActivo(true);
        TeamPlayerEntity ej2 = new TeamPlayerEntity();
        ej2.setId(1L);
        ej2.setActivo(true);

        assertEquals(ej1, ej2);
        assertEquals(ej1.hashCode(), ej2.hashCode());
        assertNotNull(ej1.toString());

        // ConfiguracionTorneoEntity
        TournamentConfigurationEntity d1 = new TournamentConfigurationEntity();
        d1.setId(1L);
        d1.setReglamento("Reglamento");
        d1.setSanciones("Sanciones");
        TournamentConfigurationEntity d2 = new TournamentConfigurationEntity();
        d2.setId(1L);
        d2.setReglamento("Reglamento");
        d2.setSanciones("Sanciones");

        assertEquals(c1, c2);
        assertEquals(c1.hashCode(), c2.hashCode());
        assertNotNull(c1.toString());

        // EstadisticaEquipoEntity
        TeamStatisticsEntity est1 = new TeamStatisticsEntity();
        est1.setId(1L);
        est1.setPuntos(9);
        est1.setPartidosJugados(3);
        est1.setPartidosGanados(3);
        est1.setPartidosEmpatados(0);
        est1.setPartidosPerdidos(0);
        est1.setGolesFavor(6);
        est1.setGolesContra(1);
        est1.setDiferenciaGol(5);
        TeamStatisticsEntity est2 = new TeamStatisticsEntity();
        est2.setId(1L);
        est2.setPuntos(9);
        est2.setPartidosJugados(3);
        est2.setPartidosGanados(3);
        est2.setPartidosEmpatados(0);
        est2.setPartidosPerdidos(0);
        est2.setGolesFavor(6);
        est2.setGolesContra(1);
        est2.setDiferenciaGol(5);

        assertEquals(est1, est2);
        assertEquals(est1.hashCode(), est2.hashCode());
        assertNotNull(est1.toString());
    }

    @Test
    void configuracionTorneoEntity_EqualsHashCodeToString_FuncionanCorrectamente() {
        TournamentEntity torneo = new TournamentEntity();
        torneo.setId(1L);

        TournamentConfigurationEntity c1 = new TournamentConfigurationEntity();
        c1.setId(1L);
        c1.setTorneo(torneo);
        c1.setReglamento("Reglamento");
        c1.setSanciones("Sanciones");
        c1.setFechasImportantes("Fechas");

        TournamentConfigurationEntity c2 = new TournamentConfigurationEntity();
        c2.setId(1L);
        c2.setTorneo(torneo);
        c2.setReglamento("Reglamento");
        c2.setSanciones("Sanciones");
        c2.setFechasImportantes("Fechas");

        assertEquals(c1, c2);
        assertEquals(c1.hashCode(), c2.hashCode());
        assertNotNull(c1.toString());
        assertNotEquals(c1, new TournamentConfigurationEntity());
        assertEquals(c1, c1);
        assertNotEquals(c1, null);
    }

    @Test
    void jugadorEntity_EqualsHashCodeToString_FuncionanCorrectamente() {
        PlayerEntity j1 = new PlayerEntity();
        j1.setId(1L);
        j1.setPosiciones("Delantero");
        j1.setEdad(22);
        j1.setDisponible(true);
        j1.setPerfilCompleto(true);

        PlayerEntity j2 = new PlayerEntity();
        j2.setId(1L);
        j2.setPosiciones("Delantero");
        j2.setEdad(22);
        j2.setDisponible(true);
        j2.setPerfilCompleto(true);

        assertEquals(j1, j2);
        assertEquals(j1.hashCode(), j2.hashCode());
        assertNotNull(j1.toString());
        assertNotEquals(j1, new PlayerEntity());
        assertEquals(j1, j1);
        assertNotEquals(j1, null);
    }

    @Test
    void equipoEntity_EqualsHashCodeToString_FuncionanCorrectamente() {
        TeamEntity e1 = new TeamEntity();
        e1.setId(1L);
        e1.setNombre("Alpha FC");
        e1.setColorUniformeLocal("Rojo");
        e1.setEstadoInscripcion("APROBADO");

        TeamEntity e2 = new TeamEntity();
        e2.setId(1L);
        e2.setNombre("Alpha FC");
        e2.setColorUniformeLocal("Rojo");
        e2.setEstadoInscripcion("APROBADO");

        assertEquals(e1, e2);
        assertEquals(e1.hashCode(), e2.hashCode());
        assertNotNull(e1.toString());
        assertNotEquals(e1, new TeamEntity());
        assertEquals(e1, e1);
        assertNotEquals(e1, null);
    }

    @Test
    void pagoEntity_EqualsHashCodeToString_FuncionanCorrectamente() {
        PaymentEntity p1 = new PaymentEntity();
        p1.setId(1L);
        p1.setStatus(PaymentStatus.PENDING);
        p1.setFileUrl("http://url.pdf");

        PaymentEntity p2 = new PaymentEntity();
        p2.setId(1L);
        p2.setStatus(PaymentStatus.PENDING);
        p2.setFileUrl("http://url.pdf");

        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());
        assertNotNull(p1.toString());
        assertNotEquals(p1, new PaymentEntity());
        assertEquals(p1, p1);
        assertNotEquals(p1, null);
    }

    @Test
    void llaveEliminatoriaEntity_EqualsHashCodeToString_FuncionanCorrectamente() {
        KnockoutBracketEntity l1 = new KnockoutBracketEntity();
        l1.setId(1L);
        l1.setFase("Semifinal");
        l1.setNumeroLlave(1);

        KnockoutBracketEntity l2 = new KnockoutBracketEntity();
        l2.setId(1L);
        l2.setFase("Semifinal");
        l2.setNumeroLlave(1);

        assertEquals(l1, l2);
        assertEquals(l1.hashCode(), l2.hashCode());
        assertNotNull(l1.toString());
        assertNotEquals(l1, new KnockoutBracketEntity());
        assertEquals(l1, l1);
        assertNotEquals(l1, null);
    }

    @Test
    void paymentEntity_EqualsHashCodeToString_FuncionanCorrectamente() {
        PaymentEntity p1 = new PaymentEntity(1L, 2L, 3L, "url", PaymentStatus.PENDING);
        PaymentEntity p2 = new PaymentEntity(1L, 2L, 3L, "url", PaymentStatus.PENDING);

        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());
        assertNotNull(p1.toString());
        assertNotEquals(p1, new PaymentEntity());
        assertEquals(p1, p1);
        assertNotEquals(p1, null);
    }

    @Test
    void invitacionEntity_EqualsHashCodeToString_FuncionanCorrectamente() {
        InvitationEntity i1 = InvitationEntity.builder()
                .id(1L).estado("PENDIENTE").build();
        InvitationEntity i2 = InvitationEntity.builder()
                .id(1L).estado("PENDIENTE").build();

        assertEquals(i1, i2);
        assertEquals(i1.hashCode(), i2.hashCode());
        assertNotNull(i1.toString());
        assertNotEquals(i1, new InvitationEntity());
        assertEquals(i1, i1);
        assertNotEquals(i1, null);
    }

    @Test
    void partidoEntity_EqualsHashCodeToString_FuncionanCorrectamente() {
        MatchEntity p1 = new MatchEntity();
        p1.setId(1L);
        p1.setGolesLocal(2);
        p1.setGolesVisitante(1);
        p1.setFase("Grupos");

        MatchEntity p2 = new MatchEntity();
        p2.setId(1L);
        p2.setGolesLocal(2);
        p2.setGolesVisitante(1);
        p2.setFase("Grupos");

        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());
        assertNotNull(p1.toString());
        assertNotEquals(p1, new MatchEntity());
        assertEquals(p1, p1);
        assertNotEquals(p1, null);
    }

    @Test
    void alineacionEntity_EqualsHashCodeToString_FuncionanCorrectamente() {
        LineupEntity a1 = new LineupEntity();
        a1.setId(1L);
        a1.setFormacion(TacticalFormation.F_1_2_3_1);

        LineupEntity a2 = new LineupEntity();
        a2.setId(1L);
        a2.setFormacion(TacticalFormation.F_1_2_3_1);

        assertEquals(a1, a2);
        assertEquals(a1.hashCode(), a2.hashCode());
        assertNotNull(a1.toString());
        assertNotEquals(a1, new LineupEntity());
        assertEquals(a1, a1);
        assertNotEquals(a1, null);
    }

    @Test
    void eventoPartidoEntity_EqualsHashCodeToString_FuncionanCorrectamente() {
        MatchEventEntity ep1 = new MatchEventEntity();
        ep1.setId(1L);
        ep1.setEventType(EventType.GOL);
        ep1.setMinuto(45);
        ep1.setDescripcion("Gol de cabeza");

        MatchEventEntity ep2 = new MatchEventEntity();
        ep2.setId(1L);
        ep2.setEventType(EventType.GOL);
        ep2.setMinuto(45);
        ep2.setDescripcion("Gol de cabeza");

        assertEquals(ep1, ep2);
        assertEquals(ep1.hashCode(), ep2.hashCode());
        assertNotNull(ep1.toString());
        assertNotEquals(ep1, new MatchEventEntity());
        assertEquals(ep1, ep1);
        assertNotEquals(ep1, null);
    }

    @Test
    void userEntity_EqualsHashCodeToString_FuncionanCorrectamente() {
        UserEntity u1 = UserEntity.builder()
                .id(1L).name("Juan").lastName("Perez")
                .email("juan@mail.com").password("pass")
                .role("PLAYER").active(true)
                .academicProgram("sistemas").build();
        UserEntity u2 = UserEntity.builder()
                .id(1L).name("Juan").lastName("Perez")
                .email("juan@mail.com").password("pass")
                .role("PLAYER").active(true)
                .academicProgram("sistemas").build();

        assertEquals(u1, u2);
        assertEquals(u1.hashCode(), u2.hashCode());
        assertNotNull(u1.toString());
        assertNotEquals(u1, new UserEntity());
        assertEquals(u1, u1);
        assertNotEquals(u1, null);
    }

    @Test
    void alineacionJugadorEntity_EqualsHashCodeToString_FuncionanCorrectamente() {
        LineupPlayerEntity aj1 = new LineupPlayerEntity();
        aj1.setId(1L);
        aj1.setRol("TITULAR");
        aj1.setPosicionEnCancha("Delantero");
        aj1.setNumeroCamiseta(9);

        LineupPlayerEntity aj2 = new LineupPlayerEntity();
        aj2.setId(1L);
        aj2.setRol("TITULAR");
        aj2.setPosicionEnCancha("Delantero");
        aj2.setNumeroCamiseta(9);

        assertEquals(aj1, aj2);
        assertEquals(aj1.hashCode(), aj2.hashCode());
        assertNotNull(aj1.toString());
        assertNotEquals(aj1, new LineupPlayerEntity());
        assertEquals(aj1, aj1);
        assertNotEquals(aj1, null);
    }

    @Test
    void canchaEntity_EqualsHashCodeToString_FuncionanCorrectamente() {
        SoccerFieldEntity c1 = new SoccerFieldEntity();
        c1.setId(1L);
        c1.setNombre("Cancha 1");
        c1.setDireccion("Calle 100");
        c1.setDescripcion("Principal");

        SoccerFieldEntity c2 = new SoccerFieldEntity();
        c2.setId(1L);
        c2.setNombre("Cancha 1");
        c2.setDireccion("Calle 100");
        c2.setDescripcion("Principal");

        assertEquals(c1, c2);
        assertEquals(c1.hashCode(), c2.hashCode());
        assertNotNull(c1.toString());
        assertNotEquals(c1, new SoccerFieldEntity());
        assertEquals(c1, c1);
        assertNotEquals(c1, null);
    }

    @Test
    void tournamentEntity_EqualsHashCodeToString_FuncionanCorrectamente() {
        TournamentEntity t1 = new TournamentEntity();
        t1.setId(1L);
        t1.setTournId("TOURN-1");
        t1.setName("TechCup");
        t1.setMaxTeams(8);
        t1.setCostPerTeam(150000);

        TournamentEntity t2 = new TournamentEntity();
        t2.setId(1L);
        t2.setTournId("TOURN-1");
        t2.setName("TechCup");
        t2.setMaxTeams(8);
        t2.setCostPerTeam(150000);

        assertEquals(t1, t2);
        assertEquals(t1.hashCode(), t2.hashCode());
        assertNotNull(t1.toString());
        assertNotEquals(t1, new TournamentEntity());
        assertEquals(t1, t1);
        assertNotEquals(t1, null);
    }
}
