package edu.dosw.proyect.core.repositories;

import edu.dosw.proyect.core.models.Partido;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class PartidoRepository {

    private final Map<Long, Partido> dataStore = new HashMap<>();
    private long currentId = 1;

    public Optional<Partido> findById(Long id) {
        return Optional.ofNullable(dataStore.get(id));
    }

    public Partido save(Partido partido) {
        if (partido.getId() == null) {
            partido.setId(currentId++);
        }
        dataStore.put(partido.getId(), partido);
        return partido;
    }

    public List<Partido> findAll() {
        return new ArrayList<>(dataStore.values());
    }

    public List<Partido> findByTorneo_TournId(String tournId) {
        return dataStore.values().stream()
                .filter(p -> p.getTorneo() != null && tournId.equals(p.getTorneo().getTournId()))
                .collect(Collectors.toList());
    }
}
