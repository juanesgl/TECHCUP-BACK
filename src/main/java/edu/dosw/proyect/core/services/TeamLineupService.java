package edu.dosw.proyect.core.services;

import edu.dosw.proyect.controllers.dtos.request.SaveLineupRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.TeamLineupResponseDTO;

import java.util.List;

public interface TeamLineupService {

    TeamLineupResponseDTO saveLineup(Long captainId, SaveLineupRequestDTO request);

    TeamLineupResponseDTO updateLineup(Long captainId, Long lineupId,
                                       SaveLineupRequestDTO request);

    TeamLineupResponseDTO getLineup(Long captainId, Long teamId, Long matchId);

    List<TeamLineupResponseDTO> getTeamLineups(Long captainId, Long teamId);
}
