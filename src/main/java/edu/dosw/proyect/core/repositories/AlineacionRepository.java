package edu.dosw.proyect.core.repositories;

import edu.dosw.proyect.core.models.Alineacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlineacionRepository extends JpaRepository<Alineacion, Long> {
    List<Alineacion> findByPartidoId(Long partidoId);

    List<Alineacion> findByEquipoId(Long equipoId);
}
