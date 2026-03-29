package edu.dosw.proyect.core.services;

import edu.dosw.proyect.controllers.dtos.request.CanchaDTO;
import edu.dosw.proyect.controllers.dtos.request.ConfiguracionTorneoRequestDTO;
import edu.dosw.proyect.controllers.dtos.response.ConfiguracionTorneoResponseDTO;
import edu.dosw.proyect.core.exceptions.BusinessRuleException;
import edu.dosw.proyect.core.models.Cancha;
import edu.dosw.proyect.core.models.Tournament;
import edu.dosw.proyect.core.models.enums.TournamentsStatus;
import edu.dosw.proyect.core.repositories.CanchaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class ConfiguracionTorneoService {

    private final TournamentService tournamentService;
    private final CanchaRepository canchaRepository;

    public ConfiguracionTorneoResponseDTO configurarTorneo(String tournId, ConfiguracionTorneoRequestDTO configDto) {
        Tournament tournament = tournamentService.getTournamentById(tournId);
        if (tournament.getStatus() != TournamentsStatus.DRAFT && tournament.getStatus() != TournamentsStatus.ACTIVE) {
            throw new BusinessRuleException(
                    "La configuraciÃ³n solo estÃ¡ permitida cuando el torneo estÃ¡ en estado Borrador o Activo.");
        }
        if (tournament.getOrganizerId() != null && !tournament.getOrganizerId().equals(configDto.getOrganizerId())) {
            throw new BusinessRuleException(
                    "No tiene permisos para configurar este torneo. Solo el organizador puede hacerlo.");
        }
        if (tournament.getEndDate() != null && configDto.getRegistrationCloseDate().isAfter(tournament.getEndDate())) {
            throw new BusinessRuleException(
                    "La fecha de cierre de inscripciones debe ser anterior a la fecha de finalizaciÃ³n del torneo.");
        }
        tournament.setOrganizerId(configDto.getOrganizerId());
        tournament.setRegistrationCloseDate(configDto.getRegistrationCloseDate());
        tournament.setImportantDates(configDto.getImportantDates());
        tournament.setMatchSchedules(configDto.getMatchSchedules());
        tournament.setSanctions(configDto.getSanctions());
        tournament.setRegulation(configDto.getRegulation());
        if (tournament.getCanchas() == null) {
            tournament.setCanchas(new ArrayList<>());
        }
        tournament.getCanchas().clear();

        for (CanchaDTO cDto : configDto.getCanchas()) {
            Cancha c = new Cancha();
            c.setNombre(cDto.getNombre());
            c.setUbicacion(cDto.getUbicacion());
            c.setTorneo(tournament);
            Cancha savedCancha = canchaRepository.save(c);
            tournament.getCanchas().add(savedCancha);
        }
        return ConfiguracionTorneoResponseDTO.builder()
                .message("Los parÃ¡metros fueron guardados de manera exitosa")
                .tournId(tournament.getTournId())
                .registrationCloseDate(tournament.getRegistrationCloseDate())
                .canchas(configDto.getCanchas())
                .build();
    }
}

