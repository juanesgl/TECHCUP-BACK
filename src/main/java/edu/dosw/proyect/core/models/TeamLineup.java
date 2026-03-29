package edu.dosw.proyect.core.models;

import edu.dosw.proyect.core.models.enums.LineupStatus;
import edu.dosw.proyect.core.models.enums.TacticalFormation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamLineup {
    private Long              id;
    private Long              teamId;
    private String            teamName;
    private Long              matchId;
    private Long              captainId;
    private TacticalFormation formation;
    private LineupStatus      status;
    private LocalDateTime     createdAt;
    private LocalDateTime     updatedAt;

    @Builder.Default
    private List<StarterEntry> starters  = new ArrayList<>();

    @Builder.Default
    private List<Long>         reserveIds = new ArrayList<>();
}
