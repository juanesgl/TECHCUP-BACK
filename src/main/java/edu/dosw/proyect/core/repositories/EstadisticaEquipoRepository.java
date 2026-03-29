package edu.dosw.proyect.core.repositories;

import edu.dosw.proyect.core.models.EstadisticaEquipo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EstadisticaEquipoRepository extends JpaRepository<EstadisticaEquipo, Long> {
    List<EstadisticaEquipo> findByTorneoIdOrderByPuntosDesc(Long torneoId);

    Optional<EstadisticaEquipo> findByEquipoIdAndTorneoId(Long equipoId, Long torneoId);
}

