package edu.dosw.proyect.persistence.mapper;

import edu.dosw.proyect.core.models.Invitacion;
import edu.dosw.proyect.persistence.entity.InvitacionEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InvitacionPersistenceMapper {

    private final EquipoPersistenceMapper equipoMapper;
    private final JugadorPersistenceMapper jugadorMapper;

    public InvitacionEntity toEntity(Invitacion domain) {
        if (domain == null) return null;
        return InvitacionEntity.builder()
                .id(domain.getId())
                .equipo(equipoMapper.toEntity(domain.getEquipo()))
                .jugador(jugadorMapper.toEntity(domain.getJugador()))
                .estado(domain.getEstado())
                .fechaEnvio(domain.getFechaEnvio())
                .fechaRespuesta(domain.getFechaRespuesta())
                .build();
    }

    public Invitacion toDomain(InvitacionEntity entity) {
        if (entity == null) return null;
        return Invitacion.builder()
                .id(entity.getId())
                .equipo(equipoMapper.toDomain(entity.getEquipo()))
                .jugador(jugadorMapper.toDomain(entity.getJugador()))
                .estado(entity.getEstado())
                .fechaEnvio(entity.getFechaEnvio())
                .fechaRespuesta(entity.getFechaRespuesta())
                .build();
    }
}