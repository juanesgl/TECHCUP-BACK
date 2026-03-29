package edu.dosw.proyect.core.repositories;

import edu.dosw.proyect.core.models.Alineacion;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class AlineacionRepository {

    private final Map<String, Alineacion> dataStore = new HashMap<>();
    private long currentId = 1;

    private String buildKey(Long partidoId, Long equipoId){
        return partidoId + "-" + equipoId;
    }

    public Alineacion save(Alineacion alineacion) {
        if (alineacion.getId() == null) {
            alineacion.setId(currentId++);
        }
        String key = buildKey(alineacion.getPartidoId(), alineacion.getEquipoId());
        dataStore.put(key, alineacion);
        return alineacion;
    }

    public Optional<Alineacion> findByPartidoIdAndEquipoId(Long partidoId, Long equipoId) {
        String key = buildKey(partidoId, equipoId);
        return Optional.ofNullable(dataStore.get(key));
    }

    public Optional<Alineacion> findById(Long id) {
        return dataStore.values().stream()
                .filter(a -> a.getId().equals(id))
                .findFirst();
    }
}
