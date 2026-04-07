package edu.dosw.proyect.persistence.mapper;

import edu.dosw.proyect.core.models.Cancha;
import edu.dosw.proyect.persistence.entity.CanchaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CanchaPersistenceMapper {

    private final TournamentPersistenceMapper tournamentMapper;

    public CanchaEntity toEntity(Cancha domain) {
        if (domain == null) return null;
        return CanchaEntity.builder()
                .id(domain.getId())
                .nombre(domain.getNombre())
                .direccion(domain.getDireccion())
                .descripcion(domain.getDescripcion())
                .torneo(tournamentMapper.toEntity(domain.getTorneo()))
                .build();
    }

    public Cancha toDomain(CanchaEntity entity) {
        if (entity == null) return null;
        Cancha c = new Cancha();
        c.setId(entity.getId());
        c.setNombre(entity.getNombre());
        c.setDireccion(entity.getDireccion());
        c.setDescripcion(entity.getDescripcion());
        c.setTorneo(tournamentMapper.toDomain(entity.getTorneo()));
        return c;
    }
}