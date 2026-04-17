package edu.dosw.proyect.models;

import edu.dosw.proyect.core.models.*;
import edu.dosw.proyect.core.models.enums.MatchStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class PartidoTest {

    @Test
    void partido_Builder_ConstruyeCorrectamente() {
        Team local = new Team();
        local.setNombre("Alpha");

        Team visitante = new Team();
        visitante.setNombre("Beta");

        Partido p = Partido.builder()
                .id(1L)
                .teamLocal(local)
                .teamVisitante(visitante)
                .fechaHora(LocalDateTime.now())
                .golesLocal(2)
                .golesVisitante(1)
                .estado(MatchStatus.FINALIZADO)
                .fase("Grupos")
                .build();

        assertEquals(1L, p.getId());
        assertEquals("Alpha", p.getTeamLocal().getNombre());
        assertEquals("Beta", p.getTeamVisitante().getNombre());
        assertEquals(2, p.getGolesLocal());
        assertEquals(1, p.getGolesVisitante());
        assertEquals(MatchStatus.FINALIZADO, p.getEstado());
    }

    @Test
    void partido_CamposTransient_FuncionanCorrectamente() {
        Partido p = new Partido();
        p.setNombreEquipoLocal("Alpha");
        p.setNombreEquipoVisitante("Beta");
        p.setFecha(LocalDate.now());
        p.setHora(LocalTime.of(10, 0));
        p.setCanchaLegacy("Cancha Principal");
        p.setArbitroLegacy("Carlos");

        assertEquals("Alpha", p.getNombreEquipoLocal());
        assertEquals("Beta", p.getNombreEquipoVisitante());
        assertNotNull(p.getFecha());
        assertNotNull(p.getHora());
        assertEquals("Cancha Principal", p.getCanchaLegacy());
        assertEquals("Carlos", p.getArbitroLegacy());
    }

    @Test
    void partido_NoArgsConstructor_CreaVacio() {
        Partido p = new Partido();
        assertNull(p.getId());
        assertEquals(0, p.getGolesLocal());
        assertEquals(0, p.getGolesVisitante());
    }
}