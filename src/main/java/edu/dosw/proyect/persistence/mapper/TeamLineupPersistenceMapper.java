package edu.dosw.proyect.persistence.mapper;

import edu.dosw.proyect.core.models.StarterEntry;
import edu.dosw.proyect.core.models.TeamLineup;
import edu.dosw.proyect.persistence.entity.StarterEntryEntity;
import edu.dosw.proyect.persistence.entity.TeamLineupEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TeamLineupPersistenceMapper {

        public TeamLineupEntity toEntity(TeamLineup domain) {
                if (domain == null)
                        return null;

                List<StarterEntryEntity> starters = domain.getStarters() != null
                                ? domain.getStarters().stream()
                                                .map(this::toStarterEntity)
                                                .toList()
                                : Collections.emptyList();

                String reserveIds = domain.getReserveIds() != null
                                ? domain.getReserveIds().stream()
                                                .map(String::valueOf)
                                                .collect(Collectors.joining(","))
                                : null;

                TeamLineupEntity entity = TeamLineupEntity.builder()
                                .id(domain.getId())
                                .teamId(domain.getTeamId())
                                .teamName(domain.getTeamName())
                                .matchId(domain.getMatchId())
                                .captainId(domain.getCaptainId())
                                .formation(domain.getFormation())
                                .status(domain.getStatus())
                                .createdAt(domain.getCreatedAt())
                                .updatedAt(domain.getUpdatedAt())
                                .reserveIds(reserveIds)
                                .build();

                for (StarterEntryEntity s : starters) {
                        s.setLineup(entity);
                }
                entity.setStarters(starters);

                return entity;
        }

        public TeamLineup toDomain(TeamLineupEntity entity) {
                if (entity == null)
                        return null;

                List<StarterEntry> starters = entity.getStarters() != null
                                ? entity.getStarters().stream()
                                                .map(this::toStarterDomain)
                                                .toList()
                                : Collections.emptyList();

                List<Long> reserveIds = new ArrayList<>();
                if (entity.getReserveIds() != null && !entity.getReserveIds().isBlank()) {
                        reserveIds = Arrays.stream(entity.getReserveIds().split(","))
                                        .map(String::trim)
                                        .filter(s -> !s.isEmpty())
                                        .map(Long::parseLong)
                                        .toList();
                }

                return TeamLineup.builder()
                                .id(entity.getId())
                                .teamId(entity.getTeamId())
                                .teamName(entity.getTeamName())
                                .matchId(entity.getMatchId())
                                .captainId(entity.getCaptainId())
                                .formation(entity.getFormation())
                                .status(entity.getStatus())
                                .createdAt(entity.getCreatedAt())
                                .updatedAt(entity.getUpdatedAt())
                                .starters(starters)
                                .reserveIds(reserveIds)
                                .build();
        }

        private StarterEntryEntity toStarterEntity(StarterEntry domain) {
                return StarterEntryEntity.builder()
                                .playerId(domain.getPlayerId())
                                .playerName(domain.getPlayerName())
                                .fieldPosition(domain.getFieldPosition())
                                .build();
        }

        private StarterEntry toStarterDomain(StarterEntryEntity entity) {
                return StarterEntry.builder()
                                .playerId(entity.getPlayerId())
                                .playerName(entity.getPlayerName())
                                .fieldPosition(entity.getFieldPosition())
                                .build();
        }
}