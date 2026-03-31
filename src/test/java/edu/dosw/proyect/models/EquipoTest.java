package edu.dosw.proyect.models;

import edu.dosw.proyect.core.models.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class EquipoTest {

    @Test
    void equipo_Builder_ConstruyeCorrectamente() {
        Jugador capitan = new Jugador();
        capitan.setNombre("Capitan Test");

        Equipo equipo = Equipo.builder()
                .id(1L)
                .nombre("Alpha FC")
                .escudoUrl("alpha.png")
                .colorUniformeLocal("Rojo")
                .colorUniformeVisita("Blanco")
                .capitan(capitan)
                .estadoInscripcion("APROBADO")
                .build();

        assertEquals(1L, equipo.getId());
        assertEquals("Alpha FC", equipo.getNombre());
        assertEquals("alpha.png", equipo.getEscudoUrl());
        assertEquals("Rojo", equipo.getColorUniformeLocal());
        assertEquals("APROBADO", equipo.getEstadoInscripcion());
        assertEquals("Capitan Test", equipo.getCapitan().getNombre());
    }

    @Test
    void equipo_CamposTransient_FuncionanCorrectamente() {
        Equipo equipo = new Equipo();
        equipo.setEscudo("escudo.png");
        equipo.setColoresUniforme("Azul y Negro");

        assertEquals("escudo.png", equipo.getEscudo());
        assertEquals("Azul y Negro", equipo.getColoresUniforme());
    }

    @Test
    void equipo_NoArgsConstructor_ListasInicializadas() {
        Equipo equipo = new Equipo();
        assertNotNull(equipo.getEquipoJugadores());
        assertTrue(equipo.getEquipoJugadores().isEmpty());
    }

    @Test
    void equipo_Builder_TodosLosCampos() {
        Jugador capitan = new Jugador(1L, "Capitan", true, false, true);
        Tournament torneo = new Tournament();
        torneo.setTournId("TOURN-1");

        Equipo equipo = Equipo.builder()
                .id(1L)
                .nombre("Alpha FC")
                .escudoUrl("alpha.png")
                .colorUniformeLocal("Rojo")
                .colorUniformeVisita("Blanco")
                .capitan(capitan)
                .torneo(torneo)
                .estadoInscripcion("APROBADO")
                .build();

        assertEquals(1L, equipo.getId());
        assertEquals("Alpha FC", equipo.getNombre());
        assertEquals("alpha.png", equipo.getEscudoUrl());
        assertEquals("Rojo", equipo.getColorUniformeLocal());
        assertEquals("Blanco", equipo.getColorUniformeVisita());
        assertEquals("APROBADO", equipo.getEstadoInscripcion());
        assertNotNull(equipo.getCapitan());
        assertNotNull(equipo.getTorneo());
    }

    @Test
    void equipo_Setters_FuncionanCorrectamente() {
        Equipo equipo = new Equipo();
        equipo.setNombre("Beta FC");
        equipo.setColorUniformeLocal("Azul");
        equipo.setEstadoInscripcion("PENDIENTE");
        equipo.setEquipoJugadores(new ArrayList<>());

        assertEquals("Beta FC", equipo.getNombre());
        assertEquals("Azul", equipo.getColorUniformeLocal());
        assertEquals("PENDIENTE", equipo.getEstadoInscripcion());
        assertNotNull(equipo.getEquipoJugadores());
    }
}