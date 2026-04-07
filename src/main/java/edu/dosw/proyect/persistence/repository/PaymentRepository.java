package edu.dosw.proyect.persistence.repository;

import edu.dosw.proyect.persistence.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {
    List<PaymentEntity> findByUserId(Long userId);
    List<PaymentEntity> findByTournamentId(Long tournamentId);
    Optional<PaymentEntity> findByIdAndUserId(Long id, Long userId);
}