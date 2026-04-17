package edu.dosw.proyect.models;

import edu.dosw.proyect.core.models.*;
import edu.dosw.proyect.core.models.enums.TacticalFormation;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LineupTest {

    private Player buildJugador(String nombre) {
        Player j = new Player();
        j.setNombre(nombre);
        return j;
    }

    private LineupPlayer buildAlineacionJugador(String nombre, String rol) {
        LineupPlayer aj = new LineupPlayer();
        aj.setJugador(buildJugador(nombre));
        aj.setRol(rol);
        return aj;
    }

    @Test
    void getNombreEquipo_ConEquipo_RetornaNombre() {
        Team team = new Team();
        team.setNombre("Equipo Alpha");

        Lineup a = new Lineup();
        a.setTeam(team);

        assertEquals("Equipo Alpha", a.getNombreEquipo());
    }

    @Test
    void getNombreEquipo_SinEquipo_RetornaNull() {
        Lineup a = new Lineup();
        assertNull(a.getNombreEquipo());
    }

    @Test
    void getTitulares_FiltrarPorRolTitular() {
        Lineup a = new Lineup();
        a.setJugadores(List.of(
                buildAlineacionJugador("J1", "TITULAR"),
                buildAlineacionJugador("J2", "TITULAR"),
                buildAlineacionJugador("J3", "RESERVA")
        ));

        List<String> titulares = a.getTitulares();

        assertEquals(2, titulares.size());
        assertTrue(titulares.contains("J1"));
        assertTrue(titulares.contains("J2"));
        assertFalse(titulares.contains("J3"));
    }

    @Test
    void getReservas_FiltrarPorRolReserva() {
        Lineup a = new Lineup();
        a.setJugadores(List.of(
                buildAlineacionJugador("J1", "TITULAR"),
                buildAlineacionJugador("J2", "RESERVA"),
                buildAlineacionJugador("J3", "RESERVA")
        ));

        List<String> reservas = a.getReservas();

        assertEquals(2, reservas.size());
        assertTrue(reservas.contains("J2"));
        assertTrue(reservas.contains("J3"));
    }

    @Test
    void getTitulares_ListaVacia_RetornaListaVacia() {
        Lineup a = new Lineup();
        a.setJugadores(List.of());

        assertTrue(a.getTitulares().isEmpty());
    }

    @Test
    void getReservas_ListaVacia_RetornaListaVacia() {
        Lineup a = new Lineup();
        a.setJugadores(List.of());

        assertTrue(a.getReservas().isEmpty());
    }

    @Test
    void alineacion_Builder_ConstruyeCorrectamente() {
        Team team = new Team();
        team.setNombre("Alpha");

        Lineup a = Lineup.builder()
                .id(1L)
                .team(team)
                .formacion(TacticalFormation.F_1_2_3_1)
                .fechaRegistro(LocalDateTime.now())
                .build();

        assertEquals(1L, a.getId());
        assertEquals("Alpha", a.getNombreEquipo());
        assertEquals(TacticalFormation.F_1_2_3_1, a.getFormacion());
        assertNotNull(a.getFechaRegistro());
    }
}