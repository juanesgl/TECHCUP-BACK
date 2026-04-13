package edu.dosw.proyect.controllers.mappers;

import edu.dosw.proyect.controllers.dtos.request.SaveLineupRequestDTO;
import edu.dosw.proyect.controllers.dtos.request.StarterEntryRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.StarterEntryResponseDTO;
import edu.dosw.proyect.controllers.dtos.response.TeamLineupResponseDTO;
import edu.dosw.proyect.core.models.StarterEntry;
import edu.dosw.proyect.core.models.TeamLineup;
import edu.dosw.proyect.core.models.User;
import edu.dosw.proyect.core.models.enums.LineupStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * MapStruct mapper.
 * Objeto origen: SaveLineupRequestDTO → TeamLineup (dominio).
 * Objeto origen: TeamLineup (dominio) → TeamLineupResponseDTO.
 * Usa métodos default para lógica compleja con playerMap.
 */
@Mapper(componentModel = "spring")
public interface TeamLineupMapper {

    @Mapping(target = "playerId",     source = "playerId")
    @Mapping(target = "playerName",   ignore = true)
    @Mapping(target = "fieldPosition",source = "fieldPosition")
    StarterEntry toStarterEntry(StarterEntryRequestDTO request);

    @Mapping(target = "playerId",     source = "playerId")
    @Mapping(target = "playerName",   source = "playerName")
    @Mapping(target = "fieldPosition",source = "fieldPosition")
    StarterEntryResponseDTO toStarterEntryResponseDTO(StarterEntry entry);

    default TeamLineup toNewTeamLineup(SaveLineupRequestDTO request,
                                       Long captainId,
                                       Map<Long, User> playerMap) {
        return TeamLineup.builder()
                .teamId(request.getTeamId())
                .matchId(request.getMatchId())
                .captainId(captainId)
                .formation(request.getFormation())
                .status(LineupStatus.SAVED)
                .starters(buildStarters(request.getStarters(), playerMap))
                .reserveIds(request.getReserveIds() != null
                        ? request.getReserveIds() : new ArrayList<>())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    default void applyUpdate(TeamLineup existing,
                             SaveLineupRequestDTO request,
                             Map<Long, User> playerMap) {
        existing.setFormation(request.getFormation());
        existing.setStarters(buildStarters(request.getStarters(), playerMap));
        existing.setReserveIds(request.getReserveIds() != null
                ? request.getReserveIds() : new ArrayList<>());
        existing.setStatus(LineupStatus.SAVED);
        existing.setUpdatedAt(LocalDateTime.now());
    }

    default TeamLineupResponseDTO toResponseDTO(TeamLineup lineup, String message) {
        List<StarterEntryResponseDTO> starterDTOs = lineup.getStarters().stream()
                .map(this::toStarterEntryResponseDTO)
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
                    return StarterEntry.builder()
                            .playerId(req.getPlayerId())
                            .playerName(player != null ? player.getName() : "Unknown")
                            .fieldPosition(req.getFieldPosition())
                            .build();
                })
                .collect(Collectors.toList());
    }
}