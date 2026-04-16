package edu.dosw.proyect.persistence.repository;

import edu.dosw.proyect.persistence.entity.TeamEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<TeamEntity, Long> {
    Optional<TeamEntity> findByNombreAndTorneoId(String nombre, Long torneoId);
    Optional<TeamEntity> findByNombre(String nombre);
    boolean existsByNombre(String nombre);
    List<TeamEntity> findByTorneoId(Long torneoId);
}