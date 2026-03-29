package edu.dosw.proyect.core.repositories;

import edu.dosw.proyect.core.models.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {
    List<Pago> findByTorneoId(Long torneoId);

    List<Pago> findByEquipoId(Long equipoId);
}
