package edu.dosw.proyect.core.repositories;

import edu.dosw.proyect.core.models.Equipo;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class EquipoRepository {
    
    private final Map<Long, Equipo> dataStore = new HashMap<>();

    public Optional<Equipo> findById(Long id) {
        return Optional.ofNullable(dataStore.get(id));
    }

    public Equipo save(Equipo equipo) {
        if (equipo.getId() == null) {
            equipo.setId((long) (dataStore.size() + 1));
        }
        dataStore.put(equipo.getId(), equipo);
        return equipo;
    }

    public boolean existsByNombre(String nombre) {
        return dataStore.values().stream()
                .anyMatch(equipo -> equipo.getNombre().equalsIgnoreCase(nombre));
    }
    
    public List<Equipo> findAll() {
        return new ArrayList<>(dataStore.values());
    }
}
