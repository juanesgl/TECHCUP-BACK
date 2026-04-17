package edu.dosw.proyect.persistence.repository;

import edu.dosw.proyect.persistence.entity.SoccerFieldEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface SoccerFieldRepository extends JpaRepository<SoccerFieldEntity, Long> {
    Optional<SoccerFieldEntity> findByNombre(String nombre);
}