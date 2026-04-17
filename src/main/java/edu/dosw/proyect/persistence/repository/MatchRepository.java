package edu.dosw.proyect.persistence.repository;

import edu.dosw.proyect.persistence.entity.MatchEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<MatchEntity, Long> {

    @Query("SELECT p FROM MatchEntity p WHERE " +
            "(:canchaId IS NULL OR p.cancha.id = :canchaId) AND " +
            "(:fecha IS NULL OR CAST(p.fechaHora AS date) = :fecha)")
    List<MatchEntity> findByFiltros(@Param("fecha") LocalDate fecha,
                                    @Param("canchaId") Long canchaId);

    List<MatchEntity> findByTorneoId(Long torneoId);

    List<MatchEntity> findByTorneo_TournId(String tournId);

    @Query("SELECT p FROM MatchEntity p WHERE " +
            "p.equipoLocal.nombre = :nombre OR p.equipoVisitante.nombre = :nombre")
    List<MatchEntity> findByNombreEquipo(@Param("nombre") String nombre);
}