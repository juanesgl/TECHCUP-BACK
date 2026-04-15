package edu.dosw.proyect.persistence.repository;

import edu.dosw.proyect.persistence.entity.TournamentConfigurationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface TournamentConfigurationRepository extends JpaRepository<TournamentConfigurationEntity, Long> {
    Optional<TournamentConfigurationEntity> findByTorneoTournId(String tournId);
}