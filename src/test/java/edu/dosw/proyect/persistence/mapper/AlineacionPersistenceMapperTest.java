package edu.dosw.proyect.persistence.mapper;

import edu.dosw.proyect.core.models.Alineacion;
import edu.dosw.proyect.core.models.enums.TacticalFormation;
import edu.dosw.proyect.persistence.entity.AlineacionEntity;
import edu.dosw.proyect.persistence.entity.AlineacionJugadorEntity;
import edu.dosw.proyect.persistence.entity.JugadorEntity;
import edu.dosw.proyect.persistence.entity.UserEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AlineacionPersistenceMapperTest {

    private final UserPersistenceMapper userMapper = new UserPersistenceMapper();
    private final edu.dosw.proyect.persistence.mapper.TournamentPersistenceMapper tournamentMapper =
            new edu.dosw.proyect.persistence.mapper.TournamentPersistenceMapper(userMapper);
    private final edu.dosw.proyect.persistence.mapper.CanchaPersistenceMapper canchaMapper =
            new edu.dosw.proyect.persistence.mapper.CanchaPersistenceMapper(tournamentMapper);
    private final edu.dosw.proyect.persistence.mapper.JugadorPersistenceMapper jugadorMapper =
            new edu.dosw.proyect.persistence.mapper.JugadorPersistenceMapper(userMapper);
    private final edu.dosw.proyect.persistence.mapper.EquipoPersistenceMapper equipoMapper =
            new edu.dosw.proyect.persistence.mapper.EquipoPersistenceMapper(tournamentMapper, jugadorMapper);
    private final edu.dosw.proyect.persistence.mapper.PartidoPersistenceMapper partidoMapper =
            new edu.dosw.proyect.persistence.mapper.PartidoPersistenceMapper(
                    tournamentMapper, equipoMapper, canchaMapper, userMapper);
    private final edu.dosw.proyect.persistence.mapper.AlineacionPersistenceMapper mapper =
            new edu.dosw.proyect.persistence.mapper.AlineacionPersistenceMapper(
                    partidoMapper, equipoMapper, jugadorMapper);

    @Test
    void toEntity_MapeaCorrectamente() {
        Alineacion a = new Alineacion();
        a.setId(1L);
        a.setFormacion(TacticalFormation.F_1_2_3_1);
        a.setFechaRegistro(LocalDateTime.now());

        AlineacionEntity entity = mapper.toEntity(a);

        assertNotNull(entity);
        assertEquals(1L, entity.getId());
        assertEquals(TacticalFormation.F_1_2_3_1, entity.getFormacion());
    }

    @Test
    void toDomain_MapeaCorrectamente() {
        AlineacionEntity entity = new AlineacionEntity();
        entity.setId(1L);
        entity.setFormacion(TacticalFormation.F_1_3_2_1);
        entity.setFechaRegistro(LocalDateTime.now());

        Alineacion domain = mapper.toDomain(entity);

        assertNotNull(domain);
        assertEquals(1L, domain.getId());
        assertEquals(TacticalFormation.F_1_3_2_1, domain.getFormacion());
    }

    @Test
    void toEntity_Null_RetornaNull() {
        assertNull(mapper.toEntity(null));
    }

    @Test
    void toDomain_Null_RetornaNull() {
        assertNull(mapper.toDomain(null));
    }

    @Test
    void toDomain_ConJugadores_MapeaCorrectamente() {
        UserEntity userEntity = UserEntity.builder()
                .id(1L).name("Juan").email("j@mail.com")
                .password("p").role("PLAYER").build();

        JugadorEntity jugadorEntity = new JugadorEntity();
        jugadorEntity.setId(1L);
        jugadorEntity.setUsuario(userEntity);
        jugadorEntity.setPosiciones("Delantero");

        AlineacionJugadorEntity aj = new AlineacionJugadorEntity();
        aj.setId(1L);
        aj.setJugador(jugadorEntity);
        aj.setRol("TITULAR");
        aj.setPosicionEnCancha("Delantero");
        aj.setNumeroCamiseta(9);

        AlineacionEntity entity = new AlineacionEntity();
        entity.setId(1L);
        entity.setFormacion(TacticalFormation.F_1_2_3_1);
        entity.setFechaRegistro(LocalDateTime.now());
        entity.setJugadores(List.of(aj));

        Alineacion domain = mapper.toDomain(entity);

        assertNotNull(domain);
        assertEquals(1, domain.getJugadores().size());
        assertEquals("TITULAR", domain.getJugadores().get(0).getRol());
        assertEquals(9, domain.getJugadores().get(0).getNumeroCamiseta());
    }
}