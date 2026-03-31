package edu.dosw.proyect.persistence.repository;

import edu.dosw.proyect.persistence.entity.AlineacionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlineacionRepository extends JpaRepository<AlineacionEntity, Long> {

    List<AlineacionEntity> findByPartidoId(Long partidoId);

    List<AlineacionEntity> findByEquipoId(Long equipoId);

    Optional<AlineacionEntity> findByPartidoIdAndEquipoId(Long partidoId, Long equipoId);
}