package edu.dosw.proyect.persistence.mapper;

import edu.dosw.proyect.core.models.Jugador;
import edu.dosw.proyect.persistence.entity.JugadorEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JugadorPersistenceMapper {

    private final UserPersistenceMapper userMapper;

    public JugadorEntity toEntity(Jugador domain) {
        if (domain == null) return null;
        JugadorEntity e = new JugadorEntity();
        e.setId(domain.getId());
        e.setUsuario(userMapper.toEntity(domain.getUsuario()));
        e.setFotoUrl(domain.getFotoUrl());
        e.setPosiciones(domain.getPosiciones());
        e.setDorsal(domain.getDorsal());
        e.setDisponible(domain.isDisponible());
        e.setSemestre(domain.getSemestre());
        e.setGenero(domain.getGenero());
        e.setIdentificacion(domain.getIdentificacion());
        e.setEdad(domain.getEdad());
        e.setPerfilCompleto(domain.isPerfilCompleto());
        e.setTieneEquipo(domain.isTieneEquipo());
        return e;
    }

    public Jugador toDomain(JugadorEntity entity) {
        if (entity == null) return null;
        Jugador j = new Jugador();
        j.setId(entity.getId());
        j.setUsuario(userMapper.toDomain(entity.getUsuario()));
        j.setFotoUrl(entity.getFotoUrl());
        j.setPosiciones(entity.getPosiciones());
        j.setDorsal(entity.getDorsal());
        j.setDisponible(entity.isDisponible());
        j.setSemestre(entity.getSemestre());
        j.setGenero(entity.getGenero());
        j.setIdentificacion(entity.getIdentificacion());
        j.setEdad(entity.getEdad());
        j.setPerfilCompleto(entity.isPerfilCompleto());
        j.setTieneEquipo(entity.isTieneEquipo());
        return j;
    }
}