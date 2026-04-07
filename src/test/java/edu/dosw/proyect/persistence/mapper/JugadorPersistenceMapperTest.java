package edu.dosw.proyect.persistence.mapper;

import edu.dosw.proyect.core.models.Jugador;
import edu.dosw.proyect.core.models.User;
import edu.dosw.proyect.persistence.entity.JugadorEntity;
import edu.dosw.proyect.persistence.entity.UserEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JugadorPersistenceMapperTest {

    private final UserPersistenceMapper userMapper = new UserPersistenceMapper();
    private final edu.dosw.proyect.persistence.mapper.JugadorPersistenceMapper mapper =
            new edu.dosw.proyect.persistence.mapper.JugadorPersistenceMapper(userMapper);

    @Test
    void toEntity_MapeaCorrectamente() {
        User user = User.builder().id(1L).name("Juan")
                .email("j@mail.com").password("p").role("PLAYER").build();

        Jugador j = new Jugador();
        j.setId(1L);
        j.setUsuario(user);
        j.setPosiciones("Delantero");
        j.setEdad(22);
        j.setDisponible(true);
        j.setPerfilCompleto(true);
        j.setSemestre("5");
        j.setGenero("MASCULINO");
        j.setIdentificacion("123456");
        j.setDorsal(9);

        JugadorEntity entity = mapper.toEntity(j);

        assertNotNull(entity);
        assertEquals(1L, entity.getId());
        assertEquals("Delantero", entity.getPosiciones());
        assertTrue(entity.isDisponible());
        assertTrue(entity.isPerfilCompleto());
        assertEquals(9, entity.getDorsal());
    }

    @Test
    void toDomain_MapeaCorrectamente() {
        UserEntity userEntity = UserEntity.builder().id(1L).name("Juan")
                .email("j@mail.com").password("p").role("PLAYER").build();

        JugadorEntity entity = new JugadorEntity();
        entity.setId(1L);
        entity.setUsuario(userEntity);
        entity.setPosiciones("Mediocampista");
        entity.setEdad(25);
        entity.setTieneEquipo(false);
        entity.setDisponible(true);
        entity.setSemestre("8");

        Jugador domain = mapper.toDomain(entity);

        assertNotNull(domain);
        assertEquals(1L, domain.getId());
        assertEquals("Mediocampista", domain.getPosiciones());
        assertTrue(domain.isDisponible());
        assertFalse(domain.isTieneEquipo());
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