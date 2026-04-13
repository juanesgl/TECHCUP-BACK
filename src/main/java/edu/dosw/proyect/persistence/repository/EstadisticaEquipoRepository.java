package edu.dosw.proyect.persistence.repository;

import edu.dosw.proyect.persistence.entity.EstadisticasEquipoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface EstadisticaEquipoRepository extends JpaRepository<EstadisticasEquipoEntity, Long> {
    List<EstadisticasEquipoEntity> findByTorneoIdOrderByPuntosDesc(Long torneoId);
    Optional<EstadisticasEquipoEntity> findByEquipoIdAndTorneoId(Long equipoId, Long torneoId);
}