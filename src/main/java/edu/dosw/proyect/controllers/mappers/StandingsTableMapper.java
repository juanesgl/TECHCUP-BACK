package edu.dosw.proyect.controllers.mappers;

import edu.dosw.proyect.controllers.dtos.response.RegisterMatchResultResponseDTO;
import edu.dosw.proyect.controllers.dtos.response.StandingsTableResponseDTO;
import edu.dosw.proyect.controllers.dtos.response.TeamStandingDTO;
import edu.dosw.proyect.core.models.Partido;
import edu.dosw.proyect.core.models.enums.MatchStatus;
import org.mapstruct.Mapper;

import java.util.List;


@Mapper(componentModel = "spring")
public interface StandingsTableMapper {

    default TeamStandingDTO toTeamStandingDTO(int position, Long teamId, String teamName,
                                              int mp, int w, int d, int l,
                                              int gf, int ga) {
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

    default StandingsTableResponseDTO toStandingsTableResponseDTO(String tournamentId,
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

    default RegisterMatchResultResponseDTO toRegisterMatchResultResponseDTO(Partido match) {
        String outcome;
        if (match.getGolesLocal() > match.getGolesVisitante())       outcome = "HOME";
        else if (match.getGolesLocal() < match.getGolesVisitante())  outcome = "AWAY";
        else                                                          outcome = "DRAW";

        String home = match.getTeamLocal()     != null
                ? match.getTeamLocal().getNombre()     : "Unknown";
        String away = match.getTeamVisitante() != null
                ? match.getTeamVisitante().getNombre() : "Unknown";

        return RegisterMatchResultResponseDTO.builder()
                .matchId(match.getId())
                .homeTeam(home)
                .awayTeam(away)
                .homeGoals(match.getGolesLocal())
                .awayGoals(match.getGolesVisitante())
                .outcome(outcome)
                .message("Resultado registrado exitosamente. La tabla de posiciones ha sido actualizada.")
                .build();
    }

    default boolean isMatchCountable(Partido match) {
        return match.getEstado() == MatchStatus.FINALIZADO
                || match.getEstado() == MatchStatus.EN_JUEGO;
    }
}