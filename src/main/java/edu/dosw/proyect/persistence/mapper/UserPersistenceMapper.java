package edu.dosw.proyect.persistence.mapper;

import edu.dosw.proyect.core.models.User;
import edu.dosw.proyect.persistence.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserPersistenceMapper {

    public UserEntity toEntity(User domain) {
        if (domain == null) return null;
        return UserEntity.builder()
                .id(domain.getId())
                .name(domain.getName())
                .lastName(domain.getLastName())
                .email(domain.getEmail())
                .password(domain.getPassword())
                .role(domain.getRole())
                .registrationDate(domain.getRegistrationDate())
                .active(domain.isActive())
                .academicProgram(domain.getAcademicProgram())
                .build();
    }

    public User toDomain(UserEntity entity) {
        if (entity == null) return null;
        return User.builder()
                .id(entity.getId())
                .name(entity.getName())
                .lastName(entity.getLastName())
                .email(entity.getEmail())
                .password(entity.getPassword())
                .role(entity.getRole())
                .registrationDate(entity.getRegistrationDate())
                .active(entity.isActive())
                .academicProgram(entity.getAcademicProgram())
                .build();
    }
}