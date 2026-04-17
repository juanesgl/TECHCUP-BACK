package edu.dosw.proyect.models;

import edu.dosw.proyect.core.models.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class TeamTest {

    @Test
    void equipo_Builder_ConstruyeCorrectamente() {
        Player capitan = new Player();
        capitan.setNombre("Capitan Test");

        Team team = Team.builder()
                .id(1L)
                .nombre("Alpha FC")
                .escudoUrl("alpha.png")
                .colorUniformeLocal("Rojo")
                .colorUniformeVisita("Blanco")
                .capitan(capitan)
                .estadoInscripcion("APROBADO")
                .build();

        assertEquals(1L, team.getId());
        assertEquals("Alpha FC", team.getNombre());
        assertEquals("alpha.png", team.getEscudoUrl());
        assertEquals("Rojo", team.getColorUniformeLocal());
        assertEquals("APROBADO", team.getEstadoInscripcion());
        assertEquals("Capitan Test", team.getCapitan().getNombre());
    }

    @Test
    void equipo_CamposTransient_FuncionanCorrectamente() {
        Team team = new Team();
        team.setEscudo("escudo.png");
        team.setColoresUniforme("Azul y Negro");

        assertEquals("escudo.png", team.getEscudo());
        assertEquals("Azul y Negro", team.getColoresUniforme());
    }

    @Test
    void equipo_NoArgsConstructor_ListasInicializadas() {
        Team team = new Team();
        assertNotNull(team.getEquipoJugadores());
        assertTrue(team.getEquipoJugadores().isEmpty());
    }

    @Test
    void equipo_Builder_TodosLosCampos() {
        Player capitan = new Player(1L, "Capitan", true, false, true);
        Tournament torneo = new Tournament();
        torneo.setTournId("TOURN-1");

        Team team = Team.builder()
                .id(1L)
                .nombre("Alpha FC")
                .escudoUrl("alpha.png")
                .colorUniformeLocal("Rojo")
                .colorUniformeVisita("Blanco")
                .capitan(capitan)
                .torneo(torneo)
                .estadoInscripcion("APROBADO")
                .build();

        assertEquals(1L, team.getId());
        assertEquals("Alpha FC", team.getNombre());
        assertEquals("alpha.png", team.getEscudoUrl());
        assertEquals("Rojo", team.getColorUniformeLocal());
        assertEquals("Blanco", team.getColorUniformeVisita());
        assertEquals("APROBADO", team.getEstadoInscripcion());
        assertNotNull(team.getCapitan());
        assertNotNull(team.getTorneo());
    }

    @Test
    void equipo_Setters_FuncionanCorrectamente() {
        Team team = new Team();
        team.setNombre("Beta FC");
        team.setColorUniformeLocal("Azul");
        team.setEstadoInscripcion("PENDIENTE");
        team.setEquipoJugadores(new ArrayList<>());

        assertEquals("Beta FC", team.getNombre());
        assertEquals("Azul", team.getColorUniformeLocal());
        assertEquals("PENDIENTE", team.getEstadoInscripcion());
        assertNotNull(team.getEquipoJugadores());
    }
}