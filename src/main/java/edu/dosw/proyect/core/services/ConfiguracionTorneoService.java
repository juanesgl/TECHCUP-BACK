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

        // 1. Validar Estado del torneo (Solo DRAFT o ACTIVE)
        if (tournament.getStatus() != TournamentsStatus.DRAFT && tournament.getStatus() != TournamentsStatus.ACTIVE) {
            throw new BusinessRuleException(
                    "La configuración solo está permitida cuando el torneo está en estado Borrador o Activo.");
        }

        // 2. Validar Organizador (Simulado: Si tiene uno asignado, debe coincidir. Si
        // no, lo asignamos asumiendo que es el dueño inicial en esta simulación de
        // flujo sin login JWT).
        if (tournament.getOrganizerId() != null && !tournament.getOrganizerId().equals(configDto.getOrganizerId())) {
            throw new BusinessRuleException(
                    "No tiene permisos para configurar este torneo. Solo el organizador puede hacerlo.");
        }

        // 3. Validar Fechas: Cierre de inscripción debe ser anterior a fecha final del
        // torneo.
        if (tournament.getEndDate() != null && configDto.getRegistrationCloseDate().isAfter(tournament.getEndDate())) {
            throw new BusinessRuleException(
                    "La fecha de cierre de inscripciones debe ser anterior a la fecha de finalización del torneo.");
        }

        // 4. Se debe obligar a tener al menos 1 cancha (Spring Validation lo garantiza
        // en el controller, pero lo reafirmamos si se requiere lógica extra).

        // Asignar variables al torneo
        tournament.setOrganizerId(configDto.getOrganizerId());
        tournament.setRegistrationCloseDate(configDto.getRegistrationCloseDate());
        tournament.setImportantDates(configDto.getImportantDates());
        tournament.setMatchSchedules(configDto.getMatchSchedules());
        tournament.setSanctions(configDto.getSanctions());
        tournament.setRegulation(configDto.getRegulation());

        // Manejo de canchas (vacíamos las viejas para re-guardar la configuración
        // limpia)
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

        // El mensaje final cumple el requisito visual
        return ConfiguracionTorneoResponseDTO.builder()
                .message("Los parámetros fueron guardados de manera exitosa")
                .tournId(tournament.getTournId())
                .registrationCloseDate(tournament.getRegistrationCloseDate())
                .canchas(configDto.getCanchas())
                .build();
    }
}
