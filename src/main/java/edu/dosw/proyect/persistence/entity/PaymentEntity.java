package edu.dosw.proyect.persistence.entity;

import edu.dosw.proyect.core.models.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "payment")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private Long tournamentId;
    private String fileUrl;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
}