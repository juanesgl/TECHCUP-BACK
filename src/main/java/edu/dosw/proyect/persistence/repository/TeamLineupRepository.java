package edu.dosw.proyect.persistence.repository;

import edu.dosw.proyect.core.models.TeamLineup;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class TeamLineupRepository {

    private final Map<Long, TeamLineup> store = new HashMap<>();
    private long idSequence = 1;

    public Optional<TeamLineup> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    public TeamLineup save(TeamLineup lineup) {
        if (lineup.getId() == null) {
            lineup.setId(idSequence++);
        }
        store.put(lineup.getId(), lineup);
        return lineup;
    }

    public Optional<TeamLineup> findByTeamIdAndMatchId(Long teamId, Long matchId) {
        return store.values().stream()
                .filter(l -> teamId.equals(l.getTeamId())
                        && matchId.equals(l.getMatchId()))
                .findFirst();
    }

    public List<TeamLineup> findByTeamId(Long teamId) {
        return store.values().stream()
                .filter(l -> teamId.equals(l.getTeamId()))
                .collect(Collectors.toList());
    }

    public List<TeamLineup> findAll() {
        return new ArrayList<>(store.values());
    }
}
