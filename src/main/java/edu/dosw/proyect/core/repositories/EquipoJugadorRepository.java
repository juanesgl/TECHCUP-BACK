package edu.dosw.proyect.core.repositories;

import edu.dosw.proyect.core.models.EquipoJugador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EquipoJugadorRepository extends JpaRepository<EquipoJugador, Long> {
    List<EquipoJugador> findByEquipoId(Long equipoId);

    List<EquipoJugador> findByJugadorId(Long jugadorId);

    Optional<EquipoJugador> findByEquipoIdAndJugadorId(Long equipoId, Long jugadorId);
}
