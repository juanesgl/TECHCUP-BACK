package edu.dosw.proyect.core.repositories;

import edu.dosw.proyect.core.models.EventoPartido;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class EventoPartidoRepository {

    private final Map<Long, EventoPartido> dataStore = new HashMap<>();
    private long currentId = 1;

    public Optional<EventoPartido> findById(Long id) {
        return Optional.ofNullable(dataStore.get(id));
    }

    public EventoPartido save(EventoPartido evento) {
        if (evento.getId() == null) {
            evento.setId(currentId++);
        }
        dataStore.put(evento.getId(), evento);
        return evento;
    }

    public List<EventoPartido> findAll() {
        return new ArrayList<>(dataStore.values());
    }

    public List<EventoPartido> findByPartido_Id(Long partidoId) {
        return dataStore.values().stream()
                .filter(e -> e.getPartido() != null && partidoId.equals(e.getPartido().getId()))
                .collect(Collectors.toList());
    }

    public List<EventoPartido> findByPartido_Torneo_TournId(String tournId) {
        return dataStore.values().stream()
                .filter(e -> e.getPartido() != null &&
                        e.getPartido().getTorneo() != null &&
                        tournId.equals(e.getPartido().getTorneo().getTournId()))
                .collect(Collectors.toList());
    }
}
