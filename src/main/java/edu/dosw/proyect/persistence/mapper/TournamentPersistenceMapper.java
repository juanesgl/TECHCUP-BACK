package edu.dosw.proyect.persistence.mapper;

import edu.dosw.proyect.core.models.Tournament;
import edu.dosw.proyect.persistence.entity.TournamentEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TournamentPersistenceMapper {

    private final UserPersistenceMapper userMapper;

    public TournamentEntity toEntity(Tournament domain) {
        if (domain == null) return null;
        return TournamentEntity.builder()
                .id(domain.getId())
                .tournId(domain.getTournId())
                .name(domain.getName())
                .startDate(domain.getStartDate())
                .endDate(domain.getEndDate())
                .maxTeams(domain.getMaxTeams())
                .costPerTeam(domain.getCostPerTeam())
                .status(domain.getStatus())
                .organizador(userMapper.toEntity(domain.getOrganizador()))
                .build();
    }

    public Tournament toDomain(TournamentEntity entity) {
        if (entity == null) return null;
        Tournament t = new Tournament();
        t.setId(entity.getId());
        t.setTournId(entity.getTournId());
        t.setName(entity.getName());
        t.setStartDate(entity.getStartDate());
        t.setEndDate(entity.getEndDate());
        t.setMaxTeams(entity.getMaxTeams());
        t.setCostPerTeam(entity.getCostPerTeam());
        t.setStatus(entity.getStatus());
        t.setOrganizador(userMapper.toDomain(entity.getOrganizador()));
        return t;
    }
}