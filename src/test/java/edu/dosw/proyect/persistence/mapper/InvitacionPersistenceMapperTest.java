package edu.dosw.proyect.persistence.mapper;

import edu.dosw.proyect.core.models.Invitacion;
import edu.dosw.proyect.persistence.entity.InvitacionEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class InvitacionPersistenceMapperTest {

    private final UserPersistenceMapper userMapper = new UserPersistenceMapper();
    private final edu.dosw.proyect.persistence.mapper.TournamentPersistenceMapper tournamentMapper =
            new edu.dosw.proyect.persistence.mapper.TournamentPersistenceMapper(userMapper);
    private final edu.dosw.proyect.persistence.mapper.JugadorPersistenceMapper jugadorMapper =
            new edu.dosw.proyect.persistence.mapper.JugadorPersistenceMapper(userMapper);
    private final edu.dosw.proyect.persistence.mapper.EquipoPersistenceMapper equipoMapper =
            new edu.dosw.proyect.persistence.mapper.EquipoPersistenceMapper(tournamentMapper, jugadorMapper);
    private final edu.dosw.proyect.persistence.mapper.InvitacionPersistenceMapper mapper =
            new edu.dosw.proyect.persistence.mapper.InvitacionPersistenceMapper(equipoMapper, jugadorMapper);

    @Test
    void toEntity_MapeaCorrectamente() {
        Invitacion inv = Invitacion.builder()
                .id(1L).estado("PENDIENTE")
                .fechaEnvio(LocalDateTime.now())
                .build();

        InvitacionEntity entity = mapper.toEntity(inv);

        assertNotNull(entity);
        assertEquals(1L, entity.getId());
        assertEquals("PENDIENTE", entity.getEstado());
    }

    @Test
    void toDomain_MapeaCorrectamente() {
        InvitacionEntity entity = InvitacionEntity.builder()
                .id(1L).estado("ACEPTADA")
                .fechaEnvio(LocalDateTime.now())
                .build();

        Invitacion domain = mapper.toDomain(entity);

        assertNotNull(domain);
        assertEquals(1L, domain.getId());
        assertEquals("ACEPTADA", domain.getEstado());
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