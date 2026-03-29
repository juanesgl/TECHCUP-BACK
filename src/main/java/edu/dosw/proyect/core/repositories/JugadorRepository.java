package edu.dosw.proyect.core.repositories;

import edu.dosw.proyect.core.models.Jugador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JugadorRepository extends JpaRepository<Jugador, Long> {

        @Query("SELECT j FROM Jugador j JOIN j.usuario u WHERE " +
                        "(:name IS NULL OR LOWER(u.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
                        "(:posicion IS NULL OR j.posiciones LIKE %:posicion%) AND " +
                        "(:semestre IS NULL OR j.semestre = :semestre) AND " +
                        "(:disponible IS NULL OR j.disponible = :disponible) AND " +
                        "(:edad IS NULL OR j.edad = :edad)")
        List<Jugador> filterPlayers(@Param("name") String name,
                        @Param("posicion") String posicion,
                        @Param("semestre") String semestre,
                        @Param("disponible") Boolean disponible,
                        @Param("edad") Integer edad);

        
        @Query("SELECT j FROM Jugador j WHERE j.usuario.name = :nombre")
        Optional<Jugador> findByNombre(@Param("nombre") String nombre);
}

