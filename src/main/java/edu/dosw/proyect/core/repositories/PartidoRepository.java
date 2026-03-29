package edu.dosw.proyect.core.repositories;

import edu.dosw.proyect.core.models.Partido;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class PartidoRepository {

    private final Map<Long, Partido> dataStore = new HashMap<Long, Partido>();
    private long contador = 1L;


    public Partido save(Partido partido) {
        if (partido.getId() == null) {
            partido.setId(contador++);
        }
        dataStore.put(partido.getId(), partido);
        return partido;
    }

    public Optional<Partido> findById(Long id) {
        return Optional.ofNullable(dataStore.get(id));
    }

    public List<Partido> findAll() {
        return new ArrayList<Partido>(dataStore.values());
    }

    public List<Partido> findByFecha(LocalDate fecha) {
        return dataStore.values().stream()
                .filter(p -> fecha.equals(p.getFecha()))
                .collect(Collectors.toList());
    }

    public List<Partido> findByCancha(String cancha) {
        return dataStore.values().stream()
                .filter(p -> p.getCancha() != null && p.getCancha().equalsIgnoreCase(cancha))
                .collect(Collectors.toList());
    }

    public List<Partido> findByEquipo(String nombreEquipo) {
        return dataStore.values().stream()
                .filter(p -> (p.getNombreEquipoLocal() != null &&
                        p.getNombreEquipoLocal().equalsIgnoreCase(nombreEquipo)) ||
                        (p.getNombreEquipoVisitante() != null &&
                                p.getNombreEquipoVisitante().equalsIgnoreCase(nombreEquipo)))
                .collect(Collectors.toList());
    }

    public List<Partido> findByTournamentId(String tournamentId) {
        return dataStore.values().stream()
                .filter(p -> tournamentId.equals(p.getTournamentId()))
                .collect(Collectors.toList());
    }

    public boolean existsById(Long id) {
        return dataStore.containsKey(id);
    }
}
