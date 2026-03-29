package edu.dosw.proyect.controllers.dtos.response;

import edu.dosw.proyect.core.models.enums.LineupStatus;
import edu.dosw.proyect.core.models.enums.TacticalFormation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamLineupResponseDTO {
    private Long                        lineupId;
    private Long                        teamId;
    private String                      teamName;
    private Long                        matchId;
    private TacticalFormation           formation;
    private String                      formationDisplay;   
    private LineupStatus                status;
    private List<StarterEntryResponseDTO> starters;
    private List<Long>                  reserveIds;
    private LocalDateTime               savedAt;
    private String                      message;
}