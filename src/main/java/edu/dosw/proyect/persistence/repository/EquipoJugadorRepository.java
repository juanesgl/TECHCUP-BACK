package edu.dosw.proyect.persistence.repository;

import edu.dosw.proyect.persistence.entity.EquipoJugadorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface EquipoJugadorRepository extends JpaRepository<EquipoJugadorEntity, Long> {
    List<EquipoJugadorEntity> findByEquipoId(Long equipoId);
    List<EquipoJugadorEntity> findByJugadorId(Long jugadorId);
    Optional<EquipoJugadorEntity> findByEquipoIdAndJugadorId(Long equipoId, Long jugadorId);
}