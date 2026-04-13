package edu.dosw.proyect.persistence.entity;

import edu.dosw.proyect.core.models.enums.FieldPosition;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "STARTER_ENTRY")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StarterEntryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lineup_id", nullable = false)
    private TeamLineupEntity lineup;

    @Column(name = "player_id", nullable = false)
    private Long playerId;

    @Column(name = "player_name")
    private String playerName;

    @Enumerated(EnumType.STRING)
    @Column(name = "field_position")
    private FieldPosition fieldPosition;
}