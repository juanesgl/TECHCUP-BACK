package edu.dosw.proyect.core.repositories;

import edu.dosw.proyect.core.models.AlineacionJugador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlineacionJugadorRepository extends JpaRepository<AlineacionJugador, Long> {
    List<AlineacionJugador> findByAlineacionId(Long alineacionId);
}
