package edu.dosw.proyect.persistence.repository;

import edu.dosw.proyect.persistence.entity.EventoPartidoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EventoPartidoRepository extends JpaRepository<EventoPartidoEntity, Long> {
    List<EventoPartidoEntity> findByPartidoId(Long partidoId);
    List<EventoPartidoEntity> findByPartido_Torneo_TournId(String tournId);
}