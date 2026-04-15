package edu.dosw.proyect.persistence.entity;

import edu.dosw.proyect.core.models.enums.LineupStatus;
import edu.dosw.proyect.core.models.enums.TacticalFormation;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "TEAM_LINEUP")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamLineupEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "team_id", nullable = false)
    private Long teamId;

    @Column(name = "team_name")
    private String teamName;

    @Column(name = "match_id", nullable = false)
    private Long matchId;

    @Column(name = "captain_id", nullable = false)
    private Long captainId;

    @Enumerated(EnumType.STRING)
    @Column(name = "formation")
    private TacticalFormation formation;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private LineupStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "lineup", cascade = CascadeType.ALL,
            orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<StarterEntryEntity> starters = new ArrayList<>();

    @Column(name = "reserve_ids", columnDefinition = "text")
    private String reserveIds;
}