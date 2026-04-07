package edu.dosw.proyect.persistence.entitys;

import edu.dosw.proyect.core.models.enums.PaymentStatus;
import edu.dosw.proyect.core.models.enums.TacticalFormation;
import edu.dosw.proyect.core.models.enums.TipoEvento;
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

        JugadorEntity j1 = new JugadorEntity();
        j1.setId(1L);
        JugadorEntity j2 = new JugadorEntity();
        j2.setId(1L);

        assertEquals(j1, j2);
        assertNotNull(j1.toString());

        EquipoEntity e1 = new EquipoEntity();
        e1.setId(1L);
        e1.setNombre("Alpha");
        EquipoEntity e2 = new EquipoEntity();
        e2.setId(1L);
        e2.setNombre("Alpha");

        assertEquals(e1, e2);
        assertNotNull(e1.toString());

        PartidoEntity p1 = new PartidoEntity();
        p1.setId(1L);
        PartidoEntity p2 = new PartidoEntity();
        p2.setId(1L);

        assertEquals(p1, p2);
        assertNotNull(p1.toString());

        PaymentEntity pay1 = new PaymentEntity(1L, 1L, 1L, "url", PaymentStatus.PENDING);
        PaymentEntity pay2 = new PaymentEntity(1L, 1L, 1L, "url", PaymentStatus.PENDING);

        assertEquals(pay1, pay2);
        assertNotNull(pay1.toString());

        InvitacionEntity inv1 = InvitacionEntity.builder().id(1L).estado("PENDIENTE").build();
        InvitacionEntity inv2 = InvitacionEntity.builder().id(1L).estado("PENDIENTE").build();

        assertEquals(inv1, inv2);
        assertNotNull(inv1.toString());

        AlineacionEntity a1 = new AlineacionEntity();
        a1.setId(1L);
        AlineacionEntity a2 = new AlineacionEntity();
        a2.setId(1L);

        assertEquals(a1, a2);
        assertNotNull(a1.toString());

        CanchaEntity c1 = new CanchaEntity();
        c1.setId(1L);
        c1.setNombre("Cancha 1");
        CanchaEntity c2 = new CanchaEntity();
        c2.setId(1L);
        c2.setNombre("Cancha 1");

        assertEquals(c1, c2);
        assertNotNull(c1.toString());

        PagoEntity pago1 = new PagoEntity();
        pago1.setId(1L);
        PagoEntity pago2 = new PagoEntity();
        pago2.setId(1L);

        assertEquals(pago1, pago2);
        assertNotNull(pago1.toString());

        EventoPartidoEntity ep1 = new EventoPartidoEntity();
        ep1.setId(1L);
        EventoPartidoEntity ep2 = new EventoPartidoEntity();
        ep2.setId(1L);

        assertEquals(ep1, ep2);
        assertNotNull(ep1.toString());

        LlaveEliminatoriaEntity l1 = new LlaveEliminatoriaEntity();
        l1.setId(1L);
        LlaveEliminatoriaEntity l2 = new LlaveEliminatoriaEntity();
        l2.setId(1L);

        assertEquals(l1, l2);
        assertNotNull(l1.toString());

        // AlineacionJugadorEntity
        AlineacionJugadorEntity aj1 = new AlineacionJugadorEntity();
        aj1.setId(1L);
        aj1.setRol("TITULAR");
        aj1.setPosicionEnCancha("Delantero");
        aj1.setNumeroCamiseta(9);
        AlineacionJugadorEntity aj2 = new AlineacionJugadorEntity();
        aj2.setId(1L);
        aj2.setRol("TITULAR");
        aj2.setPosicionEnCancha("Delantero");
        aj2.setNumeroCamiseta(9);

        assertEquals(aj1, aj2);
        assertEquals(aj1.hashCode(), aj2.hashCode());
        assertNotNull(aj1.toString());

        // EquipoJugadorEntity
        EquipoJugadorEntity ej1 = new EquipoJugadorEntity();
        ej1.setId(1L);
        ej1.setActivo(true);
        EquipoJugadorEntity ej2 = new EquipoJugadorEntity();
        ej2.setId(1L);
        ej2.setActivo(true);

        assertEquals(ej1, ej2);
        assertEquals(ej1.hashCode(), ej2.hashCode());
        assertNotNull(ej1.toString());

        // ConfiguracionTorneoEntity
        ConfiguracionTorneoEntity d1 = new ConfiguracionTorneoEntity();
        d1.setId(1L);
        d1.setReglamento("Reglamento");
        d1.setSanciones("Sanciones");
        ConfiguracionTorneoEntity d2 = new ConfiguracionTorneoEntity();
        d2.setId(1L);
        d2.setReglamento("Reglamento");
        d2.setSanciones("Sanciones");

        assertEquals(c1, c2);
        assertEquals(c1.hashCode(), c2.hashCode());
        assertNotNull(c1.toString());

        // EstadisticaEquipoEntity
        EstadisticasEquipoEntity est1 = new EstadisticasEquipoEntity();
        est1.setId(1L);
        est1.setPuntos(9);
        est1.setPartidosJugados(3);
        est1.setPartidosGanados(3);
        est1.setPartidosEmpatados(0);
        est1.setPartidosPerdidos(0);
        est1.setGolesFavor(6);
        est1.setGolesContra(1);
        est1.setDiferenciaGol(5);
        EstadisticasEquipoEntity est2 = new EstadisticasEquipoEntity();
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

        ConfiguracionTorneoEntity c1 = new ConfiguracionTorneoEntity();
        c1.setId(1L);
        c1.setTorneo(torneo);
        c1.setReglamento("Reglamento");
        c1.setSanciones("Sanciones");
        c1.setFechasImportantes("Fechas");

        ConfiguracionTorneoEntity c2 = new ConfiguracionTorneoEntity();
        c2.setId(1L);
        c2.setTorneo(torneo);
        c2.setReglamento("Reglamento");
        c2.setSanciones("Sanciones");
        c2.setFechasImportantes("Fechas");

        assertEquals(c1, c2);
        assertEquals(c1.hashCode(), c2.hashCode());
        assertNotNull(c1.toString());
        assertNotEquals(c1, new ConfiguracionTorneoEntity());
        assertEquals(c1, c1);
        assertNotEquals(c1, null);
    }

    @Test
    void jugadorEntity_EqualsHashCodeToString_FuncionanCorrectamente() {
        JugadorEntity j1 = new JugadorEntity();
        j1.setId(1L);
        j1.setPosiciones("Delantero");
        j1.setEdad(22);
        j1.setDisponible(true);
        j1.setPerfilCompleto(true);

        JugadorEntity j2 = new JugadorEntity();
        j2.setId(1L);
        j2.setPosiciones("Delantero");
        j2.setEdad(22);
        j2.setDisponible(true);
        j2.setPerfilCompleto(true);

        assertEquals(j1, j2);
        assertEquals(j1.hashCode(), j2.hashCode());
        assertNotNull(j1.toString());
        assertNotEquals(j1, new JugadorEntity());
        assertEquals(j1, j1);
        assertNotEquals(j1, null);
    }

    @Test
    void equipoEntity_EqualsHashCodeToString_FuncionanCorrectamente() {
        EquipoEntity e1 = new EquipoEntity();
        e1.setId(1L);
        e1.setNombre("Alpha FC");
        e1.setColorUniformeLocal("Rojo");
        e1.setEstadoInscripcion("APROBADO");

        EquipoEntity e2 = new EquipoEntity();
        e2.setId(1L);
        e2.setNombre("Alpha FC");
        e2.setColorUniformeLocal("Rojo");
        e2.setEstadoInscripcion("APROBADO");

        assertEquals(e1, e2);
        assertEquals(e1.hashCode(), e2.hashCode());
        assertNotNull(e1.toString());
        assertNotEquals(e1, new EquipoEntity());
        assertEquals(e1, e1);
        assertNotEquals(e1, null);
    }

    @Test
    void pagoEntity_EqualsHashCodeToString_FuncionanCorrectamente() {
        PagoEntity p1 = new PagoEntity();
        p1.setId(1L);
        p1.setEstado("PENDIENTE");
        p1.setComprobanteUrl("http://url.pdf");

        PagoEntity p2 = new PagoEntity();
        p2.setId(1L);
        p2.setEstado("PENDIENTE");
        p2.setComprobanteUrl("http://url.pdf");

        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());
        assertNotNull(p1.toString());
        assertNotEquals(p1, new PagoEntity());
        assertEquals(p1, p1);
        assertNotEquals(p1, null);
    }

    @Test
    void llaveEliminatoriaEntity_EqualsHashCodeToString_FuncionanCorrectamente() {
        LlaveEliminatoriaEntity l1 = new LlaveEliminatoriaEntity();
        l1.setId(1L);
        l1.setFase("Semifinal");
        l1.setNumeroLlave(1);

        LlaveEliminatoriaEntity l2 = new LlaveEliminatoriaEntity();
        l2.setId(1L);
        l2.setFase("Semifinal");
        l2.setNumeroLlave(1);

        assertEquals(l1, l2);
        assertEquals(l1.hashCode(), l2.hashCode());
        assertNotNull(l1.toString());
        assertNotEquals(l1, new LlaveEliminatoriaEntity());
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
        InvitacionEntity i1 = InvitacionEntity.builder()
                .id(1L).estado("PENDIENTE").build();
        InvitacionEntity i2 = InvitacionEntity.builder()
                .id(1L).estado("PENDIENTE").build();

        assertEquals(i1, i2);
        assertEquals(i1.hashCode(), i2.hashCode());
        assertNotNull(i1.toString());
        assertNotEquals(i1, new InvitacionEntity());
        assertEquals(i1, i1);
        assertNotEquals(i1, null);
    }

    @Test
    void partidoEntity_EqualsHashCodeToString_FuncionanCorrectamente() {
        PartidoEntity p1 = new PartidoEntity();
        p1.setId(1L);
        p1.setGolesLocal(2);
        p1.setGolesVisitante(1);
        p1.setFase("Grupos");

        PartidoEntity p2 = new PartidoEntity();
        p2.setId(1L);
        p2.setGolesLocal(2);
        p2.setGolesVisitante(1);
        p2.setFase("Grupos");

        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());
        assertNotNull(p1.toString());
        assertNotEquals(p1, new PartidoEntity());
        assertEquals(p1, p1);
        assertNotEquals(p1, null);
    }

    @Test
    void alineacionEntity_EqualsHashCodeToString_FuncionanCorrectamente() {
        AlineacionEntity a1 = new AlineacionEntity();
        a1.setId(1L);
        a1.setFormacion(TacticalFormation.F_1_2_3_1);

        AlineacionEntity a2 = new AlineacionEntity();
        a2.setId(1L);
        a2.setFormacion(TacticalFormation.F_1_2_3_1);

        assertEquals(a1, a2);
        assertEquals(a1.hashCode(), a2.hashCode());
        assertNotNull(a1.toString());
        assertNotEquals(a1, new AlineacionEntity());
        assertEquals(a1, a1);
        assertNotEquals(a1, null);
    }

    @Test
    void eventoPartidoEntity_EqualsHashCodeToString_FuncionanCorrectamente() {
        EventoPartidoEntity ep1 = new EventoPartidoEntity();
        ep1.setId(1L);
        ep1.setTipoEvento(TipoEvento.GOL);
        ep1.setMinuto(45);
        ep1.setDescripcion("Gol de cabeza");

        EventoPartidoEntity ep2 = new EventoPartidoEntity();
        ep2.setId(1L);
        ep2.setTipoEvento(TipoEvento.GOL);
        ep2.setMinuto(45);
        ep2.setDescripcion("Gol de cabeza");

        assertEquals(ep1, ep2);
        assertEquals(ep1.hashCode(), ep2.hashCode());
        assertNotNull(ep1.toString());
        assertNotEquals(ep1, new EventoPartidoEntity());
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
        AlineacionJugadorEntity aj1 = new AlineacionJugadorEntity();
        aj1.setId(1L);
        aj1.setRol("TITULAR");
        aj1.setPosicionEnCancha("Delantero");
        aj1.setNumeroCamiseta(9);

        AlineacionJugadorEntity aj2 = new AlineacionJugadorEntity();
        aj2.setId(1L);
        aj2.setRol("TITULAR");
        aj2.setPosicionEnCancha("Delantero");
        aj2.setNumeroCamiseta(9);

        assertEquals(aj1, aj2);
        assertEquals(aj1.hashCode(), aj2.hashCode());
        assertNotNull(aj1.toString());
        assertNotEquals(aj1, new AlineacionJugadorEntity());
        assertEquals(aj1, aj1);
        assertNotEquals(aj1, null);
    }

    @Test
    void canchaEntity_EqualsHashCodeToString_FuncionanCorrectamente() {
        CanchaEntity c1 = new CanchaEntity();
        c1.setId(1L);
        c1.setNombre("Cancha 1");
        c1.setDireccion("Calle 100");
        c1.setDescripcion("Principal");

        CanchaEntity c2 = new CanchaEntity();
        c2.setId(1L);
        c2.setNombre("Cancha 1");
        c2.setDireccion("Calle 100");
        c2.setDescripcion("Principal");

        assertEquals(c1, c2);
        assertEquals(c1.hashCode(), c2.hashCode());
        assertNotNull(c1.toString());
        assertNotEquals(c1, new CanchaEntity());
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
