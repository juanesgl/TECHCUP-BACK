package edu.dosw.proyect.persistence.repository;

import edu.dosw.proyect.persistence.entity.PartidoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PartidoRepository extends JpaRepository<PartidoEntity, Long> {

    @Query("SELECT p FROM PartidoEntity p WHERE " +
            "(:canchaId IS NULL OR p.cancha.id = :canchaId) AND " +
            "(:fecha IS NULL OR CAST(p.fechaHora AS date) = :fecha)")
    List<PartidoEntity> findByFiltros(@Param("fecha") LocalDate fecha,
                                      @Param("canchaId") Long canchaId);

    List<PartidoEntity> findByTorneoId(Long torneoId);

    List<PartidoEntity> findByTorneo_TournId(String tournId);

    @Query("SELECT p FROM PartidoEntity p WHERE " +
            "p.equipoLocal.nombre = :nombre OR p.equipoVisitante.nombre = :nombre")
    List<PartidoEntity> findByNombreEquipo(@Param("nombre") String nombre);
}