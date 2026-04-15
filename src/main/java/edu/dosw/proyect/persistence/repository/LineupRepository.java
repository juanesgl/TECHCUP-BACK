package edu.dosw.proyect.persistence.repository;

import edu.dosw.proyect.persistence.entity.LineupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LineupRepository extends JpaRepository<LineupEntity, Long> {

    List<LineupEntity> findByPartidoId(Long partidoId);

    List<LineupEntity> findByEquipoId(Long equipoId);

    Optional<LineupEntity> findByPartidoIdAndEquipoId(Long partidoId, Long equipoId);
}