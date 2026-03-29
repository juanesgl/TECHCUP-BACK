package edu.dosw.proyect.controllers.mappers;

import edu.dosw.proyect.controllers.dtos.request.SaveLineupRequestDTO;
import edu.dosw.proyect.controllers.dtos.request.StarterEntryRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.StarterEntryResponseDTO;
import edu.dosw.proyect.controllers.dtos.response.TeamLineupResponseDTO;
import edu.dosw.proyect.core.models.StarterEntry;
import edu.dosw.proyect.core.models.TeamLineup;
import edu.dosw.proyect.core.models.User;
import edu.dosw.proyect.core.models.enums.LineupStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Component
public class TeamLineupMapper {


    public TeamLineup toNewTeamLineup(SaveLineupRequestDTO request,
                                      Long captainId,
                                      Map<Long, User> playerMap) {
        List<StarterEntry> starters = buildStarters(request.getStarters(), playerMap);

        return TeamLineup.builder()
                .teamId(request.getTeamId())
                .matchId(request.getMatchId())
                .captainId(captainId)
                .formation(request.getFormation())
                .status(LineupStatus.SAVED)
                .starters(starters)
                .reserveIds(request.getReserveIds() != null
                        ? request.getReserveIds()
                        : new ArrayList<>())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }


    public void applyUpdate(TeamLineup existing,
                            SaveLineupRequestDTO request,
                            Map<Long, User> playerMap) {
        existing.setFormation(request.getFormation());
        existing.setStarters(buildStarters(request.getStarters(), playerMap));
        existing.setReserveIds(request.getReserveIds() != null
                ? request.getReserveIds()
                : new ArrayList<>());
        existing.setStatus(LineupStatus.SAVED);
        existing.setUpdatedAt(LocalDateTime.now());
    }


    public TeamLineupResponseDTO toResponseDTO(TeamLineup lineup, String message) {
        List<StarterEntryResponseDTO> starterDTOs = lineup.getStarters().stream()
                .map(s -> StarterEntryResponseDTO.builder()
                        .playerId(s.getPlayerId())
                        .playerName(s.getPlayerName())
                        .fieldPosition(s.getFieldPosition())
                        .build())
                .collect(Collectors.toList());

        return TeamLineupResponseDTO.builder()
                .lineupId(lineup.getId())
                .teamId(lineup.getTeamId())
                .teamName(lineup.getTeamName())
                .matchId(lineup.getMatchId())
                .formation(lineup.getFormation())
                .formationDisplay(lineup.getFormation() != null
                        ? lineup.getFormation().getDisplayName() : null)
                .status(lineup.getStatus())
                .starters(starterDTOs)
                .reserveIds(lineup.getReserveIds())
                .savedAt(lineup.getUpdatedAt())
                .message(message)
                .build();
    }


    private List<StarterEntry> buildStarters(List<StarterEntryRequestDTO> requests,
                                             Map<Long, User> playerMap) {
        return requests.stream()
                .map(req -> {
                    User player = playerMap.get(req.getPlayerId());
                    String name = (player != null) ? player.getName() : "Unknown";
                    return StarterEntry.builder()
                            .playerId(req.getPlayerId())
                            .playerName(name)
                            .fieldPosition(req.getFieldPosition())
                            .build();
                })
                .collect(Collectors.toList());
    }
}