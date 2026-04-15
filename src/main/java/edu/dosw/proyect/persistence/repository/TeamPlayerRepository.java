package edu.dosw.proyect.persistence.repository;

import edu.dosw.proyect.persistence.entity.TeamPlayerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface TeamPlayerRepository extends JpaRepository<TeamPlayerEntity, Long> {
    List<TeamPlayerEntity> findByEquipoId(Long equipoId);
    List<TeamPlayerEntity> findByJugadorId(Long jugadorId);
    Optional<TeamPlayerEntity> findByEquipoIdAndJugadorId(Long equipoId, Long jugadorId);
}