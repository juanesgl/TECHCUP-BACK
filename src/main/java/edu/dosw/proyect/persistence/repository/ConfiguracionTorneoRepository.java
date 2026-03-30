package edu.dosw.proyect.persistence.repository;

import edu.dosw.proyect.core.models.ConfiguracionTorneo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfiguracionTorneoRepository extends JpaRepository<ConfiguracionTorneo, Long> {
    Optional<ConfiguracionTorneo> findByTorneoTournId(String tournId);
}

