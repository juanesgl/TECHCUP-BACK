package edu.dosw.proyect.persistence.repository;

import edu.dosw.proyect.persistence.entity.TeamStatisticsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface TeamStatisticsRepository extends JpaRepository<TeamStatisticsEntity, Long> {
    List<TeamStatisticsEntity> findByTorneoIdOrderByPuntosDesc(Long torneoId);
    Optional<TeamStatisticsEntity> findByEquipoIdAndTorneoId(Long equipoId, Long torneoId);
}