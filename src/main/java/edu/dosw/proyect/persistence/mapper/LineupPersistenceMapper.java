package edu.dosw.proyect.persistence.mapper;

import edu.dosw.proyect.core.models.Lineup;
import edu.dosw.proyect.core.models.LineupPlayer;
import edu.dosw.proyect.persistence.entity.LineupEntity;
import edu.dosw.proyect.persistence.entity.LineupPlayerEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {
        MatchPersistenceMapper.class,
        TeamPersistenceMapper.class,
        PlayerPersistenceMapper.class
})
public interface LineupPersistenceMapper {

    @Mapping(target = "partido", source = "partido")
    @Mapping(target = "equipo", source = "team")
    @Mapping(target = "formacion", source = "formacion")
    @Mapping(target = "fechaRegistro", source = "fechaRegistro")
    @Mapping(target = "jugadores", source = "jugadores")
    LineupEntity toEntity(Lineup domain);

    @Mapping(target = "partido", source = "partido")
    @Mapping(target = "team", source = "equipo")
    @Mapping(target = "formacion", source = "formacion")
    @Mapping(target = "fechaRegistro", source = "fechaRegistro")
    @Mapping(target = "jugadores", source = "jugadores")
    Lineup toDomain(LineupEntity entity);

    @Mapping(target = "jugador", source = "jugador")
    @Mapping(target = "rol", source = "rol")
    @Mapping(target = "posicionEnCancha", source = "posicionEnCancha")
    @Mapping(target = "numeroCamiseta", source = "numeroCamiseta")
    @Mapping(target = "alineacion", ignore = true)
    LineupPlayerEntity toJugadorEntity(LineupPlayer domain);

    @Mapping(target = "jugador", source = "jugador")
    @Mapping(target = "rol", source = "rol")
    @Mapping(target = "posicionEnCancha", source = "posicionEnCancha")
    @Mapping(target = "numeroCamiseta", source = "numeroCamiseta")
    @Mapping(target = "lineup", ignore = true)
    LineupPlayer toJugadorDomain(LineupPlayerEntity entity);
}