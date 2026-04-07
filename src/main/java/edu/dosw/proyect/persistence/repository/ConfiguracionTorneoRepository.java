package edu.dosw.proyect.persistence.repository;

import edu.dosw.proyect.persistence.entity.ConfiguracionTorneoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ConfiguracionTorneoRepository extends JpaRepository<ConfiguracionTorneoEntity, Long> {
    Optional<ConfiguracionTorneoEntity> findByTorneoTournId(String tournId);
}