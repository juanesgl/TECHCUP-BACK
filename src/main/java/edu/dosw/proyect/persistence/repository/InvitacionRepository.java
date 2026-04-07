package edu.dosw.proyect.persistence.repository;

import edu.dosw.proyect.persistence.entity.InvitacionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface InvitacionRepository extends JpaRepository<InvitacionEntity, Long> {
    List<InvitacionEntity> findByEquipoId(Long equipoId);
    List<InvitacionEntity> findByJugadorId(Long jugadorId);
}