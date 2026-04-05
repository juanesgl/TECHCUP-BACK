package edu.dosw.proyect.persistence.repository;

import edu.dosw.proyect.persistence.entity.LlaveEliminatoriaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LlaveEliminatoriaRepository extends JpaRepository<LlaveEliminatoriaEntity, Long> {
    List<LlaveEliminatoriaEntity> findByTorneoId(Long torneoId);
}