package edu.dosw.proyect.persistence.repository;

import edu.dosw.proyect.persistence.entity.MatchEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MatchEventRepository extends JpaRepository<MatchEventEntity, Long> {
    List<MatchEventEntity> findByPartidoId(Long partidoId);
    List<MatchEventEntity> findByPartido_Torneo_TournId(String tournId);
}