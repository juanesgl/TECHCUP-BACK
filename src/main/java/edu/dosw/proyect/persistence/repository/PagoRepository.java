package edu.dosw.proyect.persistence.repository;

import edu.dosw.proyect.persistence.entity.PagoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PagoRepository extends JpaRepository<PagoEntity, Long> {
    List<PagoEntity> findByTorneoId(Long torneoId);
    List<PagoEntity> findByEquipoId(Long equipoId);
}