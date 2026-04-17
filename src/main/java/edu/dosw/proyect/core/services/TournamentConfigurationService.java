package edu.dosw.proyect.core.services;

import edu.dosw.proyect.controllers.dtos.request.SoccerFieldDTO;
import edu.dosw.proyect.controllers.dtos.request.TournamentConfigurationRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.TournamentConfigurationResponseDTO;
import edu.dosw.proyect.core.exceptions.BusinessRuleException;
import edu.dosw.proyect.core.models.Tournament;
import edu.dosw.proyect.core.models.enums.TournamentsStatus;
import edu.dosw.proyect.persistence.entity.SoccerFieldEntity;
import edu.dosw.proyect.persistence.entity.TournamentEntity;
import edu.dosw.proyect.persistence.repository.SoccerFieldRepository;
import edu.dosw.proyect.persistence.repository.TournamentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TournamentConfigurationService {

    private final TournamentService tournamentService;
    private final TournamentRepository tournamentRepository;
    private final SoccerFieldRepository soccerFieldRepository;

    public TournamentConfigurationResponseDTO configurarTorneo(String tournId,
                                                               TournamentConfigurationRequestDTO configDto) {
        Tournament tournament = tournamentService.getTournamentById(tournId);

        if (tournament.getStatus() != TournamentsStatus.DRAFT
                && tournament.getStatus() != TournamentsStatus.ACTIVE) {
            throw new BusinessRuleException(
                    "La configuracion solo esta permitida en estado Borrador o Activo.");
        }

        if (tournament.getOrganizerId() != null
                && !tournament.getOrganizerId().equals(configDto.getOrganizerId())) {
            throw new BusinessRuleException("No tiene permisos para configurar este torneo.");
        }

        if (tournament.getEndDate() != null
                && configDto.getRegistrationCloseDate().isAfter(tournament.getEndDate())) {
            throw new BusinessRuleException(
                    "La fecha de cierre debe ser anterior a la fecha de finalizacion.");
        }

        TournamentEntity entity = tournamentRepository.findByTournId(tournId)
                .orElseThrow(() -> new BusinessRuleException("Torneo no encontrado"));

        for (SoccerFieldDTO cDto : configDto.getCanchas()) {
            SoccerFieldEntity c = new SoccerFieldEntity();
            c.setNombre(cDto.getNombre());
            c.setDireccion(cDto.getUbicacion());
            c.setTorneo(entity);
            soccerFieldRepository.save(c);
        }

        return TournamentConfigurationResponseDTO.builder()
                .message("Parametros guardados exitosamente")
                .tournId(tournament.getTournId())
                .registrationCloseDate(configDto.getRegistrationCloseDate())
                .canchas(configDto.getCanchas())
                .build();
    }
}