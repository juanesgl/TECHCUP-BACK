package edu.dosw.proyect.persistence.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

class InvitationPersistenceMapperTest {

    private final InvitationPersistenceMapper mapper = Mappers.getMapper(InvitationPersistenceMapper.class);

    @Test
    void mapper_Existe() {
        assertNotNull(mapper);
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