package edu.dosw.proyect.persistence.mapper;

import edu.dosw.proyect.core.models.Equipo;
import edu.dosw.proyect.persistence.entity.EquipoEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EquipoPersistenceMapper {

    private final TournamentPersistenceMapper tournamentMapper;
    private final JugadorPersistenceMapper jugadorMapper;

    public EquipoEntity toEntity(Equipo domain) {
        if (domain == null) return null;
        return EquipoEntity.builder()
                .id(domain.getId())
                .nombre(domain.getNombre())
                .escudoUrl(domain.getEscudoUrl())
                .colorUniformeLocal(domain.getColorUniformeLocal())
                .colorUniformeVisita(domain.getColorUniformeVisita())
                .estadoInscripcion(domain.getEstadoInscripcion())
                .torneo(tournamentMapper.toEntity(domain.getTorneo()))
                .capitan(jugadorMapper.toEntity(domain.getCapitan()))
                .build();
    }

    public Equipo toDomain(EquipoEntity entity) {
        if (entity == null) return null;
        Equipo e = new Equipo();
        e.setId(entity.getId());
        e.setNombre(entity.getNombre());
        e.setEscudoUrl(entity.getEscudoUrl());
        e.setColorUniformeLocal(entity.getColorUniformeLocal());
        e.setColorUniformeVisita(entity.getColorUniformeVisita());
        e.setEstadoInscripcion(entity.getEstadoInscripcion());
        e.setTorneo(tournamentMapper.toDomain(entity.getTorneo()));
        e.setCapitan(jugadorMapper.toDomain(entity.getCapitan()));
        return e;
    }
}