package edu.dosw.proyect.persistence.repository;

import edu.dosw.proyect.persistence.entity.EquipoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface EquipoRepository extends JpaRepository<EquipoEntity, Long> {
    Optional<EquipoEntity> findByNombreAndTorneoId(String nombre, Long torneoId);
    Optional<EquipoEntity> findByNombre(String nombre);
    boolean existsByNombre(String nombre);
    List<EquipoEntity> findByTorneoId(Long torneoId);
}