package edu.dosw.proyect.persistence.repository;

import edu.dosw.proyect.persistence.entity.CanchaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CanchaRepository extends JpaRepository<CanchaEntity, Long> {
    Optional<CanchaEntity> findByNombre(String nombre);
}