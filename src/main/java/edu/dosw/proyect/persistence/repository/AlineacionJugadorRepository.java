package edu.dosw.proyect.persistence.repository;

import edu.dosw.proyect.persistence.entity.AlineacionJugadorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AlineacionJugadorRepository extends JpaRepository<AlineacionJugadorEntity, Long> {
    List<AlineacionJugadorEntity> findByAlineacionId(Long alineacionId);
}