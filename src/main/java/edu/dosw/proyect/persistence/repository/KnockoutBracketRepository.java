package edu.dosw.proyect.persistence.repository;

import edu.dosw.proyect.persistence.entity.KnockoutBracketEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface KnockoutBracketRepository extends JpaRepository<KnockoutBracketEntity, Long> {
    List<KnockoutBracketEntity> findByTorneoId(Long torneoId);
}