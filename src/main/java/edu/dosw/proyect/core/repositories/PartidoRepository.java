package edu.dosw.proyect.core.repositories;

import edu.dosw.proyect.core.models.Partido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PartidoRepository extends JpaRepository<Partido, Long> {

        @Query("SELECT p FROM Partido p WHERE " +
                        "(:canchaId IS NULL OR p.cancha.id = :canchaId) AND " +
                        "(:fecha IS NULL OR CAST(p.fechaHora AS date) = :fecha)")
        List<Partido> findByFiltros(@Param("fecha") LocalDate fecha,
                        @Param("canchaId") Long canchaId);

        List<Partido> findByTorneoId(Long torneoId);

        List<Partido> findByTorneo_TournId(String tournId);

        @Query("SELECT p FROM Partido p WHERE p.equipoLocal.nombre = :nombre OR p.equipoVisitante.nombre = :nombre")
        List<Partido> findByNombreEquipo(@Param("nombre") String nombre);
}