package edu.dosw.proyect.core.repositories;

import edu.dosw.proyect.core.models.Equipo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EquipoRepository extends JpaRepository<Equipo, Long> {
    Optional<Equipo> findByNombreAndTorneoId(String nombre, Long torneoId);

    Optional<Equipo> findByNombre(String nombre);

    boolean existsByNombre(String nombre);

    List<Equipo> findByTorneoId(Long torneoId);
}

