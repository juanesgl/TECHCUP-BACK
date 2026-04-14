package edu.dosw.proyect.core.services;

import edu.dosw.proyect.controllers.dtos.request.CanchaDTO;
import edu.dosw.proyect.controllers.dtos.request.ConfiguracionTorneoRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.ConfiguracionTorneoResponseDTO;
import edu.dosw.proyect.core.exceptions.BusinessRuleException;
import edu.dosw.proyect.core.models.Tournament;
import edu.dosw.proyect.core.models.enums.TournamentsStatus;
import edu.dosw.proyect.persistence.entity.CanchaEntity;
import edu.dosw.proyect.persistence.entity.TournamentEntity;
import edu.dosw.proyect.persistence.repository.CanchaRepository;
import edu.dosw.proyect.persistence.repository.TournamentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConfiguracionTorneoService {

    private final TournamentService tournamentService;
    private final TournamentRepository tournamentRepository;
    private final CanchaRepository canchaRepository;

    public ConfiguracionTorneoResponseDTO configurarTorneo(String tournId,
                                                           ConfiguracionTorneoRequestDTO configDto) {
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

        for (CanchaDTO cDto : configDto.getCanchas()) {
            CanchaEntity c = new CanchaEntity();
            c.setNombre(cDto.getNombre());
            c.setDireccion(cDto.getUbicacion());
            c.setTorneo(entity);
            canchaRepository.save(c);
        }

        return ConfiguracionTorneoResponseDTO.builder()
                .message("Parametros guardados exitosamente")
                .tournId(tournament.getTournId())
                .registrationCloseDate(configDto.getRegistrationCloseDate())
                .canchas(configDto.getCanchas())
                .build();
    }
}