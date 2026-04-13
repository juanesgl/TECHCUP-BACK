package edu.dosw.proyect.persistence.mapper;

import edu.dosw.proyect.core.models.Partido;
import edu.dosw.proyect.core.models.enums.MatchStatus;
import edu.dosw.proyect.persistence.entity.EquipoEntity;
import edu.dosw.proyect.persistence.entity.PartidoEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class PartidoPersistenceMapperTest {

    private final UserPersistenceMapper userMapper = new UserPersistenceMapper();
    private final edu.dosw.proyect.persistence.mapper.TournamentPersistenceMapper tournamentMapper =
            new edu.dosw.proyect.persistence.mapper.TournamentPersistenceMapper(userMapper);
    private final edu.dosw.proyect.persistence.mapper.CanchaPersistenceMapper canchaMapper =
            new edu.dosw.proyect.persistence.mapper.CanchaPersistenceMapper(tournamentMapper);
    private final edu.dosw.proyect.persistence.mapper.JugadorPersistenceMapper jugadorMapper =
            new edu.dosw.proyect.persistence.mapper.JugadorPersistenceMapper(userMapper);
    private final edu.dosw.proyect.persistence.mapper.EquipoPersistenceMapper equipoMapper =
            new edu.dosw.proyect.persistence.mapper.EquipoPersistenceMapper(tournamentMapper, jugadorMapper);
    private final edu.dosw.proyect.persistence.mapper.PartidoPersistenceMapper mapper =
            new edu.dosw.proyect.persistence.mapper.PartidoPersistenceMapper(
                    tournamentMapper, equipoMapper, canchaMapper, userMapper);

    @Test
    void toEntity_MapeaCorrectamente() {
        Partido p = new Partido();
        p.setId(1L);
        p.setGolesLocal(2);
        p.setGolesVisitante(1);
        p.setEstado(MatchStatus.FINALIZADO);
        p.setFechaHora(LocalDateTime.now());
        p.setFase("Grupos");

        PartidoEntity entity = mapper.toEntity(p);

        assertNotNull(entity);
        assertEquals(1L, entity.getId());
        assertEquals(2, entity.getGolesLocal());
        assertEquals(MatchStatus.FINALIZADO, entity.getEstado());
    }

    @Test
    void toDomain_MapeaCorrectamente() {
        PartidoEntity entity = new PartidoEntity();
        entity.setId(1L);
        entity.setGolesLocal(3);
        entity.setGolesVisitante(0);
        entity.setEstado(MatchStatus.PROGRAMADO);
        entity.setFechaHora(LocalDateTime.now());

        Partido domain = mapper.toDomain(entity);

        assertNotNull(domain);
        assertEquals(1L, domain.getId());
        assertEquals(MatchStatus.PROGRAMADO, domain.getEstado());
        assertEquals(3, domain.getGolesLocal());
    }

    @Test
    void toDomain_ConEquipos_MapeaCorrectamente() {
        EquipoEntity local = new EquipoEntity();
        local.setId(1L);
        local.setNombre("Alpha");

        EquipoEntity visitante = new EquipoEntity();
        visitante.setId(2L);
        visitante.setNombre("Beta");

        PartidoEntity entity = new PartidoEntity();
        entity.setId(1L);
        entity.setEquipoLocal(local);
        entity.setEquipoVisitante(visitante);
        entity.setEstado(MatchStatus.PROGRAMADO);

        Partido domain = mapper.toDomain(entity);

        assertNotNull(domain.getEquipoLocal());
        assertNotNull(domain.getEquipoVisitante());
        assertEquals("Alpha", domain.getEquipoLocal().getNombre());
        assertEquals("Beta", domain.getEquipoVisitante().getNombre());
    }

    @Test
    void toEntity_Null_RetornaNull() {
        assertNull(mapper.toEntity(null));
    }

    @Test
    void toDomain_Null_RetornaNull() {
        assertNull(mapper.toDomain(null));
    }
}