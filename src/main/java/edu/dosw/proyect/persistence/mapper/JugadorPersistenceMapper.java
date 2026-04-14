package edu.dosw.proyect.persistence.mapper;

import edu.dosw.proyect.core.models.Jugador;
import edu.dosw.proyect.persistence.entity.JugadorEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = { UserPersistenceMapper.class })
public interface JugadorPersistenceMapper {

    @Mapping(target = "usuario", source = "usuario")
    @Mapping(target = "fotoUrl", source = "fotoUrl")
    @Mapping(target = "posiciones", source = "posiciones")
    @Mapping(target = "dorsal", source = "dorsal")
    @Mapping(target = "disponible", source = "disponible")
    @Mapping(target = "semestre", source = "semestre")
    @Mapping(target = "genero", source = "genero")
    @Mapping(target = "identificacion", source = "identificacion")
    @Mapping(target = "edad", source = "edad")
    @Mapping(target = "perfilCompleto", source = "perfilCompleto")
    @Mapping(target = "tieneEquipo", source = "tieneEquipo")
    JugadorEntity toEntity(Jugador domain);

    @Mapping(target = "usuario", source = "usuario")
    @Mapping(target = "fotoUrl", source = "fotoUrl")
    @Mapping(target = "posiciones", source = "posiciones")
    @Mapping(target = "dorsal", source = "dorsal")
    @Mapping(target = "disponible", source = "disponible")
    @Mapping(target = "semestre", source = "semestre")
    @Mapping(target = "genero", source = "genero")
    @Mapping(target = "identificacion", source = "identificacion")
    @Mapping(target = "edad", source = "edad")
    @Mapping(target = "perfilCompleto", source = "perfilCompleto")
    @Mapping(target = "tieneEquipo", source = "tieneEquipo")
    @Mapping(target = "nombre", source = "usuario.name")
    Jugador toDomain(JugadorEntity entity);
}