package edu.dosw.proyect.core.repositories;

import edu.dosw.proyect.core.models.EventoPartido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventoPartidoRepository extends JpaRepository<EventoPartido, Long> {
    List<EventoPartido> findByPartidoId(Long partidoId);

    List<EventoPartido> findByPartido_Torneo_TournId(String tournId);
}

