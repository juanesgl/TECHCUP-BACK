package edu.dosw.proyect.persistence.mapper;

import edu.dosw.proyect.core.models.User;
import edu.dosw.proyect.persistence.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserPersistenceMapper {

    @Mapping(target = "name", source = "name")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "password", source = "password")
    @Mapping(target = "role", source = "role")
    @Mapping(target = "registrationDate", source = "registrationDate")
    @Mapping(target = "active", source = "active")
    @Mapping(target = "academicProgram", source = "academicProgram")
    UserEntity toEntity(User domain);

    @Mapping(target = "name", source = "name")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "password", source = "password")
    @Mapping(target = "role", source = "role")
    @Mapping(target = "registrationDate", source = "registrationDate")
    @Mapping(target = "active", source = "active")
    @Mapping(target = "academicProgram", source = "academicProgram")
    User toDomain(UserEntity entity);
}