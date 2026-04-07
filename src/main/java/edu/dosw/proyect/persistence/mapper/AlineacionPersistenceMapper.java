package edu.dosw.proyect.persistence.mapper;

import edu.dosw.proyect.core.models.Alineacion;
import edu.dosw.proyect.core.models.AlineacionJugador;
import edu.dosw.proyect.persistence.entity.AlineacionEntity;
import edu.dosw.proyect.persistence.entity.AlineacionJugadorEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AlineacionPersistenceMapper {

    private final PartidoPersistenceMapper partidoMapper;
    private final EquipoPersistenceMapper equipoMapper;
    private final JugadorPersistenceMapper jugadorMapper;

    public AlineacionEntity toEntity(Alineacion domain) {
        if (domain == null) return null;
        return AlineacionEntity.builder()
                .id(domain.getId())
                .partido(partidoMapper.toEntity(domain.getPartido()))
                .equipo(equipoMapper.toEntity(domain.getEquipo()))
                .formacion(domain.getFormacion())
                .fechaRegistro(domain.getFechaRegistro())
                .build();
    }

    public Alineacion toDomain(AlineacionEntity entity) {
        if (entity == null) return null;
        Alineacion a = new Alineacion();
        a.setId(entity.getId());
        a.setPartido(partidoMapper.toDomain(entity.getPartido()));
        a.setEquipo(equipoMapper.toDomain(entity.getEquipo()));
        a.setFormacion(entity.getFormacion());
        a.setFechaRegistro(entity.getFechaRegistro());

        List<AlineacionJugador> jugadores = entity.getJugadores() != null
                ? entity.getJugadores().stream()
                .map(this::toAlineacionJugadorDomain)
                .collect(Collectors.toList())
                : new ArrayList<>();
        a.setJugadores(jugadores);
        return a;
    }

    private AlineacionJugador toAlineacionJugadorDomain(AlineacionJugadorEntity entity) {
        AlineacionJugador aj = new AlineacionJugador();
        aj.setId(entity.getId());
        aj.setJugador(jugadorMapper.toDomain(entity.getJugador()));
        aj.setRol(entity.getRol());
        aj.setPosicionEnCancha(entity.getPosicionEnCancha());
        aj.setNumeroCamiseta(entity.getNumeroCamiseta());
        return aj;
    }
}