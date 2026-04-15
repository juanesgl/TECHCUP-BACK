package edu.dosw.proyect.persistence.repository;

import edu.dosw.proyect.persistence.entity.TeamLineupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * JPA repository for TeamLineup.
 * Replaces the in-memory HashMap implementation — data now persists in PostgreSQL.
 */
@Repository
public interface TeamLineupJpaRepository extends JpaRepository<TeamLineupEntity, Long> {

    Optional<TeamLineupEntity> findByTeamIdAndMatchId(Long teamId, Long matchId);

    List<TeamLineupEntity> findByTeamId(Long teamId);

    List<TeamLineupEntity> findByMatchId(Long matchId);
}