package edu.dosw.proyect.persistence.mapper;

import edu.dosw.proyect.core.models.Cancha;
import edu.dosw.proyect.persistence.entity.CanchaEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CanchaPersistenceMapperTest {

    private final UserPersistenceMapper userMapper = new UserPersistenceMapper();
    private final edu.dosw.proyect.persistence.mapper.TournamentPersistenceMapper tournamentMapper =
            new edu.dosw.proyect.persistence.mapper.TournamentPersistenceMapper(userMapper);
    private final edu.dosw.proyect.persistence.mapper.CanchaPersistenceMapper mapper =
            new edu.dosw.proyect.persistence.mapper.CanchaPersistenceMapper(tournamentMapper);

    @Test
    void toEntity_MapeaCorrectamente() {
        Cancha c = new Cancha();
        c.setId(1L);
        c.setNombre("Cancha 1");
        c.setDireccion("Calle 100");
        c.setDescripcion("Principal");

        CanchaEntity entity = mapper.toEntity(c);

        assertNotNull(entity);
        assertEquals("Cancha 1", entity.getNombre());
        assertEquals("Calle 100", entity.getDireccion());
    }

    @Test
    void toDomain_MapeaCorrectamente() {
        CanchaEntity entity = new CanchaEntity();
        entity.setId(1L);
        entity.setNombre("Cancha 2");
        entity.setDireccion("Calle 200");
        entity.setDescripcion("Secundaria");

        Cancha domain = mapper.toDomain(entity);

        assertNotNull(domain);
        assertEquals("Cancha 2", domain.getNombre());
        assertEquals("Calle 200", domain.getDireccion());
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