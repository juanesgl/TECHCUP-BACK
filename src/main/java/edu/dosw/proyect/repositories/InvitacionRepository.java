package edu.dosw.proyect.repositories;

import edu.dosw.proyect.models.Invitacion;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class InvitacionRepository {
    private final Map<Long, Invitacion> dataStore = new HashMap<>();

    public Optional<Invitacion> findById(Long id) {
        return Optional.ofNullable(dataStore.get(id));
    }

    public Invitacion save(Invitacion invitacion) {
        if (invitacion.getId() == null) {
            invitacion.setId((long) (dataStore.size() + 1));
        }
        dataStore.put(invitacion.getId(), invitacion);
        return invitacion;
    }
}
