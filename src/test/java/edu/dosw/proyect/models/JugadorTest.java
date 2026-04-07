package edu.dosw.proyect.models;

import edu.dosw.proyect.core.models.Jugador;
import edu.dosw.proyect.core.models.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JugadorTest {

    @Test
    void jugador_Constructor_ConstruyeCorrectamente() {
        Jugador j = new Jugador(1L, "Juan Perez", true, false, true);

        assertEquals(1L, j.getId());
        assertEquals("Juan Perez", j.getNombre());
        assertTrue(j.isPerfilCompleto());
        assertFalse(j.isTieneEquipo());
        assertTrue(j.isDisponible());
    }

    @Test
    void jugador_NoArgsConstructor_CreaVacio() {
        Jugador j = new Jugador();
        assertNull(j.getId());
        assertFalse(j.isDisponible());
        assertFalse(j.isPerfilCompleto());
    }

    @Test
    void jugador_Setters_FuncionanCorrectamente() {
        User usuario = User.builder()
                .name("Carlos")
                .email("carlos@mail.com")
                .password("pass")
                .role("PLAYER")
                .build();

        Jugador j = new Jugador();
        j.setUsuario(usuario);
        j.setNombre("Carlos");
        j.setDorsal(10);
        j.setDisponible(true);
        j.setPerfilCompleto(true);
        j.setTieneEquipo(false);
        j.setSemestre("5");
        j.setEdad(20);
        j.setGenero("MASCULINO");

        assertEquals("Carlos", j.getNombre());
        assertEquals(10, j.getDorsal());
        assertTrue(j.isDisponible());
        assertEquals("5", j.getSemestre());
        assertEquals(20, j.getEdad());
        assertNotNull(j.getUsuario());
    }

    @Test
    void jugador_CampoNombre_EsTransient() {
        Jugador j = new Jugador();
        j.setNombre("Test");
        assertEquals("Test", j.getNombre());
    }

    @Test
    void jugador_AllArgsConstructor_TodosLosCampos() {
        User usuario = User.builder()
                .id(1L).name("Juan").email("juan@mail.com")
                .password("pass").role("PLAYER").build();

        Jugador jugador = new Jugador(
                1L, usuario, "foto.png", "Delantero",
                10, true, "5", "MASCULINO",
                "123456", 22, "Juan", true, false);

        assertEquals(1L, jugador.getId());
        assertEquals("Juan", jugador.getNombre());
        assertEquals(10, jugador.getDorsal());
        assertTrue(jugador.isDisponible());
        assertEquals("5", jugador.getSemestre());
        assertEquals("MASCULINO", jugador.getGenero());
        assertEquals(22, jugador.getEdad());
        assertTrue(jugador.isPerfilCompleto());
        assertFalse(jugador.isTieneEquipo());
    }

    @Test
    void jugador_Constructor5Args_FuncionaCorrectamente() {
        Jugador j = new Jugador(1L, "Test", true, true, false);
        assertEquals(1L, j.getId());
        assertEquals("Test", j.getNombre());
        assertTrue(j.isPerfilCompleto());
        assertTrue(j.isTieneEquipo());
        assertFalse(j.isDisponible());
    }

    @Test
    void jugador_NoArgsConstructor_ValoresPorDefecto() {
        Jugador j = new Jugador();
        assertNull(j.getId());
        assertFalse(j.isDisponible());
        assertFalse(j.isPerfilCompleto());
        assertFalse(j.isTieneEquipo());
    }

    @Test
    void jugador_Setters_TodosLosCampos() {
        Jugador j = new Jugador();
        j.setFotoUrl("foto.jpg");
        j.setPosiciones("Delantero, Mediocampista");
        j.setDorsal(9);
        j.setDisponible(true);
        j.setSemestre("8");
        j.setGenero("MASCULINO");
        j.setIdentificacion("987654");
        j.setEdad(23);
        j.setNombre("Carlos");
        j.setPerfilCompleto(true);
        j.setTieneEquipo(true);

        assertEquals("foto.jpg", j.getFotoUrl());
        assertEquals("Delantero, Mediocampista", j.getPosiciones());
        assertEquals(9, j.getDorsal());
        assertTrue(j.isDisponible());
        assertEquals("8", j.getSemestre());
        assertEquals("987654", j.getIdentificacion());
        assertEquals(23, j.getEdad());
        assertEquals("Carlos", j.getNombre());
        assertTrue(j.isPerfilCompleto());
        assertTrue(j.isTieneEquipo());
    }
}