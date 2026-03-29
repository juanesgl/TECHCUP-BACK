package edu.dosw.proyect.core.repositories;

import edu.dosw.proyect.core.models.Cancha;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class CanchaRepository {
    private final Map<Long, Cancha> dataStore = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public Cancha save(Cancha cancha) {
        if (cancha.getId() == null) {
            cancha.setId(idGenerator.getAndIncrement());
        }
        dataStore.put(cancha.getId(), cancha);
        return cancha;
    }

    public List<Cancha> findByTorneo_TournId(String tournId) {
        return dataStore.values().stream()
                .filter(c -> c.getTorneo() != null && tournId.equals(c.getTorneo().getTournId()))
                .collect(Collectors.toList());
    }
}
