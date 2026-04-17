package edu.dosw.proyect.persistence.repository;

import edu.dosw.proyect.persistence.entity.LineupPlayerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LineupPlayerRepository extends JpaRepository<LineupPlayerEntity, Long> {
    List<LineupPlayerEntity> findByAlineacionId(Long alineacionId);
}