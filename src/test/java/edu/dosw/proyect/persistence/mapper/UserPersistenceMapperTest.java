package edu.dosw.proyect.persistence.mapper;

import edu.dosw.proyect.core.models.User;
import edu.dosw.proyect.persistence.entity.UserEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserPersistenceMapperTest {

    private final UserPersistenceMapper mapper = new UserPersistenceMapper();

    private User buildUser() {
        return User.builder().id(1L).name("Juan").lastName("Perez")
                .email("juan@mail.com").password("pass").role("PLAYER")
                .active(true).academicProgram("sistemas").build();
    }

    private UserEntity buildUserEntity() {
        return UserEntity.builder().id(1L).name("Juan").lastName("Perez")
                .email("juan@mail.com").password("pass").role("PLAYER")
                .active(true).academicProgram("sistemas").build();
    }

    @Test
    void toEntity_MapeaCorrectamente() {
        User user = buildUser();
        UserEntity entity = mapper.toEntity(user);
        assertNotNull(entity);
        assertEquals(user.getId(), entity.getId());
        assertEquals(user.getName(), entity.getName());
        assertEquals(user.getEmail(), entity.getEmail());
        assertEquals(user.getRole(), entity.getRole());
        assertEquals(user.getAcademicProgram(), entity.getAcademicProgram());
        assertTrue(entity.isActive());
    }

    @Test
    void toDomain_MapeaCorrectamente() {
        UserEntity entity = buildUserEntity();
        User user = mapper.toDomain(entity);
        assertNotNull(user);
        assertEquals(entity.getId(), user.getId());
        assertEquals(entity.getName(), user.getName());
        assertEquals(entity.getEmail(), user.getEmail());
        assertEquals(entity.getRole(), user.getRole());
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