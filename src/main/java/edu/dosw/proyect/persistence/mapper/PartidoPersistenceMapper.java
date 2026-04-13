package edu.dosw.proyect.persistence.mapper;

import edu.dosw.proyect.core.models.Partido;
import edu.dosw.proyect.persistence.entity.PartidoEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PartidoPersistenceMapper {

    private final TournamentPersistenceMapper tournamentMapper;
    private final EquipoPersistenceMapper equipoMapper;
    private final CanchaPersistenceMapper canchaMapper;
    private final UserPersistenceMapper userMapper;

    public PartidoEntity toEntity(Partido domain) {
        if (domain == null) return null;
        return PartidoEntity.builder()
                .id(domain.getId())
                .torneo(tournamentMapper.toEntity(domain.getTorneo()))
                .equipoLocal(equipoMapper.toEntity(domain.getEquipoLocal()))
                .equipoVisitante(equipoMapper.toEntity(domain.getEquipoVisitante()))
                .cancha(canchaMapper.toEntity(domain.getCancha()))
                .arbitro(userMapper.toEntity(domain.getArbitro()))
                .fechaHora(domain.getFechaHora())
                .golesLocal(domain.getGolesLocal())
                .golesVisitante(domain.getGolesVisitante())
                .fase(domain.getFase())
                .estado(domain.getEstado())
                .build();
    }

    public Partido toDomain(PartidoEntity entity) {
        if (entity == null) return null;
        Partido p = new Partido();
        p.setId(entity.getId());
        p.setTorneo(tournamentMapper.toDomain(entity.getTorneo()));
        p.setEquipoLocal(equipoMapper.toDomain(entity.getEquipoLocal()));
        p.setEquipoVisitante(equipoMapper.toDomain(entity.getEquipoVisitante()));
        p.setCancha(canchaMapper.toDomain(entity.getCancha()));
        p.setArbitro(userMapper.toDomain(entity.getArbitro()));
        p.setFechaHora(entity.getFechaHora());
        p.setGolesLocal(entity.getGolesLocal());
        p.setGolesVisitante(entity.getGolesVisitante());
        p.setFase(entity.getFase());
        p.setEstado(entity.getEstado());
        return p;
    }
}