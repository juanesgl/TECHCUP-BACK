package edu.dosw.proyect.persistence.repository;

import edu.dosw.proyect.core.models.LlaveEliminatoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LlaveEliminatoriaRepository extends JpaRepository<LlaveEliminatoria, Long> {
    List<LlaveEliminatoria> findByTorneoId(Long torneoId);
}

