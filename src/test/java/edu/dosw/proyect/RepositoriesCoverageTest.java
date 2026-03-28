package edu.dosw.proyect;

import edu.dosw.proyect.core.models.Equipo;
import edu.dosw.proyect.core.models.EventoPartido;
import edu.dosw.proyect.core.models.Partido;
import edu.dosw.proyect.core.models.Tournament;
import edu.dosw.proyect.core.repositories.EquipoRepository;
import edu.dosw.proyect.core.repositories.EventoPartidoRepository;
import edu.dosw.proyect.core.repositories.PartidoRepository;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class RepositoriesCoverageTest {

    @Test
    void testEquipoRepository() {
        EquipoRepository repo = new EquipoRepository();
        Equipo e = new Equipo();
        e.setNombre("Test");

        // Save
        Equipo saved = repo.save(e);
        assertNotNull(saved.getId());

        // FindAll
        List<Equipo> all = repo.findAll();
        assertEquals(1, all.size());

        // FindById
        Optional<Equipo> found = repo.findById(saved.getId());
        assertTrue(found.isPresent());

        // No delete method in EquipoRepository
    }

    @Test
    void testPartidoRepository() {
        PartidoRepository repo = new PartidoRepository();
        Partido p = new Partido();
        Tournament t = new Tournament();
        t.setTournId("T1");
        p.setTorneo(t);

        Partido saved = repo.save(p);
        assertNotNull(saved.getId());

        // FindAll
        List<Partido> all = repo.findAll();
        assertEquals(1, all.size());

        // FindById
        Optional<Partido> found = repo.findById(saved.getId());
        assertTrue(found.isPresent());

        // Custom finder
        List<Partido> byTorneo = repo.findByTorneo_TournId("T1");
        assertEquals(1, byTorneo.size());
    }

    @Test
    void testEventoPartidoRepository() {
        EventoPartidoRepository repo = new EventoPartidoRepository();
        EventoPartido ep = new EventoPartido();
        Partido p = new Partido();
        Tournament t = new Tournament();
        t.setTournId("T1");
        p.setTorneo(t);
        p.setId(1L);
        ep.setPartido(p);

        EventoPartido saved = repo.save(ep);
        assertNotNull(saved.getId());

        // FindAll
        List<EventoPartido> all = repo.findAll();
        assertEquals(1, all.size());

        // FindById
        Optional<EventoPartido> found = repo.findById(saved.getId());
        assertTrue(found.isPresent());

        // Custom finder
        List<EventoPartido> byTorneo = repo.findByPartido_Torneo_TournId("T1");
        assertEquals(1, byTorneo.size());

        // No delete method in EventoPartidoRepository
    }
}
