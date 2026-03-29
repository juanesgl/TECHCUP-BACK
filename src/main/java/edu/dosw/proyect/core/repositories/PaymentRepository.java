package edu.dosw.proyect.core.repositories;

import edu.dosw.proyect.core.models.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByUserId(Long userId);
    List<Payment> findByTournamentId(Long tournamentId);
    Optional<Payment> findByIdAndUserId(Long id, Long userId);
}
