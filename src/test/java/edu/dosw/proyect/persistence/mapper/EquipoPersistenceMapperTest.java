package edu.dosw.proyect.persistence.mapper;

import edu.dosw.proyect.core.models.Equipo;
import edu.dosw.proyect.core.models.Jugador;
import edu.dosw.proyect.core.models.User;
import edu.dosw.proyect.persistence.entity.EquipoEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EquipoPersistenceMapperTest {

    private final UserPersistenceMapper userMapper = new UserPersistenceMapper();
    private final edu.dosw.proyect.persistence.mapper.TournamentPersistenceMapper tournamentMapper =
            new edu.dosw.proyect.persistence.mapper.TournamentPersistenceMapper(userMapper);
    private final edu.dosw.proyect.persistence.mapper.JugadorPersistenceMapper jugadorMapper =
            new edu.dosw.proyect.persistence.mapper.JugadorPersistenceMapper(userMapper);
    private final edu.dosw.proyect.persistence.mapper.EquipoPersistenceMapper mapper =
            new edu.dosw.proyect.persistence.mapper.EquipoPersistenceMapper(tournamentMapper, jugadorMapper);

    @Test
    void toEntity_MapeaCorrectamente() {
        Equipo e = new Equipo();
        e.setId(1L);
        e.setNombre("Alpha FC");
        e.setColorUniformeLocal("Rojo");
        e.setColorUniformeVisita("Blanco");
        e.setEscudoUrl("alpha.png");
        e.setEstadoInscripcion("APROBADO");

        EquipoEntity entity = mapper.toEntity(e);

        assertNotNull(entity);
        assertEquals("Alpha FC", entity.getNombre());
        assertEquals("APROBADO", entity.getEstadoInscripcion());
    }

    @Test
    void toDomain_MapeaCorrectamente() {
        EquipoEntity entity = new EquipoEntity();
        entity.setId(1L);
        entity.setNombre("Beta FC");
        entity.setColorUniformeLocal("Azul");
        entity.setEstadoInscripcion("PENDIENTE");

        Equipo domain = mapper.toDomain(entity);

        assertNotNull(domain);
        assertEquals("Beta FC", domain.getNombre());
        assertEquals("PENDIENTE", domain.getEstadoInscripcion());
    }

    @Test
    void toEntity_ConCapitan_MapeaCorrectamente() {
        Jugador capitan = new Jugador();
        capitan.setId(1L);
        capitan.setUsuario(User.builder().id(1L).name("Cap")
                .email("cap@mail.com").password("p").role("CAPTAIN").build());

        Equipo e = new Equipo();
        e.setId(1L);
        e.setNombre("Alpha FC");
        e.setCapitan(capitan);

        EquipoEntity entity = mapper.toEntity(e);

        assertNotNull(entity);
        assertNotNull(entity.getCapitan());
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