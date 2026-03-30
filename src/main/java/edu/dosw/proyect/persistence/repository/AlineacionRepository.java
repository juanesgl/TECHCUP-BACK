package edu.dosw.proyect.persistence.repository;

import edu.dosw.proyect.core.models.Alineacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlineacionRepository extends JpaRepository<Alineacion, Long> {
    List<Alineacion> findByPartidoId(Long partidoId);

    List<Alineacion> findByEquipoId(Long equipoId);

    Optional<Alineacion> findByPartidoIdAndEquipoId(Long partidoId, Long equipoId);
}
