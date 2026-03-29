package edu.dosw.proyect.core.repositories;

import edu.dosw.proyect.core.models.Cancha;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CanchaRepository extends JpaRepository<Cancha, Long> {
    Optional<Cancha> findByNombre(String nombre);
}
