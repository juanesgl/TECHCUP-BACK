package edu.dosw.proyect.controllers.mappers;

import edu.dosw.proyect.controllers.dtos.response.RegisterMatchResultResponseDTO;
import edu.dosw.proyect.controllers.dtos.response.StandingsTableResponseDTO;
import edu.dosw.proyect.controllers.dtos.response.TeamStandingDTO;
import edu.dosw.proyect.core.models.Partido;
import edu.dosw.proyect.core.models.enums.MatchStatus;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Mapper responsible for converting domain data into
 * standings-related DTOs.
 */
@Component
public class StandingsTableMapper {

    /**
     * Builds a TeamStandingDTO calculating GD and PTS from raw stats.
     *
     * @param position  row number in the table (1-based)
     * @param teamId    team identifier
     * @param teamName  display name
     * @param mp        matches played
     * @param w         wins
     * @param d         draws
     * @param l         losses
     * @param gf        goals for
     * @param ga        goals against
     * @return fully populated DTO
     */
    public TeamStandingDTO toTeamStandingDTO(int position, Long teamId, String teamName,
                                             int mp, int w, int d, int l, int gf, int ga) {
        return TeamStandingDTO.builder()
                .position(position)
                .teamId(teamId)
                .teamName(teamName)
                .matchesPlayed(mp)
                .wins(w)
                .draws(d)
                .losses(l)
                .goalsFor(gf)
                .goalsAgainst(ga)
                .goalDifference(gf - ga)
                .points((w * 3) + d)
                .build();
    }

    /**
     * Builds the full standings table response.
     */
    public StandingsTableResponseDTO toStandingsTableResponseDTO(String tournamentId,
                                                                 String tournamentName,
                                                                 int totalMatchesPlayed,
                                                                 List<TeamStandingDTO> standings) {
        return StandingsTableResponseDTO.builder()
                .tournamentId(tournamentId)
                .tournamentName(tournamentName)
                .totalTeams(standings.size())
                .totalMatchesPlayed(totalMatchesPlayed)
                .standings(standings)
                .build();
    }

    /**
     * Converts an already-updated Partido into a match result response DTO.
     */
    public RegisterMatchResultResponseDTO toRegisterMatchResultResponseDTO(Partido match) {
        String outcome;
        if (match.getGolesLocal() > match.getGolesVisitante()) {
            outcome = "HOME";
        } else if (match.getGolesLocal() < match.getGolesVisitante()) {
            outcome = "AWAY";
        } else {
            outcome = "DRAW";
        }

        String home = match.getEquipoLocal()     != null ? match.getEquipoLocal().getNombre()     : "Unknown";
        String away = match.getEquipoVisitante() != null ? match.getEquipoVisitante().getNombre() : "Unknown";

        return RegisterMatchResultResponseDTO.builder()
                .matchId(match.getId())
                .homeTeam(home)
                .awayTeam(away)
                .homeGoals(match.getGolesLocal())
                .awayGoals(match.getGolesVisitante())
                .outcome(outcome)
                .message("Result registered successfully. The standings table has been updated.")
                .build();
    }

    /**
     * Returns true if the match should be included in standings calculations.
     * Only FINALIZADO (FINISHED) and EN_JUEGO (IN_PROGRESS) count.
     */
    public boolean isMatchCountable(Partido match) {
        return match.getEstado() == MatchStatus.FINALIZADO
                || match.getEstado() == MatchStatus.EN_JUEGO;
    }
}