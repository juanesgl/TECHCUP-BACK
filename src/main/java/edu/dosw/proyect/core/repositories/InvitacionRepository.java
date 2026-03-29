package edu.dosw.proyect.core.repositories;

import edu.dosw.proyect.core.models.Invitacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvitacionRepository extends JpaRepository<Invitacion, Long> {
    List<Invitacion> findByEquipoId(Long equipoId);

    List<Invitacion> findByJugadorId(Long jugadorId);
}

